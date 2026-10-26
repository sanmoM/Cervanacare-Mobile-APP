package com.example.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun SpecialistDashboardScreen(
    specialist: Specialist,
    bookings: List<Booking>,
    earningsSummary: SpecialistEarningsSummary,
    onAcceptBooking: (String) -> Unit,
    onDeclineBooking: (String, String) -> Unit,
    onToggleAvailability: () -> Unit,
    onChatClick: (Booking) -> Unit,
    onRequestPayout: (Int) -> Unit
) {
    var declineTargetBookingId by remember { mutableStateOf<String?>(null) }
    var declineReason by remember { mutableStateOf("") }

    val pendingRequests = bookings.filter { it.status == BookingStatus.PENDING }
    val upcomingEngagements = bookings.filter { it.status == BookingStatus.CONFIRMED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Specialist Status & Availability Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(CervannaTealPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = specialist.name.take(2).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = specialist.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = CervannaTextPrimary
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = CervannaTealPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = specialist.title,
                                    fontSize = 12.sp,
                                    color = CervannaTextSecondary
                                )
                                Text(
                                    text = "NCK Licensed • DCI Verified",
                                    fontSize = 11.sp,
                                    color = CervannaTealPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Availability Toggle Switch
                        Column(horizontalAlignment = Alignment.End) {
                            Switch(
                                checked = specialist.isAvailable,
                                onCheckedChange = { onToggleAvailability() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CervannaTealPrimary,
                                    checkedTrackColor = CervannaTealContainer
                                ),
                                modifier = Modifier.testTag("specialist_availability_toggle")
                            )
                            Text(
                                text = if (specialist.isAvailable) "Available" else "Off Duty",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (specialist.isAvailable) CervannaTealPrimary else CervannaTextTertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = CervannaBorder.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Verification Badge Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CervannaTealContainer.copy(alpha = 0.5f))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = CervannaTealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Profile Status: Verified by Cervanna Care Kenya Administration",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CervannaOnTealContainer
                        )
                    }
                }
            }
        }

        // Metrics Grid (4 items)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Pending Enquiries",
                        value = "${pendingRequests.size}",
                        subtitle = "Awaiting reply",
                        icon = Icons.Default.Inbox,
                        color = CervannaAmberSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Active Bookings",
                        value = "${upcomingEngagements.size}",
                        subtitle = "Confirmed shifts",
                        icon = Icons.Default.EventAvailable,
                        color = CervannaTealPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Net Payout Due",
                        value = "KSh ${earningsSummary.pendingPayoutKsh}",
                        subtitle = "After 15% commission",
                        icon = Icons.Default.Payments,
                        color = MpesaGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Client Rating",
                        value = "${specialist.rating} ★",
                        subtitle = "${specialist.reviewCount} Reviews",
                        icon = Icons.Default.Star,
                        color = CervannaAmberSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Pending Requests Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Booking Enquiries (${pendingRequests.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CervannaTextPrimary
                )
            }
        }

        if (pendingRequests.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No pending booking requests right now. You are set to receive new leads.",
                            fontSize = 12.sp,
                            color = CervannaTextSecondary
                        )
                    }
                }
            }
        } else {
            items(pendingRequests) { booking ->
                SpecialistBookingActionCard(
                    booking = booking,
                    onAccept = { onAcceptBooking(booking.id) },
                    onDecline = { declineTargetBookingId = booking.id },
                    onChat = { onChatClick(booking) }
                )
            }
        }

        // Upcoming Confirmed Appointments
        item {
            Text(
                text = "Upcoming Scheduled Care (${upcomingEngagements.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = CervannaTextPrimary
            )
        }

        if (upcomingEngagements.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No confirmed upcoming engagements.",
                            fontSize = 12.sp,
                            color = CervannaTextSecondary
                        )
                    }
                }
            }
        } else {
            items(upcomingEngagements) { booking ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${booking.id} • ${booking.serviceName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CervannaTealDark
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CervannaTealContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CONFIRMED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CervannaOnTealContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Client: ${booking.customerName} (${booking.customerPhone})", fontSize = 12.sp, color = CervannaTextPrimary)
                        Text(text = "Schedule: ${booking.startDate} • ${booking.shiftType}", fontSize = 12.sp, color = CervannaTextSecondary)
                        Text(text = "Location: ${booking.location}", fontSize = 11.sp, color = CervannaTextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Quote: KSh ${booking.quoteKsh} (Paid via M-Pesa)", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MpesaGreenDark)
                            OutlinedButton(
                                onClick = { onChatClick(booking) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chat Client", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Decline Reason Dialog
    if (declineTargetBookingId != null) {
        AlertDialog(
            onDismissRequest = { declineTargetBookingId = null },
            title = { Text("Decline Booking Request") },
            text = {
                Column {
                    Text("Please specify why you are unable to take this booking:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = declineReason,
                        onValueChange = { declineReason = it },
                        placeholder = { Text("e.g. Schedule clash with prior engagement...", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        declineTargetBookingId?.let { onDeclineBooking(it, declineReason.ifBlank { "Schedule conflict" }) }
                        declineTargetBookingId = null
                        declineReason = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    )
                ) {
                    Text("Decline Request", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { declineTargetBookingId = null }) { Text("Back") }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 11.sp, color = CervannaTextSecondary)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CervannaTextPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = CervannaTextTertiary)
        }
    }
}

@Composable
fun SpecialistBookingActionCard(
    booking: Booking,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("spec_booking_request_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Request #${booking.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = CervannaTealDark
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CervannaAmberContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("NEW ENQUIRY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CervannaOnAmberContainer)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${booking.serviceName} • ${booking.durationDays} Days",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = CervannaTextPrimary
            )
            Text(text = "Client: ${booking.customerName} (${booking.customerPhone})", fontSize = 12.sp, color = CervannaTextSecondary)
            Text(text = "Location: ${booking.location}", fontSize = 12.sp, color = CervannaTextSecondary)
            Text(text = "Starts: ${booking.startDate} • ${booking.shiftType}", fontSize = 12.sp, color = CervannaTextSecondary)

            if (!booking.careRequirements.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Requirements: ${booking.careRequirements}",
                    fontSize = 11.sp,
                    color = CervannaTextPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CervannaSurfaceVariant)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Gross Quote & 15% commission breakdown
            val gross = booking.quoteKsh
            val commission = (gross * 0.15).toInt()
            val net = gross - commission

            Card(
                colors = CardDefaults.cardColors(containerColor = CervannaTealContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Gross: KSh $gross", fontSize = 11.sp, color = CervannaTextSecondary)
                        Text(text = "15% Platform Commission: -KSh $commission", fontSize = 10.sp, color = CervannaAmberSecondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Your Net Earnings", fontSize = 10.sp, color = CervannaOnTealContainer)
                        Text(text = "KSh $net", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CervannaTealDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accept / Decline / Message Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onChat,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat Client", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onDecline,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text("Decline", fontSize = 11.sp, color = Color(0xFFDC2626))
                }

                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CervannaTealPrimary,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("accept_booking_btn_${booking.id}")
                ) {
                    Text("Accept Booking", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
