package com.example.data.model

enum class UserRole {
    CUSTOMER,
    SPECIALIST
}

enum class ServiceCategory(val displayName: String, val iconDescription: String) {
    PRE_POST_PREGNANCY("Pre & Post Pregnancy Care", "Maternal and newborn care"),
    POST_SURGERY("Post Surgery Care", "Wound management and recovery"),
    ELDERLY_CARE("Elderly Care", "Companionship, hygiene, mobility"),
    PHYSIOTHERAPY("Physiotherapy", "In-home physical therapy & rehab"),
    NANNY_HOUSEKEEPING("Nanny & Housekeeping", "Vetted nannies & house managers"),
    SPECIAL_NEEDS("Special Needs Care", "Dedicated adaptive daily support"),
    PALLIATIVE_CARE("Palliative Care", "Compassionate comfort & dignity")
}

enum class ProfessionalCategory(val displayName: String) {
    HOUSE_MANAGER_NANNY("House Manager / Nanny"),
    NURSE("Registered Nurse (RN)"),
    PHYSIOTHERAPIST("Physiotherapist"),
    NURSE_AIDE("Nurse Aide or Assistant"),
    SPECIAL_NEEDS_CAREGIVER("Special Needs Caregiver"),
    HOME_HEALTH_ASSISTANT("Home Health Assistant")
}

enum class VerificationStatus {
    VERIFIED,
    UNDER_REVIEW,
    ADDITIONAL_INFO_REQUIRED
}

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}

enum class PaymentStatus {
    UNPAID,
    PAID,
    REFUNDED
}

data class Specialist(
    val id: String,
    val name: String,
    val title: String,
    val professionalCategory: ProfessionalCategory,
    val serviceCategories: List<ServiceCategory>,
    val bio: String,
    val experienceYears: Int,
    val education: String,
    val qualifications: List<String>,
    val skills: List<String>,
    val languages: List<String>,
    val locations: List<String>,
    val hourlyRateKsh: Int,
    val dailyRateKsh: Int,
    val monthlyRateKsh: Int,
    val verificationStatus: VerificationStatus,
    val verificationBadges: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val agencyAffiliation: String? = null,
    val availableDays: List<String>,
    val shiftTypes: List<String>,
    val phoneNumber: String,
    val email: String,
    val isAvailable: Boolean = true,
    val isFavorite: Boolean = false
)

data class Booking(
    val id: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val specialistId: String,
    val specialistName: String,
    val specialistTitle: String,
    val serviceName: String,
    val serviceCategory: ServiceCategory,
    val location: String,
    val startDate: String,
    val shiftType: String,
    val durationDays: Int,
    val careRequirements: String,
    val quoteKsh: Int,
    val status: BookingStatus,
    val paymentStatus: PaymentStatus,
    val paymentMethod: String = "M-Pesa",
    val createdAt: String,
    val cancellationReason: String? = null
)

data class Review(
    val id: String,
    val specialistId: String,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val serviceName: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val roleTarget: UserRole
)

data class SpecialistTransaction(
    val id: String,
    val date: String,
    val bookingId: String,
    val customerName: String,
    val serviceName: String,
    val grossKsh: Int,
    val commissionRatePct: Double = 15.0,
    val commissionKsh: Int,
    val netKsh: Int,
    val payoutStatus: String, // "Paid to M-Pesa", "Pending Payout"
    val mpesaReference: String
)

data class SpecialistEarningsSummary(
    val totalGrossKsh: Int,
    val commissionRatePct: Double = 15.0,
    val totalCommissionKsh: Int,
    val netEarningsKsh: Int,
    val pendingPayoutKsh: Int,
    val completedEngagements: Int
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceKsh: Int,
    val billingCycle: String,
    val description: String,
    val benefits: List<String>,
    val isActive: Boolean,
    val renewalDate: String
)

data class ChatMessage(
    val id: String,
    val bookingId: String,
    val senderRole: UserRole,
    val senderName: String,
    val text: String,
    val timestamp: String
)

data class CustomerProfile(
    val name: String,
    val email: String,
    val phone: String,
    val preferredLocation: String,
    val emergencyContact: String,
    val notificationsEnabled: Boolean = true
)
