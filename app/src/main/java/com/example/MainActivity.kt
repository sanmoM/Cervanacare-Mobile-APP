package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.*
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.customer.*
import com.example.ui.screens.specialist.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CervannaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CervannaApp()
            }
        }
    }
}

@Composable
fun CervannaApp(viewModel: CervannaViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val specialists by viewModel.specialists.collectAsStateWithLifecycle()
    val filteredSpecialists by viewModel.filteredSpecialists.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val currentSpecialist by viewModel.currentSpecialist.collectAsStateWithLifecycle()
    val earningsSummary by viewModel.earningsSummary.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val subscriptionPlan by viewModel.subscriptionPlan.collectAsStateWithLifecycle()
    val customerProfile by viewModel.customerProfile.collectAsStateWithLifecycle()
    val chatMessagesMap by viewModel.chatMessages.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Display snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnackbar()
        }
    }

    // Unauthenticated state: display AuthScreen with role selector and demo logins
    if (!uiState.isAuthenticated) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AuthScreen(
                    onLoginSuccess = { role, email, name ->
                        viewModel.login(role, email, name)
                    },
                    onDemoLoginCustomer = {
                        viewModel.login(UserRole.CUSTOMER, "sarah.kamau@cervannacare.ke", "Sarah Kamau")
                    },
                    onDemoLoginSpecialist = {
                        viewModel.login(UserRole.SPECIALIST, "faith.muthoni@cervannacare.com", "Faith Muthoni, RN")
                    }
                )
            }
        }
        return
    }

    // Authenticated state: handle back button for sub-screens
    BackHandler(enabled = uiState.viewingSpecialist != null || uiState.isBookingSheetOpen || uiState.isChatSheetOpen) {
        if (uiState.isChatSheetOpen) {
            viewModel.closeChat()
        } else if (uiState.isBookingSheetOpen) {
            viewModel.closeBookingSheet()
        } else if (uiState.viewingSpecialist != null) {
            viewModel.closeSpecialistDetails()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.viewingSpecialist == null) {
                CervannaTopAppBar(
                    currentRole = uiState.currentRole,
                    unreadNotificationCount = notifications.count { !it.isRead && it.roleTarget == uiState.currentRole },
                    onSwitchRole = { newRole -> viewModel.switchRole(newRole) },
                    onNotificationsClick = { viewModel.openNotifications() },
                    onSupportClick = { viewModel.openSupportModal() }
                )
            }
        },
        bottomBar = {
            if (uiState.viewingSpecialist == null) {
                CervannaBottomNav(
                    currentRole = uiState.currentRole,
                    selectedTabIndex = if (uiState.currentRole == UserRole.CUSTOMER) uiState.customerTab else uiState.specialistTab,
                    onTabSelected = { tab ->
                        if (uiState.currentRole == UserRole.CUSTOMER) {
                            viewModel.setCustomerTab(tab)
                        } else {
                            viewModel.setSpecialistTab(tab)
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.viewingSpecialist != null) {
                // Full Specialist Profile Detail Screen
                CustomerSpecialistDetailScreen(
                    specialist = uiState.viewingSpecialist!!,
                    reviews = reviews,
                    onBack = { viewModel.closeSpecialistDetails() },
                    onBookNow = { viewModel.startBookingFlow(uiState.viewingSpecialist!!) },
                    onContactSpecialist = {
                        viewModel.startBookingFlow(uiState.viewingSpecialist!!)
                    },
                    onToggleFavorite = { viewModel.toggleFavorite(uiState.viewingSpecialist!!.id) }
                )
            } else {
                // Route to appropriate role screens: Customer vs Specialist
                if (uiState.currentRole == UserRole.CUSTOMER) {
                    when (uiState.customerTab) {
                        0 -> CustomerHomeScreen(
                            specialists = specialists,
                            upcomingBookings = bookings,
                            onSelectService = { service ->
                                viewModel.selectServiceCategory(service)
                                viewModel.setCustomerTab(1) // Jump to explore
                            },
                            onSelectSpecialist = { spec -> viewModel.viewSpecialistDetails(spec) },
                            onBookSpecialist = { spec -> viewModel.startBookingFlow(spec) },
                            onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                            onExploreClick = { viewModel.setCustomerTab(1) },
                            onLocationChange = { loc -> viewModel.selectLocation(loc) },
                            onSupportClick = { viewModel.openSupportModal() }
                        )

                        1 -> CustomerExploreScreen(
                            specialists = filteredSpecialists,
                            searchQuery = uiState.searchQuery,
                            selectedServiceCategory = uiState.selectedServiceCategory,
                            selectedProfessionalCategory = uiState.selectedProfessionalCategory,
                            selectedLocation = uiState.selectedLocation,
                            sortBy = uiState.sortBy,
                            onSearchChange = { q -> viewModel.onSearchQueryChanged(q) },
                            onSelectServiceCategory = { sc -> viewModel.selectServiceCategory(sc) },
                            onSelectProfessionalCategory = { pc -> viewModel.selectProfessionalCategory(pc) },
                            onSelectLocation = { loc -> viewModel.selectLocation(loc) },
                            onSortChange = { s -> viewModel.setSortBy(s) },
                            onResetFilters = { viewModel.resetFilters() },
                            onSelectSpecialist = { spec -> viewModel.viewSpecialistDetails(spec) },
                            onBookSpecialist = { spec -> viewModel.startBookingFlow(spec) },
                            onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
                        )

                        2 -> CustomerBookingsScreen(
                            bookings = bookings,
                            onPayMpesa = { booking -> viewModel.openMpesaPrompt(booking.id, booking.quoteKsh) },
                            onChatClick = { booking -> viewModel.openChat(booking) },
                            onReviewClick = { booking -> viewModel.openReviewSheet(booking) },
                            onCancelBooking = { id, reason -> viewModel.cancelBooking(id, reason) },
                            onRescheduleBooking = { id, date -> viewModel.rescheduleBooking(id, date) },
                            onSupportClick = { viewModel.openSupportModal() }
                        )

                        3 -> CustomerFavoritesScreen(
                            favoriteSpecialists = specialists.filter { it.isFavorite },
                            onSelectSpecialist = { spec -> viewModel.viewSpecialistDetails(spec) },
                            onBookSpecialist = { spec -> viewModel.startBookingFlow(spec) },
                            onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                            onExploreClick = { viewModel.setCustomerTab(1) }
                        )

                        4 -> CustomerAccountScreen(
                            profile = customerProfile,
                            onSwitchRole = { newRole -> viewModel.switchRole(newRole) },
                            onSupportClick = { viewModel.openSupportModal() },
                            onNotificationsClick = { viewModel.openNotifications() },
                            onLogout = { viewModel.logout() }
                        )
                    }
                } else {
                    // SPECIALIST ROLE -> routes to Specialist Dashboard & Specialist Tabs
                    val activeSpecialist = currentSpecialist ?: specialists.first()

                    when (uiState.specialistTab) {
                        0 -> SpecialistDashboardScreen(
                            specialist = activeSpecialist,
                            bookings = bookings,
                            earningsSummary = earningsSummary,
                            onAcceptBooking = { id -> viewModel.acceptBooking(id) },
                            onDeclineBooking = { id, reason -> viewModel.declineBooking(id, reason) },
                            onToggleAvailability = { viewModel.toggleSpecialistAvailability() },
                            onChatClick = { booking -> viewModel.openChat(booking) },
                            onRequestPayout = { amt -> viewModel.requestMpesaPayout(amt) }
                        )

                        1 -> SpecialistBookingsScreen(
                            bookings = bookings,
                            onAcceptBooking = { id -> viewModel.acceptBooking(id) },
                            onDeclineBooking = { id, reason -> viewModel.declineBooking(id, reason) },
                            onCompleteBooking = { id -> viewModel.completeBooking(id) },
                            onChatClick = { booking -> viewModel.openChat(booking) }
                        )

                        2 -> SpecialistAvailabilityScreen(
                            specialist = activeSpecialist,
                            onSaveWorkingDays = { days -> viewModel.updateSpecialistWorkingDays(days) }
                        )

                        3 -> SpecialistEarningsScreen(
                            earningsSummary = earningsSummary,
                            transactions = transactions,
                            subscriptionPlan = subscriptionPlan,
                            onRequestPayout = { amt -> viewModel.requestMpesaPayout(amt) },
                            onRenewSubscription = { viewModel.renewSubscription() }
                        )

                        4 -> SpecialistProfileScreen(
                            specialist = activeSpecialist,
                            onUpdateRates = { h, d, m -> viewModel.updateSpecialistRates(h, d, m) },
                            onSwitchRole = { newRole -> viewModel.switchRole(newRole) },
                            onSupportClick = { viewModel.openSupportModal() },
                            onLogout = { viewModel.logout() }
                        )
                    }
                }
            }
        }
    }

    // Interactive Booking Sheet Modal
    if (uiState.isBookingSheetOpen && uiState.bookingSpecialist != null) {
        CustomerBookingSheet(
            specialist = uiState.bookingSpecialist!!,
            selectedService = uiState.bookingService ?: uiState.bookingSpecialist!!.serviceCategories.first(),
            selectedShift = uiState.bookingShiftType,
            location = uiState.bookingLocation,
            startDate = uiState.bookingStartDate,
            durationDays = uiState.bookingDurationDays,
            requirements = uiState.bookingRequirements,
            currentStep = uiState.bookingStep,
            onServiceChange = { sc -> viewModel.updateBookingService(sc) },
            onShiftChange = { shift -> viewModel.updateBookingShift(shift) },
            onLocationChange = { loc -> viewModel.updateBookingLocation(loc) },
            onStartDateChange = { date -> viewModel.updateBookingStartDate(date) },
            onDurationChange = { days -> viewModel.updateBookingDurationDays(days) },
            onRequirementsChange = { req -> viewModel.updateBookingRequirements(req) },
            onStepChange = { step -> viewModel.setBookingStep(step) },
            onSubmitBooking = { andMpesa -> viewModel.submitBookingRequest(andMpesa) },
            onDismiss = { viewModel.closeBookingSheet() }
        )
    }

    // Lipa na M-Pesa STK Push Dialog
    if (uiState.isMpesaPromptOpen) {
        MpesaPaymentDialog(
            bookingId = uiState.mpesaTargetBookingId,
            amountKsh = uiState.mpesaTargetAmount,
            phoneNumber = uiState.mpesaPhoneNumber,
            isProcessing = uiState.isProcessingMpesa,
            receiptRef = uiState.mpesaSuccessReceipt,
            onPhoneChange = { phone -> viewModel.onMpesaPhoneChanged(phone) },
            onSubmitPay = { viewModel.triggerMpesaPayment() },
            onDismiss = { viewModel.closeMpesaPrompt() }
        )
    }

    // Chat Dialog
    if (uiState.isChatSheetOpen && uiState.chatBooking != null) {
        val messages = chatMessagesMap[uiState.chatBooking!!.id] ?: emptyList()
        ChatDialog(
            booking = uiState.chatBooking!!,
            currentRole = uiState.currentRole,
            messages = messages,
            inputText = uiState.chatInputText,
            onInputChange = { txt -> viewModel.onChatInputChanged(txt) },
            onSend = { viewModel.sendChatMessage() },
            onDismiss = { viewModel.closeChat() }
        )
    }

    // Review Dialog
    if (uiState.isReviewSheetOpen && uiState.reviewBooking != null) {
        ReviewDialog(
            booking = uiState.reviewBooking!!,
            rating = uiState.reviewRating,
            comment = uiState.reviewComment,
            onRatingChange = { r -> viewModel.onReviewRatingChanged(r) },
            onCommentChange = { c -> viewModel.onReviewCommentChanged(c) },
            onSubmit = { viewModel.submitReview() },
            onDismiss = { viewModel.closeReviewSheet() }
        )
    }

    // 24/7 Support Hotline Dialog
    if (uiState.isSupportModalOpen) {
        SupportDialog(onDismiss = { viewModel.closeSupportModal() })
    }

    // Notifications Center Dialog
    if (uiState.isNotificationsOpen) {
        NotificationsDialog(
            notifications = notifications,
            currentRole = uiState.currentRole,
            onDismiss = { viewModel.closeNotifications() }
        )
    }
}
