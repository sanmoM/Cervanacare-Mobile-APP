package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun SupportDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CervannaSurface,
            tonalElevation = 6.dp,
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 420.dp)
                .wrapContentHeight()
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
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
                                fontSize = 11.5.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CervannaSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CervannaTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contact Channel 1: Phone / WhatsApp Dispatch
                SupportContactCard(
                    icon = Icons.Default.Phone,
                    iconBgColor = CervannaTealContainer,
                    iconTint = CervannaTealPrimary,
                    title = "Call / WhatsApp Dispatch",
                    subtitle = "+254 700 225533",
                    badge = "24/7 Available",
                    badgeColor = Color(0xFF15803D),
                    badgeBgColor = Color(0xFFDCFCE7),
                    extraInfo = "Fast direct response for urgent care & dispatch"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Contact Channel 2: Customer Support Email
                SupportContactCard(
                    icon = Icons.Default.Email,
                    iconBgColor = CervannaAmberContainer,
                    iconTint = CervannaAmberSecondary,
                    title = "Customer Support Email",
                    subtitle = "customersupport@cervannacare.com",
                    badge = "Replies in 15m",
                    badgeColor = Color(0xFFB45309),
                    badgeBgColor = Color(0xFFFEF3C7),
                    extraInfo = "Billing, bookings, special requests & feedback"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Contact Channel 3: Physical Hub
                SupportContactCard(
                    icon = Icons.Default.LocationOn,
                    iconBgColor = CervannaTealContainer,
                    iconTint = CervannaTealPrimary,
                    title = "Physical Coordination Hub",
                    subtitle = "Eastern Bypass, Kamakis, Nairobi",
                    badge = "Hub Office",
                    badgeColor = CervannaTealPrimary,
                    badgeBgColor = CervannaTealContainer,
                    extraInfo = "Greenspot Towers • Mon - Sat 8:00 AM – 7:00 PM"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Guarantee Banner
                Card(
                    colors = CardDefaults.cardColors(containerColor = CervannaTealContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CervannaTealPrimary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = CervannaTealPrimary,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(top = 2.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "100% Vetted & Verified Guarantee",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CervannaTealPrimary
                            )
                            Text(
                                text = "All specialists are verified with DCI Good Conduct certificates & healthcare regulatory licenses.",
                                fontSize = 11.sp,
                                color = CervannaTextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("support_close_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CervannaTealPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Got It, Close",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SupportContactCard(
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    badgeBgColor: Color,
    extraInfo: String? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant.copy(alpha = 0.65f)),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CervannaBorder.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CervannaTextSecondary,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = badgeBgColor
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Text(
                    text = subtitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CervannaTextPrimary
                )

                if (extraInfo != null) {
                    Text(
                        text = extraInfo,
                        fontSize = 10.5.sp,
                        color = CervannaTextSecondary
                    )
                }
            }
        }
    }
}
