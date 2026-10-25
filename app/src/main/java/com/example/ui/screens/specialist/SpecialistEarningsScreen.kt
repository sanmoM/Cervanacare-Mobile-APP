package com.example.ui.screens.specialist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
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
import com.example.data.model.SpecialistEarningsSummary
import com.example.data.model.SpecialistTransaction
import com.example.data.model.SubscriptionPlan
import com.example.ui.theme.*

@Composable
fun SpecialistEarningsScreen(
    earningsSummary: SpecialistEarningsSummary,
    transactions: List<SpecialistTransaction>,
    subscriptionPlan: SubscriptionPlan,
    onRequestPayout: (Int) -> Unit,
    onRenewSubscription: () -> Unit
) {
    var showPayoutDialog by remember { mutableStateOf(false) }
    var payoutPhoneNumber by remember { mutableStateOf("+254 700 225 533") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Earnings & Commission Breakdown Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaTealPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Net Earnings (Available Payout)",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "KSh ${earningsSummary.pendingPayoutKsh}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Calculated after standard 15% healthcare commission deduction",
                        color = CervannaTealContainer,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showPayoutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("request_mpesa_payout_btn")
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Withdraw to Safaricom M-Pesa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Commission Transparency Statement
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(20.dp))
                        Text(
                            text = "15% Healthcare Commission Statement",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CervannaTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Gross Completed Client Engagements", fontSize = 12.sp, color = CervannaTextSecondary)
                        Text(text = "KSh ${earningsSummary.totalGrossKsh}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Platform Commission (15%)", fontSize = 12.sp, color = CervannaAmberSecondary)
                        Text(text = "-KSh ${earningsSummary.totalCommissionKsh}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaAmberSecondary)
                    }
                    Divider(color = CervannaBorder.copy(alpha = 0.6f), modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total Net Payout Generated", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTealDark)
                        Text(text = "KSh ${earningsSummary.netEarningsKsh}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = CervannaTealDark)
                    }
                }
            }
        }

        // Domestic Worker Subscription Plan Section (KSh 500 / month)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaAmberContainer),
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CervannaAmberSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(
                                    text = subscriptionPlan.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CervannaOnAmberContainer
                                )
                                Text(
                                    text = "KSh ${subscriptionPlan.priceKsh} / ${subscriptionPlan.billingCycle}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = CervannaOnAmberContainer
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MpesaGreen)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Next Renewal Date: ${subscriptionPlan.renewalDate}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CervannaOnAmberContainer
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    subscriptionPlan.benefits.forEach { b ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 1.dp)
                        ) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = CervannaAmberSecondary, modifier = Modifier.size(14.dp))
                            Text(text = b, fontSize = 11.sp, color = CervannaOnAmberContainer)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRenewSubscription,
                        colors = ButtonDefaults.buttonColors(containerColor = CervannaAmberSecondary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("renew_subscription_btn")
                    ) {
                        Text("Renew via M-Pesa (KSh ${subscriptionPlan.priceKsh})", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Transaction Ledger History
        item {
            Text(
                text = "Transaction History & Ledger",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = CervannaTextPrimary
            )
        }

        items(transactions) { tx ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tx.serviceName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CervannaTextPrimary
                        )
                        Text(
                            text = "+KSh ${tx.netKsh}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CervannaTealDark
                        )
                    }
                    Text(
                        text = "Booking #${tx.bookingId} • Client: ${tx.customerName} • ${tx.date}",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gross: KSh ${tx.grossKsh} | 15% Comm: -KSh ${tx.commissionKsh}",
                            fontSize = 10.sp,
                            color = CervannaTextTertiary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (tx.payoutStatus.contains("Paid")) MpesaGreenContainer else CervannaAmberContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tx.payoutStatus,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tx.payoutStatus.contains("Paid")) MpesaGreenDark else CervannaOnAmberContainer
                            )
                        }
                    }
                }
            }
        }
    }

    // Payout Dialog
    if (showPayoutDialog) {
        AlertDialog(
            onDismissRequest = { showPayoutDialog = false },
            title = { Text("M-Pesa Payout Request") },
            text = {
                Column {
                    Text("Enter Safaricom M-Pesa phone number to receive KSh ${earningsSummary.pendingPayoutKsh}:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = payoutPhoneNumber,
                        onValueChange = { payoutPhoneNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("M-Pesa Phone Number") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRequestPayout(earningsSummary.pendingPayoutKsh)
                        showPayoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen)
                ) {
                    Text("Send Payout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPayoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}
