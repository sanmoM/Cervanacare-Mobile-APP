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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CervannaTopAppBar(
    currentRole: UserRole,
    unreadNotificationCount: Int,
    onSwitchRole: (UserRole) -> Unit,
    onNotificationsClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CervannaSurface,
            titleContentColor = CervannaTextPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CervannaTealPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = "Cervanna Care Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Cervanna Care",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = CervannaTealDark
                        )
                        // Role Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (currentRole == UserRole.CUSTOMER)
                                        CervannaTealContainer
                                    else
                                        CervannaAmberContainer
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (currentRole == UserRole.CUSTOMER) "CUSTOMER" else "SPECIALIST",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentRole == UserRole.CUSTOMER)
                                    CervannaOnTealContainer
                                else
                                    CervannaOnAmberContainer
                            )
                        }
                    }
                    Text(
                        text = "Eastern Bypass, Kenya",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                }
            }
        },
        actions = {
            // Quick Role Switch Button
            AssistChip(
                onClick = {
                    val nextRole = if (currentRole == UserRole.CUSTOMER) UserRole.SPECIALIST else UserRole.CUSTOMER
                    onSwitchRole(nextRole)
                },
                label = {
                    Text(
                        text = if (currentRole == UserRole.CUSTOMER) "Go to Specialist" else "Go to Customer",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch Role",
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier
                    .testTag("switch_role_chip")
                    .padding(end = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = CervannaSurfaceVariant,
                    labelColor = CervannaTealPrimary,
                    leadingIconContentColor = CervannaTealPrimary
                )
            )

            // Notifications with badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(
                                containerColor = CervannaAmberSecondary,
                                contentColor = Color.White
                            ) {
                                Text("$unreadNotificationCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = CervannaTextPrimary
                    )
                }
            }

            // 24/7 Helpline
            IconButton(
                onClick = onSupportClick,
                modifier = Modifier.testTag("support_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.SupportAgent,
                    contentDescription = "Support Helpline",
                    tint = CervannaTealPrimary
                )
            }
        }
    )
}
