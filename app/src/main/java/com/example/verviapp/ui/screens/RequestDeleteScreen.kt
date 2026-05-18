package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.RequestDeleteEvent
import com.example.verviapp.viewmodel.RequestDeleteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDeleteScreen(
    navController: NavController,
    requestId: Int,
    viewModel: RequestDeleteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(requestId) {
        viewModel.load(requestId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is RequestDeleteEvent.Deleted) {
                navController.popBackStack()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = sheetState,
        containerColor = VerviColors.BottomSheetBackground,
        scrimColor = VerviColors.Overlay
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFCBD5E1))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF2F2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = VerviColors.CancelRed,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Eliminar Borrador",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Estás seguro de que deseas eliminar este borrador? Esta acción no se puede deshacer.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = VerviColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            VerviButton(
                text = if (uiState.isSubmitting) "Eliminando..." else "Eliminar borrador",
                onClick = {
                    if (!uiState.isSubmitting) {
                        viewModel.deleteRequest()
                    }
                },
                color = VerviColors.CancelRed,
                height = 58.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Volver",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerviColors.TextSecondary,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
