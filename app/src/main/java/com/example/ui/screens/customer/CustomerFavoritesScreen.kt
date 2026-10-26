package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Specialist
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

@Composable
fun CustomerFavoritesScreen(
    favoriteSpecialists: List<Specialist>,
    onSelectSpecialist: (Specialist) -> Unit,
    onBookSpecialist: (Specialist) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onExploreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CervannaBackground)
    ) {
        // Header
        Surface(color = CervannaSurface, tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(
                    text = "Saved Care Specialists",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CervannaTextPrimary
                )
                Text(
                    text = "Quick access to your preferred vetted caregivers and nurses",
                    fontSize = 12.sp,
                    color = CervannaTextSecondary
                )
            }
        }

        if (favoriteSpecialists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = CervannaTextTertiary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No saved specialists yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CervannaTextPrimary
                    )
                    Text(
                        text = "Tap the heart icon on any specialist card to save them here for quick booking.",
                        fontSize = 12.sp,
                        color = CervannaTextSecondary,
                        modifier = Modifier.padding(top = 4.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CervannaTealPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Browse Specialists", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoriteSpecialists) { specialist ->
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
}
