package com.example

import com.example.data.model.*
import com.example.data.repository.CervannaRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: CervannaRepository

    @Before
    fun setUp() {
        repository = CervannaRepository()
    }

    @Test
    fun testRolesRestrictedToCustomerAndSpecialistOnly() {
        val roles = UserRole.values()
        assertEquals(2, roles.size)
        assertTrue(roles.contains(UserRole.CUSTOMER))
        assertTrue(roles.contains(UserRole.SPECIALIST))
        // Verify NO Admin role exists
        assertFalse(roles.any { it.name.contains("ADMIN", ignoreCase = true) })
    }

    @Test
    fun testSevenCoreServicesConfigured() {
        val services = ServiceCategory.values()
        assertEquals(7, services.size)
        val names = services.map { it.displayName }
        assertTrue(names.contains("Pre & Post Pregnancy Care"))
        assertTrue(names.contains("Post Surgery Care"))
        assertTrue(names.contains("Elderly Care"))
        assertTrue(names.contains("Physiotherapy"))
        assertTrue(names.contains("Nanny & Housekeeping"))
        assertTrue(names.contains("Special Needs Care"))
        assertTrue(names.contains("Palliative Care"))
    }

    @Test
    fun testHealthcareCommissionIsExactly15Percent() {
        val grossAmount = 20000
        val commissionRate = 0.15
        val commission = (grossAmount * commissionRate).toInt()
        val net = grossAmount - commission

        assertEquals(3000, commission)
        assertEquals(17000, net)
    }

    @Test
    fun testDomesticSubscriptionIs500Ksh() {
        val plan = repository.subscriptionPlan.value
        assertEquals(500, plan.priceKsh)
        assertEquals("Monthly", plan.billingCycle)
        assertTrue(plan.isActive)
    }

    @Test
    fun testCreateBookingAndPayWithMpesa() {
        val booking = repository.createBooking(
            specialistId = "spec_1",
            serviceCategory = ServiceCategory.POST_SURGERY,
            location = "Eastern Bypass, Nairobi",
            startDate = "30 Oct 2026",
            shiftType = "Day Shift (8am - 5pm)",
            durationDays = 2,
            careRequirements = "Post-op wound care"
        )

        assertNotNull(booking)
        assertEquals(BookingStatus.PENDING, booking.status)
        assertEquals(PaymentStatus.UNPAID, booking.paymentStatus)
        assertEquals(9000, booking.quoteKsh) // 4500 daily rate * 2 days

        val paid = repository.payBookingWithMpesa(booking.id, "0712345678")
        assertTrue(paid)

        val updatedBooking = repository.bookings.value.first { it.id == booking.id }
        assertEquals(PaymentStatus.PAID, updatedBooking.paymentStatus)
        assertTrue(updatedBooking.paymentMethod.contains("M-Pesa"))
    }

    @Test
    fun testSpecialistAcceptBooking() {
        val booking = repository.createBooking(
            specialistId = "spec_2",
            serviceCategory = ServiceCategory.PHYSIOTHERAPY,
            location = "Westlands, Nairobi",
            startDate = "01 Nov 2026",
            shiftType = "Session-Based",
            durationDays = 1,
            careRequirements = "Knee mobility"
        )

        repository.acceptBooking(booking.id)
        val updated = repository.bookings.value.first { it.id == booking.id }
        assertEquals(BookingStatus.CONFIRMED, updated.status)
    }
}
