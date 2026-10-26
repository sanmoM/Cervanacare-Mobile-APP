package com.example.ui.screens.customer

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun CustomerAccountScreen(
    profile: CustomerProfile,
    onSwitchRole: (UserRole) -> Unit,
    onSupportClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(profile.notificationsEnabled) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(CervannaTealPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = profile.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = CervannaTextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CervannaTealContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CLIENT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CervannaOnTealContainer
                                )
                            }
                        }
                        Text(text = profile.phone, fontSize = 13.sp, color = CervannaTextSecondary)
                        Text(text = profile.email, fontSize = 12.sp, color = CervannaTextTertiary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(13.dp))
                            Text(text = profile.preferredLocation, fontSize = 11.sp, color = CervannaTextSecondary)
                        }
                    }
                }
            }
        }

        // Switch Role Card (Direct Role Toggle)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaAmberContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSwitchRole(UserRole.SPECIALIST) }
                    .testTag("switch_to_specialist_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CervannaAmberSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Switch to Specialist Role",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CervannaOnAmberContainer
                        )
                        Text(
                            text = "Access specialist dashboard, bookings, availability & 15% healthcare earnings",
                            fontSize = 11.sp,
                            color = CervannaOnAmberContainer
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = CervannaOnAmberContainer
                    )
                }
            }
        }

        // Preferences Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Preferences & Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "SMS & In-App Notifications", fontSize = 13.sp, color = CervannaTextPrimary)
                            Text(text = "Real-time updates on bookings & M-Pesa payments", fontSize = 11.sp, color = CervannaTextSecondary)
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = CervannaTealPrimary, checkedTrackColor = CervannaTealContainer)
                        )
                    }

                    Divider(color = CervannaBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))

                    AccountActionRow(icon = Icons.Outlined.Lock, title = "Security & Change Password")
                    AccountActionRow(icon = Icons.Outlined.Emergency, title = "Emergency Contact: ${profile.emergencyContact}")
                    AccountActionRow(icon = Icons.Outlined.LocationOn, title = "Saved Home Addresses (Nairobi Eastern Bypass)")
                }
            }
        }

        // Support & Legal
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Cervanna Care Kenya", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    AccountActionRow(
                        icon = Icons.Outlined.SupportAgent,
                        title = "24/7 Helpline (+254 700 225533)",
                        onClick = onSupportClick
                    )
                    AccountActionRow(
                        icon = Icons.Outlined.Notifications,
                        title = "In-App Notifications Center",
                        onClick = onNotificationsClick
                    )
                    AccountActionRow(
                        icon = Icons.Outlined.Policy,
                        title = "Vetting Standards & Regulatory Compliance"
                    )
                    AccountActionRow(
                        icon = Icons.Outlined.DeleteForever,
                        title = "Request Account Deletion",
                        textColor = Color(0xFFDC2626),
                        onClick = { showDeleteDialog = true }
                    )
                }
            }
        }

        // Logout
        item {
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("logout_button")
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Out")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Request Account Deletion") },
            text = { Text("Per Kenyan Data Protection regulations, you can permanently request deletion of your account and personal records. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirm Request", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out of Cervanna Care?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CervannaTealPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Log Out", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun AccountActionRow(
    icon: ImageVector,
    title: String,
    textColor: Color = CervannaTextPrimary,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(20.dp))
            Text(text = title, fontSize = 13.sp, color = textColor)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CervannaTextTertiary, modifier = Modifier.size(18.dp))
    }
}
