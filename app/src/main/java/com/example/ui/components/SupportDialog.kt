package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun SupportDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CervannaSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = CervannaTealPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Cervanna Care Support",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CervannaTextPrimary
                            )
                            Text(
                                text = "24/7 Home Care Help & Inquiries",
                                fontSize = 11.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = CervannaTextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contact channels
                SupportContactItem(
                    icon = Icons.Default.Phone,
                    title = "Call / WhatsApp Dispatch",
                    subtitle = "+254 700 225533",
                    badge = "24/7 Available",
                    badgeColor = CervannaTealPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                SupportContactItem(
                    icon = Icons.Default.Email,
                    title = "Customer Support Email",
                    subtitle = "customersupport@cervannacare.com",
                    badge = "Replies in 15m",
                    badgeColor = CervannaAmberSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                SupportContactItem(
                    icon = Icons.Default.LocationOn,
                    title = "Physical Care Coordination Hub",
                    subtitle = "Eastern Bypass, Kamakis, Nairobi, Kenya",
                    badge = "Hub Office",
                    badgeColor = CervannaTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Guarantee Banner
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
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = CervannaTealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "100% Vetted & Verified Guarantee",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = CervannaTextPrimary
                            )
                            Text(
                                text = "All specialists are verified with DCI Good Conduct certificates & healthcare regulatory licenses.",
                                fontSize = 11.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_close_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CervannaTealPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SupportContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CervannaTealPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }
                Text(
                    text = subtitle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CervannaTextPrimary
                )
            }
        }
    }
}
