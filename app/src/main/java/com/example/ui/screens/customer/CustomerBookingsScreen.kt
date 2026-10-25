package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerBookingsScreen(
    bookings: List<Booking>,
    onPayMpesa: (Booking) -> Unit,
    onChatClick: (Booking) -> Unit,
    onReviewClick: (Booking) -> Unit,
    onCancelBooking: (bookingId: String, reason: String) -> Unit,
    onRescheduleBooking: (bookingId: String, newDate: String) -> Unit,
    onSupportClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Pending", "Confirmed", "Completed", "Cancelled")

    var bookingToCancel by remember { mutableStateOf<Booking?>(null) }
    var cancelReason by remember { mutableStateOf("") }
    var bookingToReschedule by remember { mutableStateOf<Booking?>(null) }
    var newRescheduleDate by remember { mutableStateOf("02 Nov 2026") }

    val filteredList = when (selectedFilter) {
        "Pending" -> bookings.filter { it.status == BookingStatus.PENDING }
        "Confirmed" -> bookings.filter { it.status == BookingStatus.CONFIRMED }
        "Completed" -> bookings.filter { it.status == BookingStatus.COMPLETED }
        "Cancelled" -> bookings.filter { it.status == BookingStatus.CANCELLED }
        else -> bookings
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground)
    ) {
        // Tab Row
        Surface(color = CervannaSurface, tonalElevation = 2.dp) {
            ScrollableTabRow(
                selectedTabIndex = filterTabs.indexOf(selectedFilter),
                edgePadding = 16.dp,
                containerColor = CervannaSurface,
                contentColor = CervannaTealPrimary
            ) {
                filterTabs.forEach { tab ->
                    Tab(
                        selected = selectedFilter == tab,
                        onClick = { selectedFilter = tab },
                        text = {
                            Text(
                                text = tab,
                                fontWeight = if (selectedFilter == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("tab_booking_$tab")
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.EventBusy,
                        contentDescription = null,
                        tint = CervannaTextTertiary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No $selectedFilter Bookings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Your booking requests and care schedules will appear here.",
                        fontSize = 12.sp,
                        color = CervannaTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredList) { booking ->
                    CustomerBookingCard(
                        booking = booking,
                        onPayClick = { onPayMpesa(booking) },
                        onChatClick = { onChatClick(booking) },
                        onReviewClick = { onReviewClick(booking) },
                        onRequestCancel = { bookingToCancel = booking },
                        onRequestReschedule = { bookingToReschedule = booking }
                    )
                }
            }
        }
    }

    // Cancel Dialog
    if (bookingToCancel != null) {
        AlertDialog(
            onDismissRequest = { bookingToCancel = null },
            title = { Text("Cancel Booking #${bookingToCancel?.id}") },
            text = {
                Column {
                    Text("Are you sure you want to cancel this booking? Please provide a reason:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = cancelReason,
                        onValueChange = { cancelReason = it },
                        placeholder = { Text("Reason for cancellation...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        bookingToCancel?.let { onCancelBooking(it.id, cancelReason.ifBlank { "Client requested cancellation" }) }
                        bookingToCancel = null
                        cancelReason = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CervannaTextTertiary)
                ) {
                    Text("Confirm Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingToCancel = null }) { Text("Keep Booking") }
            }
        )
    }

    // Reschedule Dialog
    if (bookingToReschedule != null) {
        AlertDialog(
            onDismissRequest = { bookingToReschedule = null },
            title = { Text("Reschedule Booking #${bookingToReschedule?.id}") },
            text = {
                Column {
                    Text("Enter preferred new start date:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newRescheduleDate,
                        onValueChange = { newRescheduleDate = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        bookingToReschedule?.let { onRescheduleBooking(it.id, newRescheduleDate) }
                        bookingToReschedule = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CervannaTealPrimary)
                ) {
                    Text("Save Reschedule")
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingToReschedule = null }) { Text("Dismiss") }
            }
        )
    }
}

@Composable
fun CustomerBookingCard(
    booking: Booking,
    onPayClick: () -> Unit,
    onChatClick: () -> Unit,
    onReviewClick: () -> Unit,
    onRequestCancel: () -> Unit,
    onRequestReschedule: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("booking_card_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with ID and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "#${booking.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CervannaTealDark
                    )
                    Text(
                        text = "• ${booking.createdAt}",
                        fontSize = 11.sp,
                        color = CervannaTextTertiary
                    )
                }

                val (badgeBg, badgeFg) = when (booking.status) {
                    BookingStatus.CONFIRMED -> CervannaTealContainer to CervannaOnTealContainer
                    BookingStatus.PENDING -> CervannaAmberContainer to CervannaOnAmberContainer
                    BookingStatus.COMPLETED -> Color(0xFFDBEAFE) to Color(0xFF1E40AF)
                    BookingStatus.CANCELLED -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = booking.status.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = booking.serviceName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = CervannaTextPrimary
            )
            Text(
                text = "Specialist: ${booking.specialistName} (${booking.specialistTitle})",
                fontSize = 13.sp,
                color = CervannaTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Details Box
            Card(
                colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(14.dp))
                        Text(text = booking.location, fontSize = 11.sp, color = CervannaTextSecondary)
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(14.dp))
                        Text(text = "${booking.startDate} • ${booking.shiftType} (${booking.durationDays} days)", fontSize = 11.sp, color = CervannaTextSecondary)
                    }
                }
            }

            if (!booking.careRequirements.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Care Notes: ${booking.careRequirements}",
                    fontSize = 11.sp,
                    color = CervannaTextSecondary,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Payment line
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Quote: KSh ${booking.quoteKsh}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CervannaTextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (booking.paymentStatus == PaymentStatus.PAID) MpesaGreenContainer else CervannaAmberContainer
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (booking.paymentStatus == PaymentStatus.PAID) "LIPA NA M-PESA: PAID" else "UNPAID",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (booking.paymentStatus == PaymentStatus.PAID) MpesaGreenDark else CervannaOnAmberContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = CervannaBorder.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onChatClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat", fontSize = 11.sp)
                }

                if (booking.paymentStatus == PaymentStatus.UNPAID && booking.status != BookingStatus.CANCELLED) {
                    Button(
                        onClick = onPayClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text("Pay M-Pesa", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (booking.status == BookingStatus.COMPLETED) {
                    Button(
                        onClick = onReviewClick,
                        colors = ButtonDefaults.buttonColors(containerColor = CervannaAmberSecondary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Review", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (booking.status != BookingStatus.CANCELLED) {
                    OutlinedButton(
                        onClick = onRequestReschedule,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text("Reschedule", fontSize = 11.sp)
                    }

                    TextButton(
                        onClick = onRequestCancel,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Cancel", fontSize = 11.sp, color = Color(0xFFDC2626))
                    }
                }
            }
        }
    }
}
