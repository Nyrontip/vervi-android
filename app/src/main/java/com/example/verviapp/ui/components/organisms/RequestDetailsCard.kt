package com.example.verviapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verviapp.ui.components.atoms.StatusBadge
import com.example.verviapp.ui.components.molecules.BudgetDisplay
import com.example.verviapp.ui.components.molecules.InfoPillRow
import com.example.verviapp.ui.components.molecules.PublisherCard
import com.example.verviapp.ui.components.molecules.PublisherCardSkeleton
import com.example.verviapp.ui.theme.VerviColors

/**
 * Main content card for request details (title, budget, description, publisher).
 */
@Composable
fun RequestDetailsCard(
    title: String,
    status: String,
    price: String,
    date: String,
    location: String,
    description: String,
    publisherName: String?,
    publisherRating: String?,
    publisherAvatarUrl: String?,
    isOwner: Boolean,
    onPublisherChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        colors = CardDefaults.cardColors(containerColor = VerviColors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            // Title and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(text = status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Budget display
            BudgetDisplay(amount = price)

            Spacer(modifier = Modifier.height(18.dp))

            // Info pills (date + location)
            InfoPillRow(dateValue = date, locationValue = location)

            Spacer(modifier = Modifier.height(24.dp))

            // Description section
            Text(
                text = "Descripción",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                lineHeight = 25.sp,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Publisher section
            Text(
                text = if (isOwner) "PUBLICADO POR" else "PROVEEDOR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9AA2AF),
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (publisherName != null) {
                PublisherCard(
                    name = publisherName,
                    ratingLine = publisherRating ?: "Sin calificaciones",
                    avatarUrl = publisherAvatarUrl,
                    onChatClick = onPublisherChatClick
                )
            } else {
                PublisherCardSkeleton()
            }
        }
    }
}
