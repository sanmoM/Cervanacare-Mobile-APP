package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FilterAlt
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
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerExploreScreen(
    specialists: List<Specialist>,
    searchQuery: String,
    selectedServiceCategory: ServiceCategory?,
    selectedProfessionalCategory: ProfessionalCategory?,
    selectedLocation: String?,
    sortBy: String,
    onSearchChange: (String) -> Unit,
    onSelectServiceCategory: (ServiceCategory?) -> Unit,
    onSelectProfessionalCategory: (ProfessionalCategory?) -> Unit,
    onSelectLocation: (String?) -> Unit,
    onSortChange: (String) -> Unit,
    onResetFilters: () -> Unit,
    onSelectSpecialist: (Specialist) -> Unit,
    onBookSpecialist: (Specialist) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    val sortOptions = listOf("Rating: High to Low", "Price: Low to High", "Price: High to Low", "Experience: High to Low")
    var sortExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground)
    ) {
        // Top Search Bar
        Surface(
            color = CervannaSurface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search specialists, skills, or services...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CervannaTealPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Filter & Sort Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Filter Chips Bar
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = showFilterSheet,
                            onClick = { showFilterSheet = true },
                            label = { Text("All Filters", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier.testTag("all_filters_chip")
                        )

                        // 7 Main Service Chips
                        ServiceCategory.values().forEach { cat ->
                            val isSelected = selectedServiceCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectServiceCategory(if (isSelected) null else cat) },
                                label = { Text(cat.displayName, fontSize = 11.sp) },
                                modifier = Modifier.testTag("filter_chip_${cat.name}")
                            )
                        }
                    }

                    // Sort dropdown trigger
                    Box {
                        IconButton(
                            onClick = { sortExpanded = true },
                            modifier = Modifier.testTag("sort_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort Options",
                                tint = CervannaTealPrimary
                            )
                        }

                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false }
                        ) {
                            sortOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = opt,
                                            fontWeight = if (opt == sortBy) FontWeight.Bold else FontWeight.Normal,
                                            color = if (opt == sortBy) CervannaTealPrimary else CervannaTextPrimary
                                        )
                                    },
                                    onClick = {
                                        onSortChange(opt)
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active filters indicators & count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${specialists.size} Specialists Available",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CervannaTextSecondary
            )

            if (selectedServiceCategory != null || selectedProfessionalCategory != null || selectedLocation != null || searchQuery.isNotBlank()) {
                Text(
                    text = "Reset Filters",
                    fontSize = 11.sp,
                    color = CervannaAmberSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onResetFilters() }
                        .testTag("reset_filters_btn")
                )
            }
        }

        // Specialist Results List
        if (specialists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = CervannaTextTertiary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No specialists match your criteria",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Try adjusting your service category, location, or price filters.",
                        fontSize = 12.sp,
                        color = CervannaTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onResetFilters,
                        colors = ButtonDefaults.buttonColors(containerColor = CervannaTealPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(specialists) { specialist ->
                    SpecialistCard(
                        specialist = specialist,
                        onCardClick = { onSelectSpecialist(specialist) },
                        onBookClick = { onBookSpecialist(specialist) },
                        onFavoriteClick = { onToggleFavorite(specialist.id) }
                    )
                }
            }
        }
    }

    // Comprehensive Filter Modal Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter Specialists",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = CervannaTextPrimary
                    )
                    TextButton(onClick = {
                        onResetFilters()
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = CervannaAmberSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Professional Categories
                Text(
                    text = "Professional Category",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CervannaTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ProfessionalCategory.values().forEach { pc ->
                        FilterChip(
                            selected = selectedProfessionalCategory == pc,
                            onClick = {
                                onSelectProfessionalCategory(if (selectedProfessionalCategory == pc) null else pc)
                            },
                            label = { Text(pc.displayName, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location selector
                Text(
                    text = "Service Location in Kenya",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CervannaTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                val locs = listOf("Nairobi - Eastern Bypass", "Westlands", "Kilimani", "Karen", "Mombasa", "Kisumu", "Nakuru")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    locs.forEach { loc ->
                        FilterChip(
                            selected = selectedLocation == loc,
                            onClick = { onSelectLocation(if (selectedLocation == loc) null else loc) },
                            label = { Text(loc, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showFilterSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("apply_filters_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CervannaTealPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Filters", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
