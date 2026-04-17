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
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
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
import com.example.verviapp.viewmodel.RequestConfirmEvent
import com.example.verviapp.viewmodel.RequestConfirmViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RequestConfirmScreen(
    navController: NavController,
    requestId: Int,
    viewModel: RequestConfirmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(requestId) {
        viewModel.load(requestId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is RequestConfirmEvent.Confirmed) {
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
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircleOutline,
                    contentDescription = null,
                    tint = VerviColors.Blue,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Confirmar Servicio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Estas seguro de marcar este servicio como completado? Esto liberara el pago al proveedor.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = VerviColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.paymentReceiptConfirmed,
                    onCheckedChange = { viewModel.onPaymentReceiptChecked(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = VerviColors.Blue,
                        uncheckedColor = VerviColors.TextGray
                    )
                )
                Text(
                    text = "Confirmar recibo de pago (COP)",
                    fontSize = 16.sp,
                    color = VerviColors.TextDark
                )
            }

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
                text = if (uiState.isSubmitting) "Confirmando..." else "Confirmar",
                onClick = {
                    if (!uiState.isSubmitting) {
                        viewModel.confirmRequest()
                    }
                },
                color = VerviColors.Primary,
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
