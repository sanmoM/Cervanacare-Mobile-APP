package com.example.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.Specialist
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun SpecialistProfileScreen(
    specialist: Specialist,
    onUpdateRates: (hourly: Int, daily: Int, monthly: Int) -> Unit,
    onSwitchRole: (UserRole) -> Unit,
    onSupportClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var showEditRatesDialog by remember { mutableStateOf(false) }
    var hourlyInput by remember(specialist) { mutableStateOf(specialist.hourlyRateKsh.toString()) }
    var dailyInput by remember(specialist) { mutableStateOf(specialist.dailyRateKsh.toString()) }
    var monthlyInput by remember(specialist) { mutableStateOf(specialist.monthlyRateKsh.toString()) }

    var showDocUploadAlert by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Summary Card
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
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(CervannaTealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = specialist.name.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = specialist.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = CervannaTextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Specialist",
                                    tint = CervannaTealPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(text = specialist.title, fontSize = 12.sp, color = CervannaTextSecondary)
                            Text(text = specialist.phoneNumber, fontSize = 12.sp, color = CervannaTextTertiary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Completeness Meter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Profile Completeness", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CervannaTextSecondary)
                        Text(text = "95% (Verified)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CervannaTealPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.95f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = CervannaTealPrimary,
                        trackColor = CervannaSurfaceVariant
                    )
                }
            }
        }

        // Switch Role Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaTealContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSwitchRole(UserRole.CUSTOMER) }
                    .testTag("switch_to_customer_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CervannaTealPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Switch to Customer Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CervannaOnTealContainer
                        )
                        Text(
                            text = "Browse marketplace, book care specialists, and manage customer bookings",
                            fontSize = 11.sp,
                            color = CervannaOnTealContainer
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CervannaOnTealContainer)
                }
            }
        }

        // Service Rates Card (Editable)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Service Rates (KSh)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                        TextButton(onClick = { showEditRatesDialog = true }) {
                            Text("Edit Rates", color = CervannaTealPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RateChipItem("Hourly Session", "KSh ${specialist.hourlyRateKsh}")
                        RateChipItem("Day Shift (Daily)", "KSh ${specialist.dailyRateKsh}")
                        RateChipItem("Monthly Retainer", "KSh ${specialist.monthlyRateKsh}")
                    }
                }
            }
        }

        // Verification & Documents (Complies with strictly server/admin verification)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Official Documents & Vetting", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                        TextButton(onClick = { showDocUploadAlert = true }) {
                            Text("+ Upload New", color = CervannaTealPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    DocumentItem(name = "Certificate of Good Conduct (DCI Kenya)", status = "Verified", statusColor = CervannaTealPrimary)
                    DocumentItem(name = "Nursing Council of Kenya (NCK) License #48192", status = "Verified", statusColor = CervannaTealPrimary)
                    DocumentItem(name = "National Identification Card (Kenya)", status = "Verified", statusColor = CervannaTealPrimary)
                    DocumentItem(name = "BLS / CPR First Aid Certification", status = "Verified", statusColor = CervannaTealPrimary)
                }
            }
        }

        // Bio & Services
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Professional Biography", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = specialist.bio, fontSize = 12.sp, lineHeight = 18.sp, color = CervannaTextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Locations: ${specialist.locations.joinToString(", ")}", fontSize = 12.sp, color = CervannaTextSecondary)
                    Text(text = "Languages: ${specialist.languages.joinToString(", ")}", fontSize = 12.sp, color = CervannaTextSecondary)
                }
            }
        }

        // Support Hub
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Specialist Help & Support", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onSupportClick() }.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = CervannaTealPrimary)
                        Text("24/7 Specialist Coordination Hotline (+254 700 225533)", fontSize = 12.sp, color = CervannaTealDark)
                    }
                }
            }
        }

        // Log Out Button
        item {
            OutlinedButton(
                onClick = onLogout,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("specialist_logout_btn")
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Out")
            }
        }
    }

    // Edit Rates Dialog
    if (showEditRatesDialog) {
        AlertDialog(
            onDismissRequest = { showEditRatesDialog = false },
            title = { Text("Update Service Rates (KSh)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = hourlyInput,
                        onValueChange = { hourlyInput = it },
                        label = { Text("Hourly Session Rate") },
                        placeholder = { Text("e.g. 500", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        )
                    )
                    OutlinedTextField(
                        value = dailyInput,
                        onValueChange = { dailyInput = it },
                        label = { Text("Daily Shift Rate") },
                        placeholder = { Text("e.g. 3500", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        )
                    )
                    OutlinedTextField(
                        value = monthlyInput,
                        onValueChange = { monthlyInput = it },
                        label = { Text("Monthly Rate") },
                        placeholder = { Text("e.g. 60000", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val h = hourlyInput.toIntOrNull() ?: specialist.hourlyRateKsh
                        val d = dailyInput.toIntOrNull() ?: specialist.dailyRateKsh
                        val m = monthlyInput.toIntOrNull() ?: specialist.monthlyRateKsh
                        onUpdateRates(h, d, m)
                        showEditRatesDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CervannaTealPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save Rates", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditRatesDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Document Upload Alert
    if (showDocUploadAlert) {
        AlertDialog(
            onDismissRequest = { showDocUploadAlert = false },
            title = { Text("Upload Credentials for Verification") },
            text = { Text("You can upload updated regulatory licenses, CPR certifications, or police clearance. Documents will be securely reviewed by Cervanna Care compliance administration.") },
            confirmButton = {
                Button(
                    onClick = { showDocUploadAlert = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CervannaTealPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Choose Document File", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDocUploadAlert = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun RateChipItem(title: String, price: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 10.sp, color = CervannaTextSecondary)
            Text(text = price, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTealDark)
        }
    }
}

@Composable
private fun DocumentItem(name: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(16.dp))
            Text(text = name, fontSize = 11.sp, color = CervannaTextPrimary)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusColor.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = statusColor)
        }
    }
}
