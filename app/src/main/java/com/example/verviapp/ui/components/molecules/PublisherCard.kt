package com.example.verviapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verviapp.ui.components.atoms.Avatar
import com.example.verviapp.ui.components.atoms.CircularActionButton
import com.example.verviapp.ui.theme.VerviColors

/**
 * Card displaying publisher/provider information with avatar, name, rating, and chat button.
 */
@Composable
fun PublisherCard(
    name: String,
    ratingLine: String,
    avatarUrl: String?,
    showChat: Boolean = true,
    onChatClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            imageUrl = avatarUrl,
            contentDescription = "Publicador",
            modifier = Modifier.size(54.dp),
            isOnline = true
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = ratingLine,
                color = VerviColors.TextSecondary,
                fontSize = 14.sp
            )
        }

        if (showChat) {
            CircularActionButton(
                icon = Icons.AutoMirrored.Outlined.Chat,
                contentDescription = "Chat",
                onClick = onChatClick,
                tint = VerviColors.Primary,
                backgroundColor = Color(0xFFF0F4F8)
            )
        }
    }
}

/**
 * Skeleton/loading state for PublisherCard.
 */
@Composable
fun PublisherCardSkeleton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(50))
                .background(VerviColors.BorderGray.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .background(VerviColors.BorderGray.copy(alpha = 0.2f))
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(14.dp)
                    .background(VerviColors.BorderGray.copy(alpha = 0.2f))
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    }
}
