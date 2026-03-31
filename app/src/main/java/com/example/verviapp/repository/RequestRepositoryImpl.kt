package com.example.verviapp.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.Color
import com.example.verviapp.model.RequestItem

class RequestRepositoryImpl : RequestRepository {
    override suspend fun getRequests(activeOnly: Boolean?): List<RequestItem> {
        // For now return sample data; in future this will call a remote data source
        val all = sampleRequests
        return when (activeOnly) {
            true -> all.filter { it.status != "Finalizado" }
            false -> all.filter { it.status == "Finalizado" }
            null -> all
        }
    }

    companion object {
        private val sampleRequests = listOf(
            RequestItem(
                status = "En curso",
                statusColor = Color(0xFF10B981),
                title = "Mantenimiento de Aire Acondicionado",
                date = "12 Oct 2023",
                applications = "3 Postulaciones",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB67969hdR51hZJ4MMl2KcD7Be19oLLeVWL6ROGoIgDcM5U8XflIO3tkfgCzwuH2MJSl_8_Gheu3jkhhVNTufiHwaXoHb7SQgsbVTt4ldPwE8EZWXv_CVNcD3c_ohEvLSEQECdZzXvKyJzDDMy7F2KJt6ksuHGcG9YPaLmghhRMZ-w50KFxTEAAEexjyZI93LFChiCnrTJaogs1fQs6Zg7JZew6NBm_Ul9K9ZD0v3ViKD5bnk4NnJMmmYK7kKDu899cW_Owf4R2VWgC",
                buttonText = "Gestionar",
                secondaryIcon = Icons.Default.MoreHoriz
            ),
            RequestItem(
                status = "Pendiente",
                statusColor = Color(0xFFF59E0B),
                title = "Clase Particular de Matemáticas",
                date = "10 Oct 2023",
                applications = "1 Postulación",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnl5p6ez-eg1L9zbqiPi776Iuugs_VciEqTLCanHi4PTptvUeBTYJuf-37hVntOaYzL8bDnsTTKRHFJwN13XDMvun57PjsABpdDapeKr5jXLYXSeSCOgbXTSjBlR0fXGcccOJv4v65jC0UpTk08VTtb8zFh-dMNEbmLlrK_d1oKkSCctoal7ERBV0fzFgeWYoZgzuJkqdx3GOQcCisgg-a5Q62_eZGbY52d-xyOXEzl-9nVrhRf83hHkWdJb6apj-WOF2Jhibnkhgr",
                buttonText = "Gestionar",
                secondaryIcon = Icons.Default.MoreHoriz
            ),
            RequestItem(
                status = "Borrador",
                statusColor = Color.Gray,
                title = "Reparación de Fuga de Agua",
                date = "Hoy",
                applications = "0 Postulaciones",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1bULF4g5fXFqhPjmuBYe3t3C2ROTKCc-X0AhOtpq620jekQb5iaUTK4G9oJP8fgGCm0gAyfMrtOmohM7ZaGZWqqQAnEhUOuusSHcDw25yFhxbiVhJx49fM57C8qxUHvQOcZ9IhDMoGNVrjiSWf7FMd7ypM-yxc9r2N8jGchiccQVbJNk3dabVUAOzIsaFOHeo9KzbjjMGsXseOWuLzOKdRN7EAguL-tsgVPTd-Dcs-joD9YSmKv6lHC8brYllkaAd8GFT31KpmHQv",
                buttonText = "Continuar",
                secondaryIcon = Icons.Default.DeleteOutline
            )
        )
    }
}

