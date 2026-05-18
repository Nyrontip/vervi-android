package com.example.verviapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviSmallButton
import com.example.verviapp.ui.theme.VerviColors

@Composable
fun RequestActionBar(
    isOwner: Boolean,
    hasProvider: Boolean = false,
    isClosed: Boolean = false,
    onApplyClick: () -> Unit = {},
    onViewApplicationsClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = VerviColors.BorderGray)

        when {
            // Request is closed — show informational label, no action buttons
            isClosed -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = VerviColors.TextGray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Esta solicitud está cerrada",
                        fontSize = 14.sp,
                        color = VerviColors.TextGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            isOwner && hasProvider -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerviSmallButton(
                        text = "Cancelar",
                        color = VerviColors.CancelRed,
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f)
                    )
                    VerviSmallButton(
                        text = "Confirmar",
                        color = VerviColors.Primary,
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            isOwner -> {
                VerviButton(
                    text = "Ver Postulaciones",
                    onClick = onViewApplicationsClick,
                    color = VerviColors.Blue,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    height = 54.dp
                )
            }

            else -> {
                VerviButton(
                    text = "Postularse",
                    onClick = onApplyClick,
                    color = VerviColors.OrangeSecondary,
                    icon = Icons.AutoMirrored.Filled.Send,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    height = 54.dp
                )
            }
        }
    }
}
