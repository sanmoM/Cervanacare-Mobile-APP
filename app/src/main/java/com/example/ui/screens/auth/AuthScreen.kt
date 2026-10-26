package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onLoginSuccess: (role: UserRole, email: String, name: String) -> Unit,
    onDemoLoginCustomer: () -> Unit,
    onDemoLoginSpecialist: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }

    var nameInput by remember { mutableStateOf("") }
    var emailOrPhoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CervannaBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Brand Emblem
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CervannaTealPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.HealthAndSafety,
                    contentDescription = "Cervanna Care Logo",
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Cervanna Care",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CervannaTealDark
            )

            Text(
                text = "Kenya's Trusted Home-Care & Healthcare Marketplace",
                fontSize = 12.sp,
                color = CervannaTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tab Switcher: Sign In vs Create Account
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isRegisterMode) CervannaSurface else Color.Transparent)
                            .clickable { isRegisterMode = false }
                            .padding(vertical = 10.dp)
                            .testTag("tab_signin"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign In",
                            fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (!isRegisterMode) CervannaTealPrimary else CervannaTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isRegisterMode) CervannaSurface else Color.Transparent)
                            .clickable { isRegisterMode = true }
                            .padding(vertical = 10.dp)
                            .testTag("tab_register"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Register",
                            fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (isRegisterMode) CervannaTealPrimary else CervannaTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // User Role Selector
            Text(
                text = "Select Account Role",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CervannaTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoleSelectionCard(
                    role = UserRole.CUSTOMER,
                    isSelected = selectedRole == UserRole.CUSTOMER,
                    title = "Customer / User",
                    subtitle = "Find and book care for family",
                    icon = Icons.Default.Person,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedRole = UserRole.CUSTOMER }
                )

                RoleSelectionCard(
                    role = UserRole.SPECIALIST,
                    isSelected = selectedRole == UserRole.SPECIALIST,
                    title = "Care Specialist",
                    subtitle = "Nurses, caregivers & house staff",
                    icon = Icons.Default.MedicalServices,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedRole = UserRole.SPECIALIST }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            if (isRegisterMode) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Full Name") },
                    placeholder = { Text("e.g. Sarah Kamau", color = CervannaPlaceholder) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedPlaceholderColor = CervannaPlaceholder,
                        unfocusedPlaceholderColor = CervannaPlaceholder
                    ),
                    leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null, tint = CervannaTealPrimary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = emailOrPhoneInput,
                onValueChange = { emailOrPhoneInput = it },
                label = { Text(if (isRegisterMode) "Email or Phone (+254 7XX...)" else "Email or Phone Number") },
                placeholder = { Text(if (isRegisterMode) "e.g. 0712 345 678 or user@example.com" else "e.g. 0712 345 678", color = CervannaPlaceholder) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedPlaceholderColor = CervannaPlaceholder,
                    unfocusedPlaceholderColor = CervannaPlaceholder
                ),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CervannaTealPrimary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_email_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
                placeholder = { Text("••••••••", color = CervannaPlaceholder) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedPlaceholderColor = CervannaPlaceholder,
                    unfocusedPlaceholderColor = CervannaPlaceholder
                ),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CervannaTealPrimary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_password_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Action Button
            Button(
                onClick = {
                    val email = emailOrPhoneInput.trim()
                    val pwd = passwordInput.trim()
                    if (email.isBlank()) {
                        errorMessage = "Please enter your email or phone number."
                        return@Button
                    }
                    if (pwd.length < 4) {
                        errorMessage = "Password must be at least 4 characters."
                        return@Button
                    }
                    errorMessage = null
                    val name = if (isRegisterMode && nameInput.isNotBlank()) nameInput.trim()
                    else if (selectedRole == UserRole.CUSTOMER) "Sarah Kamau" else "Faith Muthoni, RN"

                    onLoginSuccess(selectedRole, email, name)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("auth_submit_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CervannaTealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isRegisterMode) "Create ${if (selectedRole == UserRole.CUSTOMER) "Customer" else "Specialist"} Account"
                    else "Sign In as ${if (selectedRole == UserRole.CUSTOMER) "Customer" else "Specialist"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = CervannaBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // Fast Demo Logins for Testing Both User Roles
            Text(
                text = "⚡ QUICK TEST CREDENTIALS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CervannaTextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onDemoLoginCustomer,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("demo_login_customer_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log In as Customer (Sarah Kamau)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onDemoLoginSpecialist,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("demo_login_specialist_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.MedicalServices, contentDescription = null, tint = CervannaAmberSecondary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log In as Specialist (Faith Muthoni, RN)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RoleSelectionCard(
    role: UserRole,
    isSelected: Boolean,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CervannaTealContainer else CervannaSurface
        ),
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) CervannaTealPrimary else CervannaBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .testTag("role_card_${role.name.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) CervannaTealPrimary else CervannaSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else CervannaTealPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = CervannaTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = CervannaTextSecondary,
                lineHeight = 13.sp
            )
        }
    }
}
