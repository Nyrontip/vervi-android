package com.example.verviapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.theme.VerviColors

/**
 * Bottom action bar for request details (Apply or View Applications button).
 */
@Composable
fun RequestActionBar(
    isOwner: Boolean,
    onApplyClick: () -> Unit,
    onViewApplicationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = VerviColors.BorderGray)
        if (isOwner) {
            VerviButton(
                text = "Ver Postulaciones",
                onClick = onViewApplicationsClick,
                color = VerviColors.Blue,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                height = 54.dp
            )
        } else {
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
