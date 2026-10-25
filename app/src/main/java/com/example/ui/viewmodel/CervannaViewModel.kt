package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CervannaRepository
import com.example.data.security.SecureSessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CervannaUiState(
    val isAuthenticated: Boolean = true, // Default active session for immediate usability, logout returns to AuthScreen
    val currentRole: UserRole = UserRole.CUSTOMER,
    val customerTab: Int = 0, // 0: Home, 1: Explore, 2: Bookings, 3: Favorites, 4: Account
    val specialistTab: Int = 0, // 0: Dashboard, 1: Bookings, 2: Availability, 3: Earnings, 4: Profile
    // Search & Filter
    val searchQuery: String = "",
    val selectedServiceCategory: ServiceCategory? = null,
    val selectedProfessionalCategory: ProfessionalCategory? = null,
    val selectedLocation: String? = null,
    val minRating: Double = 0.0,
    val maxDailyPriceKsh: Int = 10000,
    val sortBy: String = "Rating: High to Low",
    // Navigation / Dialog details
    val viewingSpecialist: Specialist? = null,
    val viewingBooking: Booking? = null,
    // Booking Sheet
    val isBookingSheetOpen: Boolean = false,
    val bookingStep: Int = 1,
    val bookingSpecialist: Specialist? = null,
    val bookingService: ServiceCategory? = null,
    val bookingLocation: String = "Nairobi - Eastern Bypass",
    val bookingStartDate: String = "28 Oct 2026",
    val bookingShiftType: String = "Day Shift (8am - 5pm)",
    val bookingDurationDays: Int = 3,
    val bookingRequirements: String = "",
    // M-Pesa STK Prompt
    val isMpesaPromptOpen: Boolean = false,
    val mpesaTargetBookingId: String? = null,
    val mpesaTargetAmount: Int = 0,
    val mpesaPhoneNumber: String = "0712345678",
    val isProcessingMpesa: Boolean = false,
    val mpesaSuccessReceipt: String? = null,
    // Chat
    val isChatSheetOpen: Boolean = false,
    val chatBooking: Booking? = null,
    val chatInputText: String = "",
    // Review
    val isReviewSheetOpen: Boolean = false,
    val reviewBooking: Booking? = null,
    val reviewRating: Int = 5,
    val reviewComment: String = "",
    // Modals
    val isSupportModalOpen: Boolean = false,
    val isNotificationsOpen: Boolean = false,
    val isSpecialistRateEditOpen: Boolean = false,
    val isSpecialistAvailabilityEditOpen: Boolean = false,
    val snackbarMessage: String? = null
)

class CervannaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CervannaRepository()
    private val sessionManager = SecureSessionManager(application.applicationContext)

    private val _uiState = MutableStateFlow(
        CervannaUiState(
            isAuthenticated = sessionManager.sessionState.value.isLoggedIn,
            currentRole = sessionManager.sessionState.value.role
        )
    )
    val uiState: StateFlow<CervannaUiState> = _uiState.asStateFlow()

    init {
        // If there is no previous session, initialize default demo session as logged in
        if (!sessionManager.sessionState.value.isLoggedIn) {
            sessionManager.saveSession(
                token = "cervanna_jwt_initial_token",
                role = UserRole.CUSTOMER,
                email = "sarah.kamau@cervannacare.ke",
                name = "Sarah Kamau"
            )
            _uiState.update { it.copy(isAuthenticated = true, currentRole = UserRole.CUSTOMER) }
        }
    }

    val specialists: StateFlow<List<Specialist>> = repository.specialists
    val bookings: StateFlow<List<Booking>> = repository.bookings
    val reviews: StateFlow<List<Review>> = repository.reviews
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val transactions: StateFlow<List<SpecialistTransaction>> = repository.transactions
    val subscriptionPlan: StateFlow<SubscriptionPlan> = repository.subscriptionPlan
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = repository.chatMessages
    val customerProfile: StateFlow<CustomerProfile> = repository.customerProfile

    // Filtered specialists
    val filteredSpecialists: StateFlow<List<Specialist>> = combine(
        repository.specialists,
        _uiState
    ) { list, state ->
        var res = list

        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.trim().lowercase()
            res = res.filter {
                it.name.lowercase().contains(q) ||
                it.title.lowercase().contains(q) ||
                it.skills.any { skill -> skill.lowercase().contains(q) } ||
                it.locations.any { loc -> loc.lowercase().contains(q) } ||
                it.serviceCategories.any { sc -> sc.displayName.lowercase().contains(q) }
            }
        }

        if (state.selectedServiceCategory != null) {
            res = res.filter { it.serviceCategories.contains(state.selectedServiceCategory) }
        }

        if (state.selectedProfessionalCategory != null) {
            res = res.filter { it.professionalCategory == state.selectedProfessionalCategory }
        }

        if (!state.selectedLocation.isNullOrBlank()) {
            res = res.filter { it.locations.any { loc -> loc.contains(state.selectedLocation, ignoreCase = true) } }
        }

        if (state.minRating > 0.0) {
            res = res.filter { it.rating >= state.minRating }
        }

        res = res.filter { it.dailyRateKsh <= state.maxDailyPriceKsh }

        when (state.sortBy) {
            "Rating: High to Low" -> res.sortedByDescending { it.rating }
            "Price: Low to High" -> res.sortedBy { it.dailyRateKsh }
            "Price: High to Low" -> res.sortedByDescending { it.dailyRateKsh }
            "Experience: High to Low" -> res.sortedByDescending { it.experienceYears }
            else -> res
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current Specialist when in SPECIALIST role
    val currentSpecialist: StateFlow<Specialist?> = combine(
        repository.specialists,
        repository.currentSpecialistId
    ) { specs, id ->
        specs.find { it.id == id } ?: specs.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Earnings calculation with exact 15% healthcare commission
    val earningsSummary: StateFlow<SpecialistEarningsSummary> = repository.transactions.map { txList ->
        val totalGross = txList.sumOf { it.grossKsh }
        val totalCommission = txList.sumOf { it.commissionKsh }
        val netEarnings = totalGross - totalCommission
        val pendingPayout = txList.filter { it.payoutStatus.contains("Pending", ignoreCase = true) }
            .sumOf { it.netKsh }

        SpecialistEarningsSummary(
            totalGrossKsh = totalGross,
            commissionRatePct = 15.0,
            totalCommissionKsh = totalCommission,
            netEarningsKsh = netEarnings,
            pendingPayoutKsh = pendingPayout,
            completedEngagements = txList.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SpecialistEarningsSummary(54000, 15.0, 8100, 45900, 19125, 3)
    )

    // ================= AUTHENTICATION & ROLE ROUTING =================

    fun login(role: UserRole, email: String, name: String) {
        val token = "cervanna_jwt_" + System.currentTimeMillis()
        sessionManager.saveSession(
            token = token,
            role = role,
            email = email,
            name = name,
            specialistId = if (role == UserRole.SPECIALIST) "spec_1" else null
        )

        _uiState.update {
            it.copy(
                isAuthenticated = true,
                currentRole = role,
                customerTab = 0,
                specialistTab = 0,
                viewingSpecialist = null,
                viewingBooking = null,
                snackbarMessage = if (role == UserRole.CUSTOMER)
                    "Welcome, $name! Navigating to Customer Home."
                else
                    "Welcome, $name! Navigating to Specialist Dashboard."
            )
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                viewingSpecialist = null,
                viewingBooking = null,
                isBookingSheetOpen = false,
                isChatSheetOpen = false,
                snackbarMessage = "Logged out successfully."
            )
        }
    }

    // ================= ROLE & TABS =================

    fun switchRole(newRole: UserRole) {
        val currentSession = sessionManager.sessionState.value
        sessionManager.saveSession(
            token = currentSession.token ?: "cervanna_jwt_switched",
            role = newRole,
            email = currentSession.email ?: "user@cervannacare.com",
            name = if (newRole == UserRole.CUSTOMER) "Sarah Kamau" else "Faith Muthoni, RN",
            specialistId = if (newRole == UserRole.SPECIALIST) "spec_1" else null
        )

        _uiState.update {
            it.copy(
                currentRole = newRole,
                customerTab = 0,
                specialistTab = 0,
                viewingSpecialist = null,
                viewingBooking = null,
                isBookingSheetOpen = false,
                isChatSheetOpen = false,
                snackbarMessage = if (newRole == UserRole.SPECIALIST)
                    "Switched to Specialist Mode (Faith Muthoni, RN) -> Specialist Dashboard"
                else
                    "Switched to Customer Mode -> Customer Home"
            )
        }
    }

    fun setCustomerTab(tabIndex: Int) {
        _uiState.update { it.copy(customerTab = tabIndex, viewingSpecialist = null, viewingBooking = null) }
    }

    fun setSpecialistTab(tabIndex: Int) {
        _uiState.update { it.copy(specialistTab = tabIndex, viewingSpecialist = null, viewingBooking = null) }
    }

    // ================= SEARCH & FILTER =================

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectServiceCategory(cat: ServiceCategory?) {
        _uiState.update {
            it.copy(
                selectedServiceCategory = if (it.selectedServiceCategory == cat) null else cat
            )
        }
    }

    fun selectProfessionalCategory(cat: ProfessionalCategory?) {
        _uiState.update {
            it.copy(
                selectedProfessionalCategory = if (it.selectedProfessionalCategory == cat) null else cat
            )
        }
    }

    fun selectLocation(loc: String?) {
        _uiState.update {
            it.copy(selectedLocation = if (it.selectedLocation == loc) null else loc)
        }
    }

    fun setSortBy(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedServiceCategory = null,
                selectedProfessionalCategory = null,
                selectedLocation = null,
                minRating = 0.0,
                maxDailyPriceKsh = 10000,
                sortBy = "Rating: High to Low"
            )
        }
    }

    // ================= DETAILS & ACTIONS =================

    fun viewSpecialistDetails(specialist: Specialist) {
        _uiState.update { it.copy(viewingSpecialist = specialist) }
    }

    fun closeSpecialistDetails() {
        _uiState.update { it.copy(viewingSpecialist = null) }
    }

    fun toggleFavorite(specialistId: String) {
        repository.toggleFavorite(specialistId)
        _uiState.update {
            val updatedViewing = it.viewingSpecialist?.let { s ->
                if (s.id == specialistId) s.copy(isFavorite = !s.isFavorite) else s
            }
            it.copy(viewingSpecialist = updatedViewing)
        }
    }

    // ================= BOOKING FLOW =================

    fun startBookingFlow(specialist: Specialist, preselectedService: ServiceCategory? = null) {
        val initialService = preselectedService ?: specialist.serviceCategories.firstOrNull() ?: ServiceCategory.ELDERLY_CARE
        _uiState.update {
            it.copy(
                isBookingSheetOpen = true,
                bookingStep = 1,
                bookingSpecialist = specialist,
                bookingService = initialService,
                bookingLocation = "Eastern Bypass, Kamakis, Nairobi",
                bookingStartDate = "28 Oct 2026",
                bookingShiftType = specialist.shiftTypes.firstOrNull() ?: "Day Shift (8am - 5pm)",
                bookingDurationDays = 3,
                bookingRequirements = ""
            )
        }
    }

    fun updateBookingService(service: ServiceCategory) {
        _uiState.update { it.copy(bookingService = service) }
    }

    fun updateBookingShift(shift: String) {
        _uiState.update { it.copy(bookingShiftType = shift) }
    }

    fun updateBookingLocation(loc: String) {
        _uiState.update { it.copy(bookingLocation = loc) }
    }

    fun updateBookingStartDate(date: String) {
        _uiState.update { it.copy(bookingStartDate = date) }
    }

    fun updateBookingDurationDays(days: Int) {
        _uiState.update { it.copy(bookingDurationDays = days.coerceAtLeast(1)) }
    }

    fun updateBookingRequirements(req: String) {
        _uiState.update { it.copy(bookingRequirements = req) }
    }

    fun setBookingStep(step: Int) {
        _uiState.update { it.copy(bookingStep = step) }
    }

    fun closeBookingSheet() {
        _uiState.update { it.copy(isBookingSheetOpen = false) }
    }

    fun submitBookingRequest(andOpenMpesa: Boolean = true) {
        val state = _uiState.value
        val specialist = state.bookingSpecialist ?: return
        val service = state.bookingService ?: ServiceCategory.ELDERLY_CARE

        val booking = repository.createBooking(
            specialistId = specialist.id,
            serviceCategory = service,
            location = state.bookingLocation,
            startDate = state.bookingStartDate,
            shiftType = state.bookingShiftType,
            durationDays = state.bookingDurationDays,
            careRequirements = state.bookingRequirements.ifBlank { "Standard certified home care per agreed schedule." }
        )

        _uiState.update {
            it.copy(
                isBookingSheetOpen = false,
                snackbarMessage = "Booking #${booking.id} submitted to ${specialist.name}!"
            )
        }

        if (andOpenMpesa) {
            openMpesaPrompt(booking.id, booking.quoteKsh)
        }
    }

    // ================= M-PESA STK PUSH =================

    fun openMpesaPrompt(bookingId: String, amount: Int) {
        _uiState.update {
            it.copy(
                isMpesaPromptOpen = true,
                mpesaTargetBookingId = bookingId,
                mpesaTargetAmount = amount,
                isProcessingMpesa = false,
                mpesaSuccessReceipt = null
            )
        }
    }

    fun onMpesaPhoneChanged(phone: String) {
        _uiState.update { it.copy(mpesaPhoneNumber = phone) }
    }

    fun closeMpesaPrompt() {
        _uiState.update { it.copy(isMpesaPromptOpen = false, isProcessingMpesa = false) }
    }

    fun triggerMpesaPayment() {
        val bookingId = _uiState.value.mpesaTargetBookingId ?: return
        val phone = _uiState.value.mpesaPhoneNumber
        val amount = _uiState.value.mpesaTargetAmount

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingMpesa = true) }
            delay(1500) // realistic STK push processing time

            repository.payBookingWithMpesa(bookingId, phone)
            val receipt = "QKT${(100000..999999).random()}KE"

            _uiState.update {
                it.copy(
                    isProcessingMpesa = false,
                    mpesaSuccessReceipt = receipt,
                    snackbarMessage = "M-Pesa payment of KSh $amount successful! Receipt: $receipt"
                )
            }
        }
    }

    // ================= BOOKING MANAGEMENT =================

    fun cancelBooking(bookingId: String, reason: String) {
        repository.cancelBooking(bookingId, reason)
        _uiState.update { it.copy(snackbarMessage = "Booking #$bookingId cancelled.") }
    }

    fun rescheduleBooking(bookingId: String, newDate: String) {
        repository.rescheduleBooking(bookingId, newDate)
        _uiState.update { it.copy(snackbarMessage = "Booking #$bookingId rescheduled to $newDate.") }
    }

    // ================= SPECIALIST ACTIONS =================

    fun acceptBooking(bookingId: String) {
        repository.acceptBooking(bookingId)
        _uiState.update { it.copy(snackbarMessage = "Booking #$bookingId confirmed!") }
    }

    fun declineBooking(bookingId: String, reason: String) {
        repository.declineBooking(bookingId, reason)
        _uiState.update { it.copy(snackbarMessage = "Booking #$bookingId declined.") }
    }

    fun completeBooking(bookingId: String) {
        repository.completeBooking(bookingId)
        _uiState.update { it.copy(snackbarMessage = "Booking #$bookingId marked as completed.") }
    }

    fun toggleSpecialistAvailability() {
        val specId = repository.currentSpecialistId.value
        repository.toggleSpecialistAvailability(specId)
    }

    fun updateSpecialistWorkingDays(days: List<String>) {
        val specId = repository.currentSpecialistId.value
        repository.updateSpecialistAvailabilityDays(specId, days)
        _uiState.update { it.copy(isSpecialistAvailabilityEditOpen = false, snackbarMessage = "Availability schedule updated.") }
    }

    fun updateSpecialistRates(hourly: Int, daily: Int, monthly: Int) {
        val specId = repository.currentSpecialistId.value
        repository.updateSpecialistRates(specId, hourly, daily, monthly)
        _uiState.update { it.copy(isSpecialistRateEditOpen = false, snackbarMessage = "Service rates updated.") }
    }

    fun requestMpesaPayout(amount: Int) {
        val spec = currentSpecialist.value ?: return
        val ref = repository.requestMpesaPayout(amount, spec.phoneNumber)
        _uiState.update { it.copy(snackbarMessage = "Payout of KSh $amount sent to M-Pesa ${spec.phoneNumber} (Ref: $ref)") }
    }

    fun renewSubscription() {
        val spec = currentSpecialist.value ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingMpesa = true) }
            delay(1200)
            repository.renewSubscriptionWithMpesa(spec.phoneNumber)
            _uiState.update {
                it.copy(
                    isProcessingMpesa = false,
                    snackbarMessage = "Individual Specialist Subscription renewed for KSh 500."
                )
            }
        }
    }

    // ================= CHAT =================

    fun openChat(booking: Booking) {
        _uiState.update {
            it.copy(
                isChatSheetOpen = true,
                chatBooking = booking,
                chatInputText = ""
            )
        }
    }

    fun closeChat() {
        _uiState.update { it.copy(isChatSheetOpen = false, chatBooking = null) }
    }

    fun onChatInputChanged(text: String) {
        _uiState.update { it.copy(chatInputText = text) }
    }

    fun sendChatMessage() {
        val text = _uiState.value.chatInputText.trim()
        val booking = _uiState.value.chatBooking ?: return
        if (text.isBlank()) return

        val role = _uiState.value.currentRole
        val senderName = if (role == UserRole.CUSTOMER)
            _uiState.value.chatBooking?.customerName ?: "Sarah Kamau"
        else
            currentSpecialist.value?.name ?: "Faith Muthoni, RN"

        repository.sendChatMessage(booking.id, role, senderName, text)
        _uiState.update { it.copy(chatInputText = "") }
    }

    // ================= REVIEWS =================

    fun openReviewSheet(booking: Booking) {
        _uiState.update {
            it.copy(
                isReviewSheetOpen = true,
                reviewBooking = booking,
                reviewRating = 5,
                reviewComment = ""
            )
        }
    }

    fun closeReviewSheet() {
        _uiState.update { it.copy(isReviewSheetOpen = false, reviewBooking = null) }
    }

    fun onReviewRatingChanged(r: Int) {
        _uiState.update { it.copy(reviewRating = r) }
    }

    fun onReviewCommentChanged(c: String) {
        _uiState.update { it.copy(reviewComment = c) }
    }

    fun submitReview() {
        val booking = _uiState.value.reviewBooking ?: return
        val rating = _uiState.value.reviewRating
        val comment = _uiState.value.reviewComment.ifBlank { "Exceptional, compassionate care and reliable service." }

        repository.submitReview(
            specialistId = booking.specialistId,
            rating = rating,
            comment = comment,
            serviceName = booking.serviceName
        )

        _uiState.update {
            it.copy(
                isReviewSheetOpen = false,
                reviewBooking = null,
                snackbarMessage = "Review submitted! Thank you for rating ${booking.specialistName}."
            )
        }
    }

    // ================= MODALS & SNACKBAR =================

    fun openSupportModal() {
        _uiState.update { it.copy(isSupportModalOpen = true) }
    }

    fun closeSupportModal() {
        _uiState.update { it.copy(isSupportModalOpen = false) }
    }

    fun openNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = true) }
    }

    fun closeNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = false) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
