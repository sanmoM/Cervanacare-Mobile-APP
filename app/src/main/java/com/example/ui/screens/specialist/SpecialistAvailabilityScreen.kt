package com.example.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
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
import com.example.data.model.Specialist
import com.example.ui.theme.*

@Composable
fun SpecialistAvailabilityScreen(
    specialist: Specialist,
    onSaveWorkingDays: (List<String>) -> Unit
) {
    val allDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var selectedDays by remember(specialist) { mutableStateOf(specialist.availableDays.toSet()) }
    var dayShiftSelected by remember { mutableStateOf(true) }
    var nightShiftSelected by remember { mutableStateOf(true) }
    var liveInSelected by remember { mutableStateOf(false) }
    var hourlySelected by remember { mutableStateOf(true) }
    var savedAlert by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CervannaTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = CervannaTealPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Weekly Availability Schedule",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CervannaTextPrimary
                            )
                            Text(
                                text = "Choose the days and shifts you accept client appointments",
                                fontSize = 12.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Available Days of the Week
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Active Working Days",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Clients in Kenya can only book your care services on selected days.",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        allDays.forEach { day ->
                            val isSelected = selectedDays.contains(day)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) CervannaTealPrimary else CervannaSurfaceVariant
                                    )
                                    .clickable {
                                        selectedDays = if (isSelected) {
                                            selectedDays - day
                                        } else {
                                            selectedDays + day
                                        }
                                        savedAlert = false
                                    }
                                    .testTag("day_toggle_$day"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else CervannaTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Shift Preferences
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Offered Shift Formats",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CervannaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ShiftCheckboxRow(
                        title = "Day Shift (8:00 AM - 5:00 PM)",
                        subtitle = "Standard daytime clinical or companion care",
                        checked = dayShiftSelected,
                        onCheckedChange = { dayShiftSelected = it }
                    )
                    ShiftCheckboxRow(
                        title = "Night Shift (7:00 PM - 7:00 AM)",
                        subtitle = "Overnight monitoring, vitals, and infant support",
                        checked = nightShiftSelected,
                        onCheckedChange = { nightShiftSelected = it }
                    )
                    ShiftCheckboxRow(
                        title = "24/7 Live-In Care",
                        subtitle = "Full-time residential home manager / nurse aide support",
                        checked = liveInSelected,
                        onCheckedChange = { liveInSelected = it }
                    )
                    ShiftCheckboxRow(
                        title = "Hourly Physical Rehab & Nursing Visits",
                        subtitle = "1 to 2 hour focused physiotherapy or wound care sessions",
                        checked = hourlySelected,
                        onCheckedChange = { hourlySelected = it }
                    )
                }
            }
        }

        // Calendar Clash Prevention Notice
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaTealContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EventBusy,
                        contentDescription = null,
                        tint = CervannaTealPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "Automatic Overlap Prevention",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = CervannaTealDark
                        )
                        Text(
                            text = "Confirmed bookings automatically block conflicting dates to prevent double booking.",
                            fontSize = 11.sp,
                            color = CervannaOnTealContainer
                        )
                    }
                }
            }
        }

        // Save Button
        item {
            if (savedAlert) {
                Text(
                    text = "✓ Availability schedule updated and synced successfully!",
                    color = CervannaTealPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Button(
                onClick = {
                    onSaveWorkingDays(selectedDays.toList())
                    savedAlert = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_availability_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CervannaTealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Availability Schedule", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun ShiftCheckboxRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = CervannaTextPrimary)
            Text(text = subtitle, fontSize = 11.sp, color = CervannaTextSecondary)
        }
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = CervannaTealPrimary)
        )
    }
}
