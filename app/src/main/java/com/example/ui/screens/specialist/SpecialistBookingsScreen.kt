package com.example.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun SpecialistBookingsScreen(
    bookings: List<Booking>,
    onAcceptBooking: (String) -> Unit,
    onDeclineBooking: (String, String) -> Unit,
    onCompleteBooking: (String) -> Unit,
    onChatClick: (Booking) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Pending", "Confirmed", "Completed")

    val filteredList = when (selectedFilter) {
        "Pending" -> bookings.filter { it.status == BookingStatus.PENDING }
        "Confirmed" -> bookings.filter { it.status == BookingStatus.CONFIRMED }
        "Completed" -> bookings.filter { it.status == BookingStatus.COMPLETED }
        else -> bookings
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground)
    ) {
        Surface(color = CervannaSurface, tonalElevation = 2.dp) {
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(selectedFilter),
                edgePadding = 16.dp,
                containerColor = CervannaSurface,
                contentColor = CervannaTealPrimary
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedFilter == tab,
                        onClick = { selectedFilter = tab },
                        text = {
                            Text(
                                text = tab,
                                fontWeight = if (selectedFilter == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("spec_tab_$tab")
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
                        imageVector = Icons.Default.EventNote,
                        contentDescription = null,
                        tint = CervannaTextTertiary,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No $selectedFilter Bookings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CervannaTextPrimary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { booking ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("spec_booking_item_${booking.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${booking.id} • ${booking.serviceName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = CervannaTealDark
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when (booking.status) {
                                                BookingStatus.CONFIRMED -> CervannaTealContainer
                                                BookingStatus.PENDING -> CervannaAmberContainer
                                                BookingStatus.COMPLETED -> Color(0xFFDBEAFE)
                                                BookingStatus.CANCELLED -> Color(0xFFFEE2E2)
                                            }
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = booking.status.name,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (booking.status) {
                                            BookingStatus.CONFIRMED -> CervannaOnTealContainer
                                            BookingStatus.PENDING -> CervannaOnAmberContainer
                                            BookingStatus.COMPLETED -> Color(0xFF1E40AF)
                                            BookingStatus.CANCELLED -> Color(0xFF991B1B)
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Client: ${booking.customerName} (${booking.customerPhone})", fontSize = 13.sp, color = CervannaTextPrimary)
                            Text(text = "Schedule: ${booking.startDate} • ${booking.shiftType} (${booking.durationDays} Days)", fontSize = 12.sp, color = CervannaTextSecondary)
                            Text(text = "Location: ${booking.location}", fontSize = 12.sp, color = CervannaTextSecondary)

                            if (!booking.careRequirements.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Notes: ${booking.careRequirements}", fontSize = 11.sp, color = CervannaTextSecondary)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = CervannaBorder.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val net = (booking.quoteKsh * 0.85).toInt()
                                Column {
                                    Text(text = "Your Net: KSh $net", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTealDark)
                                    Text(text = "(15% commission deducted)", fontSize = 10.sp, color = CervannaTextTertiary)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(
                                        onClick = { onChatClick(booking) },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Chat", fontSize = 11.sp)
                                    }

                                    if (booking.status == BookingStatus.PENDING) {
                                        Button(
                                            onClick = { onAcceptBooking(booking.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = CervannaTealPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text("Accept", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else if (booking.status == BookingStatus.CONFIRMED) {
                                        Button(
                                            onClick = { onCompleteBooking(booking.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text("Mark Done", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
