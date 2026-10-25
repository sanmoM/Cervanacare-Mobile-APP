package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class CervannaRepository {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    private val dateOnlyFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Current Customer Profile
    private val _customerProfile = MutableStateFlow(
        CustomerProfile(
            name = "Sarah Kamau",
            email = "sarah.kamau@cervannacare.ke",
            phone = "+254 712 345 678",
            preferredLocation = "Nairobi - Eastern Bypass",
            emergencyContact = "+254 722 000 111",
            notificationsEnabled = true
        )
    )
    val customerProfile: StateFlow<CustomerProfile> = _customerProfile.asStateFlow()

    // Active Specialist Profile (when logged in as Specialist)
    private val _currentSpecialistId = MutableStateFlow("spec_1") // Faith Muthoni
    val currentSpecialistId: StateFlow<String> = _currentSpecialistId.asStateFlow()

    // Specialists
    private val _specialists = MutableStateFlow<List<Specialist>>(initialSpecialists())
    val specialists: StateFlow<List<Specialist>> = _specialists.asStateFlow()

    // Bookings
    private val _bookings = MutableStateFlow<List<Booking>>(initialBookings())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    // Reviews
    private val _reviews = MutableStateFlow<List<Review>>(initialReviews())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(initialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Specialist Transactions & Earnings
    private val _transactions = MutableStateFlow<List<SpecialistTransaction>>(initialTransactions())
    val transactions: StateFlow<List<SpecialistTransaction>> = _transactions.asStateFlow()

    // Subscription Plans
    private val _subscriptionPlan = MutableStateFlow(
        SubscriptionPlan(
            id = "sub_individual",
            name = "Individual Specialist Plan",
            priceKsh = 500,
            billingCycle = "Monthly",
            description = "Domestic worker and individual care specialist verified directory listing",
            benefits = listOf(
                "Verified Cervanna Care badge on profile",
                "Direct customer booking enquiries",
                "Zero commission on domestic household services",
                "SMS & In-app instant job alerts",
                "24/7 dedicated specialist phone support"
            ),
            isActive = true,
            renewalDate = "28 Nov 2026"
        )
    )
    val subscriptionPlan: StateFlow<SubscriptionPlan> = _subscriptionPlan.asStateFlow()

    // Chat messages map (bookingId -> messages)
    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(initialChats())
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    // ================= CUSTOMER ACTIONS =================

    fun toggleFavorite(specialistId: String) {
        _specialists.value = _specialists.value.map {
            if (it.id == specialistId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun createBooking(
        specialistId: String,
        serviceCategory: ServiceCategory,
        location: String,
        startDate: String,
        shiftType: String,
        durationDays: Int,
        careRequirements: String
    ): Booking {
        val specialist = _specialists.value.first { it.id == specialistId }
        val baseRate = when {
            shiftType.contains("Live-in", ignoreCase = true) -> specialist.dailyRateKsh * 1.3
            shiftType.contains("Night", ignoreCase = true) -> specialist.dailyRateKsh * 1.15
            else -> specialist.dailyRateKsh.toDouble()
        }
        val quoteKsh = (baseRate * durationDays.coerceAtLeast(1)).toInt()

        val newBooking = Booking(
            id = "BK-" + (1000 + _bookings.value.size + 1),
            customerId = "cust_sarah",
            customerName = _customerProfile.value.name,
            customerPhone = _customerProfile.value.phone,
            specialistId = specialist.id,
            specialistName = specialist.name,
            specialistTitle = specialist.title,
            serviceName = serviceCategory.displayName,
            serviceCategory = serviceCategory,
            location = location,
            startDate = startDate,
            shiftType = shiftType,
            durationDays = durationDays,
            careRequirements = careRequirements,
            quoteKsh = quoteKsh,
            status = BookingStatus.PENDING,
            paymentStatus = PaymentStatus.UNPAID,
            createdAt = dateOnlyFormat.format(Date())
        )

        _bookings.value = listOf(newBooking) + _bookings.value

        // Notify specialist
        addNotification(
            title = "New Booking Request Received",
            message = "New request from ${_customerProfile.value.name} for ${serviceCategory.displayName} (${shiftType}) starting on $startDate.",
            roleTarget = UserRole.SPECIALIST
        )

        return newBooking
    }

    fun payBookingWithMpesa(bookingId: String, mpesaPhone: String): Boolean {
        var success = false
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                success = true
                it.copy(
                    paymentStatus = PaymentStatus.PAID,
                    paymentMethod = "M-Pesa ($mpesaPhone)"
                )
            } else it
        }

        if (success) {
            val booking = _bookings.value.first { it.id == bookingId }
            addNotification(
                title = "M-Pesa Payment Received",
                message = "Payment of KSh ${booking.quoteKsh} confirmed for Booking #${booking.id}.",
                roleTarget = UserRole.CUSTOMER
            )
            addNotification(
                title = "Client Paid via M-Pesa",
                message = "${booking.customerName} completed payment of KSh ${booking.quoteKsh} for Booking #${booking.id}.",
                roleTarget = UserRole.SPECIALIST
            )

            // Add transaction for specialist with exact 15% healthcare commission
            val gross = booking.quoteKsh
            val commission = (gross * 0.15).toInt()
            val net = gross - commission
            val tx = SpecialistTransaction(
                id = "TX-${System.currentTimeMillis() % 100000}",
                date = dateOnlyFormat.format(Date()),
                bookingId = booking.id,
                customerName = booking.customerName,
                serviceName = booking.serviceName,
                grossKsh = gross,
                commissionRatePct = 15.0,
                commissionKsh = commission,
                netKsh = net,
                payoutStatus = "Pending Payout",
                mpesaReference = "QKT${(100000..999999).random()}KE"
            )
            _transactions.value = listOf(tx) + _transactions.value
        }
        return success
    }

    fun cancelBooking(bookingId: String, reason: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                it.copy(
                    status = BookingStatus.CANCELLED,
                    cancellationReason = reason
                )
            } else it
        }
        addNotification(
            title = "Booking Cancelled",
            message = "Booking #$bookingId was cancelled. Reason: $reason",
            roleTarget = UserRole.SPECIALIST
        )
    }

    fun rescheduleBooking(bookingId: String, newDate: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                it.copy(startDate = newDate)
            } else it
        }
        addNotification(
            title = "Booking Rescheduled",
            message = "Booking #$bookingId date updated to $newDate.",
            roleTarget = UserRole.SPECIALIST
        )
    }

    fun submitReview(specialistId: String, rating: Int, comment: String, serviceName: String) {
        val review = Review(
            id = "REV-${System.currentTimeMillis() % 10000}",
            specialistId = specialistId,
            customerName = _customerProfile.value.name,
            rating = rating,
            comment = comment,
            date = dateOnlyFormat.format(Date()),
            serviceName = serviceName
        )
        _reviews.value = listOf(review) + _reviews.value

        // Recompute specialist rating
        val specReviews = _reviews.value.filter { it.specialistId == specialistId }
        val avg = specReviews.map { it.rating }.average()
        _specialists.value = _specialists.value.map {
            if (it.id == specialistId) {
                it.copy(
                    rating = String.format(Locale.US, "%.1f", avg).toDouble(),
                    reviewCount = specReviews.size
                )
            } else it
        }
    }

    // ================= SPECIALIST ACTIONS =================

    fun acceptBooking(bookingId: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                it.copy(status = BookingStatus.CONFIRMED)
            } else it
        }
        addNotification(
            title = "Booking Confirmed by Specialist!",
            message = "Your specialist accepted booking #$bookingId. Care arrangements are confirmed.",
            roleTarget = UserRole.CUSTOMER
        )
    }

    fun declineBooking(bookingId: String, reason: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                it.copy(
                    status = BookingStatus.CANCELLED,
                    cancellationReason = "Specialist unavailable: $reason"
                )
            } else it
        }
        addNotification(
            title = "Booking Update",
            message = "Specialist could not accept booking #$bookingId. Reason: $reason",
            roleTarget = UserRole.CUSTOMER
        )
    }

    fun completeBooking(bookingId: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                it.copy(status = BookingStatus.COMPLETED)
            } else it
        }
        addNotification(
            title = "Engagement Completed",
            message = "Booking #$bookingId marked completed. Thank you for using Cervanna Care!",
            roleTarget = UserRole.CUSTOMER
        )
    }

    fun toggleSpecialistAvailability(specialistId: String) {
        _specialists.value = _specialists.value.map {
            if (it.id == specialistId) it.copy(isAvailable = !it.isAvailable) else it
        }
    }

    fun updateSpecialistAvailabilityDays(specialistId: String, days: List<String>) {
        _specialists.value = _specialists.value.map {
            if (it.id == specialistId) it.copy(availableDays = days) else it
        }
    }

    fun updateSpecialistRates(specialistId: String, hourly: Int, daily: Int, monthly: Int) {
        _specialists.value = _specialists.value.map {
            if (it.id == specialistId) {
                it.copy(hourlyRateKsh = hourly, dailyRateKsh = daily, monthlyRateKsh = monthly)
            } else it
        }
    }

    fun requestMpesaPayout(amountKsh: Int, mpesaPhone: String): String {
        val ref = "PY-" + (100000..999999).random()
        _transactions.value = _transactions.value.map {
            if (it.payoutStatus == "Pending Payout") it.copy(payoutStatus = "Paid to M-Pesa ($mpesaPhone)")
            else it
        }
        addNotification(
            title = "M-Pesa Payout Processed",
            message = "Payout of KSh $amountKsh sent to Safaricom M-Pesa $mpesaPhone (Ref: $ref).",
            roleTarget = UserRole.SPECIALIST
        )
        return ref
    }

    fun renewSubscriptionWithMpesa(phone: String): Boolean {
        _subscriptionPlan.value = _subscriptionPlan.value.copy(
            isActive = true,
            renewalDate = "28 Dec 2026"
        )
        addNotification(
            title = "Subscription Renewed",
            message = "KSh 500 monthly domestic worker subscription renewed successfully via M-Pesa ($phone).",
            roleTarget = UserRole.SPECIALIST
        )
        return true
    }

    // ================= COMMUNICATION =================

    fun sendChatMessage(bookingId: String, senderRole: UserRole, senderName: String, text: String) {
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            bookingId = bookingId,
            senderRole = senderRole,
            senderName = senderName,
            text = text,
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        )
        val currentList = _chatMessages.value[bookingId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (bookingId to (currentList + msg))
    }

    fun addNotification(title: String, message: String, roleTarget: UserRole) {
        val item = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = title,
            message = message,
            timestamp = "Just now",
            isRead = false,
            roleTarget = roleTarget
        )
        _notifications.value = listOf(item) + _notifications.value
    }

    // ================= SEED DATA =================

    private fun initialSpecialists(): List<Specialist> = listOf(
        Specialist(
            id = "spec_1",
            name = "Faith Muthoni, RN",
            title = "Senior Registered Nurse & Maternal Health Specialist",
            professionalCategory = ProfessionalCategory.NURSE,
            serviceCategories = listOf(
                ServiceCategory.PRE_POST_PREGNANCY,
                ServiceCategory.POST_SURGERY,
                ServiceCategory.PALLIATIVE_CARE
            ),
            bio = "Compassionate Registered Nurse with over 8 years of clinical experience at Kenyatta National Hospital and home-based clinical care. Specializing in prenatal monitoring, postpartum healing, newborn support, and sterile post-operative wound dressings.",
            experienceYears = 8,
            education = "B.Sc Nursing - University of Nairobi",
            qualifications = listOf(
                "Nursing Council of Kenya (NCK) Licensed #48192",
                "Advanced Neonatal Resuscitation Certified",
                "Certificate of Good Conduct (DCI Verified)",
                "Wound Ostomy Care Specialist"
            ),
            skills = listOf("Newborn Care", "Wound Care", "IV Infusion", "Postpartum Vitals", "Medication Admin", "Catheter Care"),
            languages = listOf("English", "Kiswahili", "Kikuyu"),
            locations = listOf("Nairobi - Eastern Bypass", "Westlands", "Kilimani", "Karen", "Ruiru"),
            hourlyRateKsh = 800,
            dailyRateKsh = 4500,
            monthlyRateKsh = 85000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("NCK Licensed", "Background Checked", "Cervanna Verified", "5-Star Nurse"),
            rating = 4.9,
            reviewCount = 38,
            agencyAffiliation = "Cervanna Care Premier Network",
            availableDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
            shiftTypes = listOf("Day Shift (8am - 5pm)", "Night Shift (7pm - 7am)", "Hourly Care"),
            phoneNumber = "+254 700 225 533",
            email = "faith.muthoni@cervannacare.com",
            isAvailable = true,
            isFavorite = true
        ),
        Specialist(
            id = "spec_2",
            name = "David Ochieng, PT",
            title = "Licensed Orthopedic & Neuro Physiotherapist",
            professionalCategory = ProfessionalCategory.PHYSIOTHERAPIST,
            serviceCategories = listOf(
                ServiceCategory.PHYSIOTHERAPY,
                ServiceCategory.POST_SURGERY,
                ServiceCategory.ELDERLY_CARE
            ),
            bio = "Certified home physiotherapist dedicated to restoring physical independence, mobility, and pain relief for stroke patients, orthopedic post-op recoveries, and geriatric balance rehabilitation.",
            experienceYears = 6,
            education = "B.Sc Physiotherapy - JKUAT",
            qualifications = listOf(
                "Kenya Society of Physiotherapists (KSP) Member #8821",
                "Certified Stroke Rehabilitation Therapist",
                "Certificate of Good Conduct (DCI Verified)"
            ),
            skills = listOf("Mobility Training", "Post-Op Rehab", "Gait & Balance", "Joint Mobilization", "Stroke Recovery", "Pain Relief"),
            languages = listOf("English", "Kiswahili", "Dholuo"),
            locations = listOf("Nairobi - Eastern Bypass", "Upper Hill", "Parklands", "Mombasa Road", "Syokimau"),
            hourlyRateKsh = 1200,
            dailyRateKsh = 5000,
            monthlyRateKsh = 95000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("KSP Licensed", "DCI Cleared", "Top Rated 2026"),
            rating = 4.9,
            reviewCount = 27,
            availableDays = listOf("Mon", "Wed", "Fri", "Sat"),
            shiftTypes = listOf("Session-Based (1-2 Hours)", "Day Shift (8am - 4pm)"),
            phoneNumber = "+254 711 987 654",
            email = "david.ochieng@cervannacare.com",
            isAvailable = true,
            isFavorite = false
        ),
        Specialist(
            id = "spec_3",
            name = "Grace Wambui",
            title = "Certified House Manager & Early Childhood Nanny",
            professionalCategory = ProfessionalCategory.HOUSE_MANAGER_NANNY,
            serviceCategories = listOf(
                ServiceCategory.NANNY_HOUSEKEEPING,
                ServiceCategory.PRE_POST_PREGNANCY
            ),
            bio = "Professional home manager and loving nanny with 7 years of high-standard household coordination in Nairobi. Experienced with toddler development, dietary meal planning, sanitary housekeeping, and full family support.",
            experienceYears = 7,
            education = "Diploma in Hospitality & Early Childhood Care - Kenya Utalii College",
            qualifications = listOf(
                "Certified Domestic Professional (Kenya Ministry of Labour)",
                "Red Cross Pediatric First Aid & Infant CPR",
                "Police Clearance Certificate of Good Conduct"
            ),
            skills = listOf("Infant Care", "Meal Prep & Cooking", "Deep Cleaning", "Laundry Care", "Household Budgeting", "Play & Milestones"),
            languages = listOf("English", "Kiswahili"),
            locations = listOf("Nairobi - Eastern Bypass", "Thika Road", "Runda", "Kitisuru", "Lavington"),
            hourlyRateKsh = 400,
            dailyRateKsh = 2200,
            monthlyRateKsh = 35000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("Utalii Alum", "First Aid Certified", "DCI Cleared"),
            rating = 4.8,
            reviewCount = 42,
            availableDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
            shiftTypes = listOf("Day Shift (7:30am - 5:30pm)", "24/7 Live-in Support"),
            phoneNumber = "+254 722 334 455",
            email = "grace.wambui@cervannacare.com",
            isAvailable = true,
            isFavorite = true
        ),
        Specialist(
            id = "spec_4",
            name = "Sharon Jepkosgei",
            title = "Special Needs Caregiver & Pediatric Aide",
            professionalCategory = ProfessionalCategory.SPECIAL_NEEDS_CAREGIVER,
            serviceCategories = listOf(
                ServiceCategory.SPECIAL_NEEDS,
                ServiceCategory.ELDERLY_CARE
            ),
            bio = "Patient and highly trained caregiver specializing in support for children and adults with Autism Spectrum Disorder, Cerebral Palsy, and cognitive needs. Dedicated to respectful sensory-friendly care.",
            experienceYears = 5,
            education = "Diploma in Special Education - Kenya Institute of Special Education (KISE)",
            qualifications = listOf(
                "KISE Special Education Care Certified",
                "Kenyan Sign Language (KSL) Proficient",
                "Certificate of Good Conduct (DCI Verified)"
            ),
            skills = listOf("Autism Support", "Cerebral Palsy Aid", "Sensory Therapy", "Patience & Empathy", "Behavior Support", "ADL Assistance"),
            languages = listOf("English", "Kiswahili", "Kalenjin", "Sign Language"),
            locations = listOf("Nairobi - Eastern Bypass", "Gigiri", "Karen", "South B", "Eldoret"),
            hourlyRateKsh = 650,
            dailyRateKsh = 3500,
            monthlyRateKsh = 60000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("KISE Certified", "Sign Language", "Vetted Specialist"),
            rating = 5.0,
            reviewCount = 19,
            availableDays = listOf("Mon", "Tue", "Wed", "Thu", "Sat", "Sun"),
            shiftTypes = listOf("Day Shift (8am - 5pm)", "Live-in Care", "Night Care"),
            phoneNumber = "+254 733 556 778",
            email = "sharon.jep@cervannacare.com",
            isAvailable = true,
            isFavorite = false
        ),
        Specialist(
            id = "spec_5",
            name = "Beatrice Chebet",
            title = "Geriatric Nurse Aide & Senior Companion",
            professionalCategory = ProfessionalCategory.NURSE_AIDE,
            serviceCategories = listOf(
                ServiceCategory.ELDERLY_CARE,
                ServiceCategory.PALLIATIVE_CARE
            ),
            bio = "Gentle, respectful nurse assistant providing elderly parents with daily hygiene, medication reminders, companionship, balanced nutrition, and gentle walking exercise in the comfort of home.",
            experienceYears = 9,
            education = "Nurse Aide Certificate - St. John Ambulance Kenya",
            qualifications = listOf(
                "St. John Ambulance Certified Caregiver",
                "Dementia & Alzheimer's Care Protocol",
                "Certificate of Good Conduct"
            ),
            skills = listOf("Bed Bath & Grooming", "Vital Signs Check", "Medication Prompts", "Fall Prevention", "Nutrition Prep", "Companionship"),
            languages = listOf("English", "Kiswahili"),
            locations = listOf("Nairobi - Eastern Bypass", "Langata", "South C", "Nakuru", "Mombasa"),
            hourlyRateKsh = 500,
            dailyRateKsh = 2800,
            monthlyRateKsh = 48000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("St. John Certified", "Geriatric Care", "DCI Cleared"),
            rating = 4.9,
            reviewCount = 31,
            availableDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            shiftTypes = listOf("Day Shift (8am - 6pm)", "24/7 Live-in", "Night Shift"),
            phoneNumber = "+254 720 112 233",
            email = "beatrice.chebet@cervannacare.com",
            isAvailable = true,
            isFavorite = true
        ),
        Specialist(
            id = "spec_6",
            name = "Kevin Mwangi",
            title = "Home Health Assistant & Emergency Responder",
            professionalCategory = ProfessionalCategory.HOME_HEALTH_ASSISTANT,
            serviceCategories = listOf(
                ServiceCategory.POST_SURGERY,
                ServiceCategory.ELDERLY_CARE,
                ServiceCategory.PHYSIOTHERAPY
            ),
            bio = "Energetic home health assistant helping recovering patients transfer safely from bed to wheelchair, assist with prescribed home exercises, and accompany to medical checkups across Nairobi.",
            experienceYears = 4,
            education = "Emergency Medical Technician (EMT) - Kenya Red Cross Training Institute",
            qualifications = listOf(
                "Red Cross Certified EMT",
                "Patient Transfer & Ergonomics Specialist",
                "Certificate of Good Conduct"
            ),
            skills = listOf("Patient Transfers", "Exercise Escort", "Mobility Equipment", "Vitals Log", "Hospital Escort"),
            languages = listOf("English", "Kiswahili"),
            locations = listOf("Nairobi - Eastern Bypass", "Westlands", "Ruiru", "Juja", "Kisumu"),
            hourlyRateKsh = 600,
            dailyRateKsh = 3200,
            monthlyRateKsh = 55000,
            verificationStatus = VerificationStatus.VERIFIED,
            verificationBadges = listOf("Red Cross EMT", "DCI Cleared"),
            rating = 4.7,
            reviewCount = 15,
            availableDays = listOf("Mon", "Tue", "Thu", "Fri", "Sat"),
            shiftTypes = listOf("Day Shift (8am - 5pm)", "Hourly Care"),
            phoneNumber = "+254 715 443 322",
            email = "kevin.mwangi@cervannacare.com",
            isAvailable = true,
            isFavorite = false
        )
    )

    private fun initialBookings(): List<Booking> = listOf(
        Booking(
            id = "BK-1048",
            customerId = "cust_sarah",
            customerName = "Sarah Kamau",
            customerPhone = "+254 712 345 678",
            specialistId = "spec_1",
            specialistName = "Faith Muthoni, RN",
            specialistTitle = "Senior Registered Nurse",
            serviceName = "Post Surgery Care",
            serviceCategory = ServiceCategory.POST_SURGERY,
            location = "Eastern Bypass, Kamakis, Nairobi",
            startDate = "26 Oct 2026",
            shiftType = "Day Shift (8am - 5pm)",
            durationDays = 5,
            careRequirements = "Post-cesarean recovery care, wound dressing change, vitals checking and newborn bath guidance.",
            quoteKsh = 22500,
            status = BookingStatus.CONFIRMED,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = "M-Pesa (0712345678)",
            createdAt = "24 Oct 2026"
        ),
        Booking(
            id = "BK-1049",
            customerId = "cust_sarah",
            customerName = "Sarah Kamau",
            customerPhone = "+254 712 345 678",
            specialistId = "spec_2",
            specialistName = "David Ochieng, PT",
            specialistTitle = "Licensed Physiotherapist",
            serviceName = "Physiotherapy",
            serviceCategory = ServiceCategory.PHYSIOTHERAPY,
            location = "Eastern Bypass, Ruiru, Nairobi",
            startDate = "28 Oct 2026",
            shiftType = "Session-Based (1-2 Hours)",
            durationDays = 3,
            careRequirements = "Knee mobility and walking rehab for elderly parent recovering from replacement surgery.",
            quoteKsh = 15000,
            status = BookingStatus.PENDING,
            paymentStatus = PaymentStatus.UNPAID,
            paymentMethod = "M-Pesa",
            createdAt = "24 Oct 2026"
        ),
        Booking(
            id = "BK-1035",
            customerId = "cust_sarah",
            customerName = "Sarah Kamau",
            customerPhone = "+254 712 345 678",
            specialistId = "spec_5",
            specialistName = "Beatrice Chebet",
            specialistTitle = "Geriatric Nurse Aide",
            serviceName = "Elderly Care",
            serviceCategory = ServiceCategory.ELDERLY_CARE,
            location = "Eastern Bypass, Utawala, Nairobi",
            startDate = "10 Oct 2026",
            shiftType = "Day Shift (8am - 6pm)",
            durationDays = 7,
            careRequirements = "Companionship, gentle walking support, and medication schedules for grandmother.",
            quoteKsh = 19600,
            status = BookingStatus.COMPLETED,
            paymentStatus = PaymentStatus.PAID,
            paymentMethod = "M-Pesa",
            createdAt = "08 Oct 2026"
        )
    )

    private fun initialReviews(): List<Review> = listOf(
        Review(
            id = "rev_1",
            specialistId = "spec_1",
            customerName = "Grace Njeri",
            rating = 5,
            comment = "Nurse Faith was an absolute blessing! Her knowledge of postpartum healing and gentle care for my baby gave our family total peace of mind.",
            date = "18 Oct 2026",
            serviceName = "Pre & Post Pregnancy Care"
        ),
        Review(
            id = "rev_2",
            specialistId = "spec_1",
            customerName = "Joseph Mutua",
            rating = 5,
            comment = "Handled my mother's post-op surgical wound with immaculate clinical hygiene. Highly recommend Cervanna Care nurses.",
            date = "12 Oct 2026",
            serviceName = "Post Surgery Care"
        ),
        Review(
            id = "rev_3",
            specialistId = "spec_2",
            customerName = "Esther Achieng",
            rating = 5,
            comment = "David is exceptionally patient and encouraging. My father made huge strides in his balance and walking within two weeks.",
            date = "14 Oct 2026",
            serviceName = "Physiotherapy"
        ),
        Review(
            id = "rev_4",
            specialistId = "spec_3",
            customerName = "Mary Kilonzo",
            rating = 5,
            comment = "Grace managed our household with impeccable integrity and showed endless warmth to our twin boys. Truly vetted and trustworthy.",
            date = "05 Oct 2026",
            serviceName = "Nanny & Housekeeping"
        )
    )

    private fun initialNotifications(): List<NotificationItem> = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Booking Confirmed",
            message = "Nurse Faith Muthoni confirmed Booking #BK-1048 for Post Surgery Care.",
            timestamp = "2 hours ago",
            isRead = false,
            roleTarget = UserRole.CUSTOMER
        ),
        NotificationItem(
            id = "notif_2",
            title = "M-Pesa Receipt Received",
            message = "Payment of KSh 22,500 for BK-1048 received via M-Pesa.",
            timestamp = "2 hours ago",
            isRead = true,
            roleTarget = UserRole.CUSTOMER
        ),
        NotificationItem(
            id = "notif_3",
            title = "New Booking Enquiries (Specialist)",
            message = "You have 1 pending booking request waiting for review.",
            timestamp = "3 hours ago",
            isRead = false,
            roleTarget = UserRole.SPECIALIST
        )
    )

    private fun initialTransactions(): List<SpecialistTransaction> = listOf(
        SpecialistTransaction(
            id = "TX-9921",
            date = "24 Oct 2026",
            bookingId = "BK-1048",
            customerName = "Sarah Kamau",
            serviceName = "Post Surgery Care (5 Days)",
            grossKsh = 22500,
            commissionRatePct = 15.0,
            commissionKsh = 3375,
            netKsh = 19125,
            payoutStatus = "Pending Payout",
            mpesaReference = "QKT839201KE"
        ),
        SpecialistTransaction(
            id = "TX-9872",
            date = "17 Oct 2026",
            bookingId = "BK-1035",
            customerName = "Grace Njeri",
            serviceName = "Pre & Post Pregnancy Care",
            grossKsh = 18000,
            commissionRatePct = 15.0,
            commissionKsh = 2700,
            netKsh = 15300,
            payoutStatus = "Paid to M-Pesa",
            mpesaReference = "QJS472910KE"
        ),
        SpecialistTransaction(
            id = "TX-9801",
            date = "10 Oct 2026",
            bookingId = "BK-1022",
            customerName = "Peter Maina",
            serviceName = "Post Surgery Care",
            grossKsh = 13500,
            commissionRatePct = 15.0,
            commissionKsh = 2025,
            netKsh = 11475,
            payoutStatus = "Paid to M-Pesa",
            mpesaReference = "QJR102938KE"
        )
    )

    private fun initialChats(): Map<String, List<ChatMessage>> = mapOf(
        "BK-1048" to listOf(
            ChatMessage(
                id = "c1",
                bookingId = "BK-1048",
                senderRole = UserRole.CUSTOMER,
                senderName = "Sarah Kamau",
                text = "Hello Nurse Faith, we look forward to having you on Monday morning. Please let us know if you need any specific medical supplies prepared.",
                timestamp = "09:30"
            ),
            ChatMessage(
                id = "c2",
                bookingId = "BK-1048",
                senderRole = UserRole.SPECIALIST,
                senderName = "Faith Muthoni, RN",
                text = "Habari Sarah! I bring my own sterile dressing kit, BP monitor, and thermometer. Please have clean towels and boiled warm water ready. See you at 8:00 AM!",
                timestamp = "09:42"
            )
        )
    )
}
