package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.model.Review
import com.example.data.model.Specialist
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSpecialistDetailScreen(
    specialist: Specialist,
    reviews: List<Review>,
    onBack: () -> Unit,
    onBookNow: () -> Unit,
    onContactSpecialist: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val specialistReviews = reviews.filter { it.specialistId == specialist.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(specialist.name, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.testTag("detail_favorite_button")) {
                        Icon(
                            imageVector = if (specialist.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (specialist.isFavorite) Color(0xFFE11D48) else CervannaTextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CervannaSurface)
            )
        },
        bottomBar = {
            Surface(
                color = CervannaSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "KSh ${specialist.dailyRateKsh}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = CervannaTealDark
                        )
                        Text(
                            text = "Daily Rate (Day Shift)",
                            fontSize = 11.sp,
                            color = CervannaTextSecondary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onContactSpecialist,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("detail_contact_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enquire")
                        }

                        Button(
                            onClick = onBookNow,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CervannaTealPrimary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("detail_book_now_btn")
                        ) {
                            Text("Book Now", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CervannaBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Specialist Header Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
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
                                Text(
                                    text = specialist.title,
                                    fontSize = 13.sp,
                                    color = CervannaTextSecondary
                                )
                                if (specialist.agencyAffiliation != null) {
                                    Text(
                                        text = "Agency: ${specialist.agencyAffiliation}",
                                        fontSize = 11.sp,
                                        color = CervannaAmberSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CervannaSurfaceVariant)
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = CervannaAmberSecondary, modifier = Modifier.size(16.dp))
                                    Text(" ${specialist.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Text("${specialist.reviewCount} Reviews", fontSize = 11.sp, color = CervannaTextSecondary)
                            }
                            Divider(modifier = Modifier.height(30.dp).width(1.dp), color = CervannaBorder)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${specialist.experienceYears} Years", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CervannaTextPrimary)
                                Text("Experience", fontSize = 11.sp, color = CervannaTextSecondary)
                            }
                            Divider(modifier = Modifier.height(30.dp).width(1.dp), color = CervannaBorder)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (specialist.isAvailable) "Available" else "Busy",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (specialist.isAvailable) CervannaTealPrimary else CervannaAmberSecondary
                                )
                                Text("Schedule", fontSize = 11.sp, color = CervannaTextSecondary)
                            }
                        }
                    }
                }
            }

            // Verification Scope Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaTealContainer.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Verification & Vetting Scope",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = CervannaTealDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        specialist.verificationBadges.forEach { badge ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(14.dp))
                                Text(text = badge, fontSize = 12.sp, color = CervannaOnTealContainer)
                            }
                        }
                    }
                }
            }

            // Bio Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "About", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CervannaTextPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = specialist.bio,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = CervannaTextSecondary
                        )
                    }
                }
            }

            // Skills & Services Offered
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Services & Specialized Skills", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CervannaTextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "Core Healthcare Services", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CervannaTealPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        specialist.serviceCategories.forEach { sc ->
                            Text(text = "• ${sc.displayName}", fontSize = 12.sp, color = CervannaTextPrimary, modifier = Modifier.padding(vertical = 1.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Clinical & Practical Skills", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CervannaTealPrimary)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            specialist.skills.forEach { skill ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CervannaSurfaceVariant)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = skill, fontSize = 11.sp, color = CervannaTextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Education & Credentials
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Education & Certifications", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CervannaTextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = specialist.education, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = CervannaTextPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        specialist.qualifications.forEach { q ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.School, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(14.dp))
                                Text(text = q, fontSize = 12.sp, color = CervannaTextSecondary)
                            }
                        }
                    }
                }
            }

            // Service Locations & Languages
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Locations & Languages", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CervannaTextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(16.dp))
                            Text("Service Areas: ${specialist.locations.joinToString(", ")}", fontSize = 12.sp, color = CervannaTextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = CervannaTealPrimary, modifier = Modifier.size(16.dp))
                            Text("Spoken: ${specialist.languages.joinToString(", ")}", fontSize = 12.sp, color = CervannaTextSecondary)
                        }
                    }
                }
            }

            // Customer Reviews
            item {
                Text(
                    text = "Customer Reviews (${specialistReviews.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CervannaTextPrimary
                )
            }

            if (specialistReviews.isEmpty()) {
                item {
                    Text(
                        text = "No reviews yet. Be the first to book and rate ${specialist.name}!",
                        fontSize = 12.sp,
                        color = CervannaTextSecondary
                    )
                }
            } else {
                items(specialistReviews) { rev ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = rev.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CervannaTextPrimary)
                                Row {
                                    (1..rev.rating).forEach {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = CervannaAmberSecondary, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                            Text(text = "${rev.serviceName} • ${rev.date}", fontSize = 11.sp, color = CervannaTextTertiary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "\"${rev.comment}\"", fontSize = 12.sp, color = CervannaTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
