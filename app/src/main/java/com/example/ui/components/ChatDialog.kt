package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.Booking
import com.example.data.model.ChatMessage
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun ChatDialog(
    booking: Booking,
    currentRole: UserRole,
    messages: List<ChatMessage>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CervannaSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.82f)
                .padding(vertical = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Chat Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CervannaSurfaceVariant)
                        .padding(16.dp),
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
                                .background(CervannaTealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (currentRole == UserRole.CUSTOMER)
                                    booking.specialistName.take(1)
                                else
                                    booking.customerName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Column {
                            Text(
                                text = if (currentRole == UserRole.CUSTOMER)
                                    booking.specialistName
                                else
                                    booking.customerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CervannaTextPrimary
                            )
                            Text(
                                text = "${booking.serviceName} • #${booking.id}",
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
                            contentDescription = "Close Chat",
                            tint = CervannaTextTertiary
                        )
                    }
                }

                // Chat Messages List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (messages.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Send a message to coordinate care instructions, schedules, or special requirements.",
                                    fontSize = 12.sp,
                                    color = CervannaTextTertiary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    items(messages) { msg ->
                        val isMe = msg.senderRole == currentRole
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                        ) {
                            Text(
                                text = msg.senderName,
                                fontSize = 10.sp,
                                color = CervannaTextSecondary,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 14.dp,
                                            topEnd = 14.dp,
                                            bottomStart = if (isMe) 14.dp else 2.dp,
                                            bottomEnd = if (isMe) 2.dp else 14.dp
                                        )
                                    )
                                    .background(
                                        if (isMe) CervannaTealPrimary else CervannaSurfaceVariant
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    color = if (isMe) Color.White else CervannaTextPrimary,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = msg.timestamp,
                                fontSize = 9.sp,
                                color = CervannaTextTertiary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        placeholder = { Text("Write a message...", fontSize = 13.sp, color = CervannaPlaceholder) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = CervannaPlaceholder,
                            unfocusedPlaceholderColor = CervannaPlaceholder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = false,
                        maxLines = 3
                    )

                    IconButton(
                        onClick = onSend,
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (inputText.isNotBlank()) CervannaTealPrimary else CervannaSurfaceVariant
                            )
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Send Message",
                            tint = if (inputText.isNotBlank()) Color.White else CervannaTextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
