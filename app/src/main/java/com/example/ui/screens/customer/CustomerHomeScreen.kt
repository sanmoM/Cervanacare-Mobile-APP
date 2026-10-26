package com.example.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun CustomerHomeScreen(
    specialists: List<Specialist>,
    upcomingBookings: List<Booking>,
    onSelectService: (ServiceCategory) -> Unit,
    onSelectSpecialist: (Specialist) -> Unit,
    onBookSpecialist: (Specialist) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onExploreClick: () -> Unit,
    onLocationChange: (String) -> Unit,
    onSupportClick: () -> Unit
) {
    var selectedLocation by remember { mutableStateOf("Nairobi - Eastern Bypass") }
    val locations = listOf("Nairobi - Eastern Bypass", "Westlands", "Kilimani", "Karen", "Mombasa", "Kisumu", "Nakuru")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Location Selector Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = CervannaTealPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Care in:",
                        fontSize = 12.sp,
                        color = CervannaTextSecondary
                    )
                    Text(
                        text = selectedLocation,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CervannaTextPrimary
                    )
                }

                AssistChip(
                    onClick = {
                        val nextIdx = (locations.indexOf(selectedLocation) + 1) % locations.size
                        selectedLocation = locations[nextIdx]
                        onLocationChange(selectedLocation)
                    },
                    label = { Text("Change", fontSize = 11.sp, color = CervannaTealPrimary, fontWeight = FontWeight.SemiBold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = CervannaSurfaceVariant,
                        labelColor = CervannaTealPrimary
                    ),
                    modifier = Modifier.testTag("change_location_chip")
                )
            }
        }

        // Hero Banner Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(190.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cervanna_hero),
                        contentDescription = "Cervanna Care Professional Support",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Dark burgundy gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xCC400022),
                                        Color(0xF0280015)
                                    ),
                                    startY = 60f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = CervannaAmberSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "KENYA'S VETTED CARE MARKETPLACE",
                                color = CervannaAmberContainer,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "Trusted Nurses, Caregivers & House Managers",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Licensed by regulatory councils & background checked by DCI.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Search shortcut bar
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { onExploreClick() }
                    .testTag("home_search_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = CervannaTealPrimary
                    )
                    Text(
                        text = "Search by nurse, physiotherapist, nanny, or location...",
                        fontSize = 13.sp,
                        color = CervannaTextTertiary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filters",
                        tint = CervannaTealPrimary
                    )
                }
            }
        }

        // Active Booking banner (if any upcoming)
        val activeBooking = upcomingBookings.firstOrNull { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.PENDING }
        if (activeBooking != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CervannaTealContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
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
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(CervannaTealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Upcoming Booking #${activeBooking.id}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = CervannaTealDark
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (activeBooking.status == BookingStatus.CONFIRMED) CervannaTealDark else CervannaAmberSecondary)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = activeBooking.status.name,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "${activeBooking.specialistName} • ${activeBooking.serviceName}",
                                fontSize = 12.sp,
                                color = CervannaTextPrimary
                            )
                            Text(
                                text = "Starts: ${activeBooking.startDate} (${activeBooking.shiftType})",
                                fontSize = 11.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Section: 7 Core Healthcare & Home Care Services
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Specialized Services",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "All 7 Services",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CervannaTealPrimary,
                        modifier = Modifier.clickable { onExploreClick() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable category cards
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(ServiceCategory.values()) { category ->
                        ServiceCategoryCard(
                            category = category,
                            onClick = { onSelectService(category) }
                        )
                    }
                }
            }
        }

        // Section: Featured Specialists
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Featured Specialists",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = CervannaTextPrimary
                        )
                        Text(
                            text = "Vetted, verified, and ready to assist",
                            fontSize = 12.sp,
                            color = CervannaTextSecondary
                        )
                    }
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CervannaTealPrimary,
                        modifier = Modifier.clickable { onExploreClick() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Specialist list
        items(specialists.take(4)) { specialist ->
            SpecialistCard(
                specialist = specialist,
                onCardClick = { onSelectSpecialist(specialist) },
                onBookClick = { onBookSpecialist(specialist) },
                onFavoriteClick = { onToggleFavorite(specialist.id) }
            )
        }

        // Emergency & Support Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CervannaAmberContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CervannaAmberSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Need Immediate Home Care?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CervannaOnAmberContainer
                        )
                        Text(
                            text = "Our Nairobi Eastern Bypass dispatch hub coordinates same-day nurses & aides: +254 700 225533",
                            fontSize = 11.sp,
                            color = CervannaOnAmberContainer
                        )
                    }
                    Button(
                        onClick = onSupportClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CervannaAmberSecondary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCategoryCard(
    category: ServiceCategory,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(135.dp)
            .height(130.dp)
            .clickable { onClick() }
            .testTag("service_card_${category.name}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CervannaTealContainer),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (category) {
                    ServiceCategory.PRE_POST_PREGNANCY -> Icons.Default.ChildCare
                    ServiceCategory.POST_SURGERY -> Icons.Default.Healing
                    ServiceCategory.ELDERLY_CARE -> Icons.Default.Elderly
                    ServiceCategory.PHYSIOTHERAPY -> Icons.Default.FitnessCenter
                    ServiceCategory.NANNY_HOUSEKEEPING -> Icons.Default.Home
                    ServiceCategory.SPECIAL_NEEDS -> Icons.Default.Accessible
                    ServiceCategory.PALLIATIVE_CARE -> Icons.Default.VolunteerActivism
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CervannaTealPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = category.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = CervannaTextPrimary,
                    lineHeight = 15.sp
                )
                Text(
                    text = category.iconDescription,
                    fontSize = 9.sp,
                    color = CervannaTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SpecialistCard(
    specialist: Specialist,
    onCardClick: () -> Unit,
    onBookClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CervannaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onCardClick() }
            .testTag("specialist_card_${specialist.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar Initials with verified ring
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(CervannaTealPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = specialist.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
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
                                fontSize = 15.sp,
                                color = CervannaTextPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Specialist",
                                tint = CervannaTealPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = specialist.title,
                            fontSize = 12.sp,
                            color = CervannaTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = CervannaAmberSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = " ${specialist.rating}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = CervannaTextPrimary
                                )
                                Text(
                                    text = " (${specialist.reviewCount})",
                                    fontSize = 11.sp,
                                    color = CervannaTextSecondary
                                )
                            }
                            Text(text = "•", color = CervannaTextTertiary)
                            Text(
                                text = "${specialist.experienceYears} yrs exp",
                                fontSize = 11.sp,
                                color = CervannaTextSecondary
                            )
                        }
                    }
                }

                // Favorite button
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp).testTag("fav_btn_${specialist.id}")
                ) {
                    Icon(
                        imageVector = if (specialist.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (specialist.isFavorite) Color(0xFFE11D48) else CervannaTextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Skills chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                specialist.skills.take(3).forEach { skill ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CervannaSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = skill,
                            fontSize = 10.sp,
                            color = CervannaTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = CervannaBorder.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Bottom price and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "From KSh ${specialist.dailyRateKsh}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CervannaTealDark
                    )
                    Text(
                        text = "per day • ${specialist.locations.firstOrNull() ?: "Nairobi"}",
                        fontSize = 11.sp,
                        color = CervannaTextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onCardClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Profile", fontSize = 12.sp, color = CervannaTealPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onBookClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CervannaTealPrimary,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("book_btn_${specialist.id}")
                    ) {
                        Text("Book Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
