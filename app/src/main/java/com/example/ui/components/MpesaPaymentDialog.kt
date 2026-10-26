package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun MpesaPaymentDialog(
    bookingId: String?,
    amountKsh: Int,
    phoneNumber: String,
    isProcessing: Boolean,
    receiptRef: String?,
    onPhoneChange: (String) -> Unit,
    onSubmitPay: () -> Unit,
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with M-Pesa Green Brand
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
                                .background(MpesaGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "M",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Column {
                            Text(
                                text = "Lipa na M-Pesa",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MpesaGreenDark
                            )
                            Text(
                                text = "Cervanna Care Paybill 890200",
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

                Spacer(modifier = Modifier.height(18.dp))

                if (receiptRef != null) {
                    // Success Receipt State
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MpesaGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = MpesaGreen,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Payment Received!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CervannaTextPrimary
                    )

                    Text(
                        text = "Your booking payment has been verified by Safaricom M-Pesa.",
                        fontSize = 13.sp,
                        color = CervannaTextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Amount Paid", fontSize = 12.sp, color = CervannaTextSecondary)
                                Text("KSh $amountKsh", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MpesaGreenDark)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("M-Pesa Receipt", fontSize = 12.sp, color = CervannaTextSecondary)
                                Text(receiptRef, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTealPrimary)
                            }
                            if (bookingId != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Booking ID", fontSize = 12.sp, color = CervannaTextSecondary)
                                    Text("#$bookingId", fontSize = 13.sp, color = CervannaTextPrimary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mpesa_done_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CervannaTealPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    // Payment input & trigger state
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CervannaTealContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total Booking Amount",
                                fontSize = 12.sp,
                                color = CervannaOnTealContainer
                            )
                            Text(
                                text = "KSh $amountKsh",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CervannaTealDark
                            )
                            if (bookingId != null) {
                                Text(
                                    text = "Booking Reference: #$bookingId",
                                    fontSize = 11.sp,
                                    color = CervannaTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Enter Safaricom M-Pesa Phone Number",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = CervannaTextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = onPhoneChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mpesa_phone_input"),
                        placeholder = { Text("e.g. 0712 345 678", color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = MpesaGreen
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text(
                        text = "An STK Push prompt will be sent to your phone. Enter your 4-digit M-Pesa PIN to complete payment.",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary,
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onSubmitPay,
                        enabled = !isProcessing && phoneNumber.length >= 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("mpesa_submit_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MpesaGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Prompting M-Pesa STK...", color = Color.White)
                        } else {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Send M-Pesa Prompt",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
