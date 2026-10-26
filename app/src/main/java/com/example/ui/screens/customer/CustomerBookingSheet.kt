package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.data.model.Specialist
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerBookingSheet(
    specialist: Specialist,
    selectedService: ServiceCategory,
    selectedShift: String,
    location: String,
    startDate: String,
    durationDays: Int,
    requirements: String,
    currentStep: Int,
    onServiceChange: (ServiceCategory) -> Unit,
    onShiftChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onDurationChange: (Int) -> Unit,
    onRequirementsChange: (String) -> Unit,
    onStepChange: (Int) -> Unit,
    onSubmitBooking: (andOpenMpesa: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Book ${specialist.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Step $currentStep of 4: " + when (currentStep) {
                            1 -> "Service & Shift"
                            2 -> "Location & Schedule"
                            3 -> "Care Requirements"
                            else -> "Review & M-Pesa Quote"
                        },
                        fontSize = 12.sp,
                        color = CervannaTealPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = CervannaTextTertiary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step Progress Bar
            LinearProgressIndicator(
                progress = { currentStep / 4f },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = CervannaTealPrimary,
                trackColor = CervannaSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            when (currentStep) {
                1 -> {
                    // STEP 1: Service & Shift
                    Text(
                        text = "Select Specific Care Service",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    specialist.serviceCategories.forEach { sc ->
                        val isSel = selectedService == sc
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) CervannaTealContainer else CervannaSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onServiceChange(sc) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = isSel,
                                    onClick = { onServiceChange(sc) }
                                )
                                Column {
                                    Text(text = sc.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTextPrimary)
                                    Text(text = sc.iconDescription, fontSize = 11.sp, color = CervannaTextSecondary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Select Preferred Shift",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    specialist.shiftTypes.forEach { shift ->
                        val isSel = selectedShift == shift
                        FilterChip(
                            selected = isSel,
                            onClick = { onShiftChange(shift) },
                            label = {
                                Text(
                                    text = shift,
                                    fontSize = 11.sp,
                                    color = if (isSel) Color.White else CervannaTealPrimary,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                labelColor = CervannaTealPrimary,
                                iconColor = CervannaTealPrimary,
                                selectedContainerColor = CervannaTealPrimary,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = CervannaTealPrimary.copy(alpha = 0.5f),
                                selectedBorderColor = CervannaTealPrimary
                            ),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }

                2 -> {
                    // STEP 2: Location & Schedule
                    Text(
                        text = "Care Location in Kenya",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = location,
                        onValueChange = onLocationChange,
                        placeholder = { Text("e.g. Kilimani, Nairobi", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("booking_location_input"),
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = CervannaTealPrimary) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Preferred Start Date",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = onStartDateChange,
                        placeholder = { Text("e.g. 28 Oct 2026", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("booking_start_date_input"),
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = CervannaTealPrimary) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Duration: $durationDays Day(s)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Slider(
                        value = durationDays.toFloat(),
                        onValueChange = { onDurationChange(it.toInt().coerceAtLeast(1)) },
                        valueRange = 1f..30f,
                        steps = 28,
                        colors = SliderDefaults.colors(thumbColor = CervannaTealPrimary, activeTrackColor = CervannaTealPrimary)
                    )
                }

                3 -> {
                    // STEP 3: Care Requirements
                    Text(
                        text = "Care Requirements & Patient Details",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Describe medical history, special dietary needs, or routine expectations.",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = requirements,
                        onValueChange = onRequirementsChange,
                        placeholder = { Text("e.g. Post-operative wound dressing, assistance with mobility, medication reminders at 8 AM and 6 PM...", fontSize = 12.sp, color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        minLines = 4,
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth().testTag("booking_requirements_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                4 -> {
                    // STEP 4: Quotation Breakdown & M-Pesa
                    val baseDaily = specialist.dailyRateKsh
                    val shiftMultiplier = if (selectedShift.contains("Live-in", true)) 1.3 else 1.0
                    val totalQuote = (baseDaily * shiftMultiplier * durationDays).toInt()

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CervannaTealContainer),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Quotation Summary",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CervannaTealDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Service", fontSize = 12.sp, color = CervannaTextPrimary)
                                Text(selectedService.displayName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Specialist", fontSize = 12.sp, color = CervannaTextPrimary)
                                Text(specialist.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Shift & Duration", fontSize = 12.sp, color = CervannaTextPrimary)
                                Text("$durationDays Day(s) • $selectedShift", fontSize = 12.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Location", fontSize = 12.sp, color = CervannaTextPrimary)
                                Text(location, fontSize = 12.sp)
                            }
                            Divider(color = CervannaBorder, modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Estimated Quote", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTealDark)
                                Text("KSh $totalQuote", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = CervannaTealDark)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MpesaGreen)
                            Column {
                                Text("Payment via Safaricom Lipa na M-Pesa", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CervannaTextPrimary)
                                Text("Instant STK Push will be triggered upon submission", fontSize = 11.sp, color = CervannaTextSecondary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { onStepChange(currentStep - 1) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (currentStep < 4) {
                    Button(
                        onClick = { onStepChange(currentStep + 1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CervannaTealPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("booking_next_step_btn")
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { onSubmitBooking(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MpesaGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_booking_mpesa_btn")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pay via M-Pesa & Confirm", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
