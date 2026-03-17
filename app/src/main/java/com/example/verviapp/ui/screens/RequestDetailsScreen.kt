package com.example.verviapp.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors


// -----------------------------------------------------
// ATOMS
// -----------------------------------------------------

@Composable
fun AppIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier.size(24.dp),
        tint = tint
    )
}

@Composable
fun TitleText(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
fun StatusBadge(text: String, color: androidx.compose.ui.graphics.Color) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun Avatar(url: String) {

    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(2.dp, VerviColors.Primary.copy(.2f), CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PrimaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = VerviColors.Primary
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Icon(icon, null)

        Spacer(Modifier.width(8.dp))

        Text(text, fontWeight = FontWeight.Bold)
    }
}


// -----------------------------------------------------
// MOLECULES
// -----------------------------------------------------

@Composable
fun ServiceHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            TitleText("Reparación Aire Acondicionado")

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                AppIcon(Icons.Outlined.CalendarToday, Modifier.size(16.dp))

                Spacer(Modifier.width(4.dp))

                Text(
                    "14 de Octubre, 2023 • 10:30 AM",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {

            Text(
                "$125.000",
                fontWeight = FontWeight.Bold,
                color = VerviColors.Primary,
                fontSize = 20.sp
            )

            Text(
                "COP TOTAL",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ChatCard(navController: NavController) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(VerviColors.CardBackground)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VerviColors.OrangeSecondary.copy(.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Chat, null, tint = VerviColors.OrangeSecondary)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {

                Text("Resumen de Chat", fontWeight = FontWeight.SemiBold)

                Text(
                    "Ver conversación con el cliente",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                { navController.navigate("chat")}
            ) {
                AppIcon(Icons.Outlined.ChevronRight)
            }
        }
    }
}

@Composable
fun ClientRow() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Avatar(
            "https://lh3.googleusercontent.com/aida-public/AB6AXuA4proU8h62Ta-hxlsKb4sn_tOVh71LEFwXcF5QBLhKrk3B8tPVOoHJSCGLo_DkCMtADx2mppfKT4TZv0R6MWuwCQCV_oBhvsm6q5Hlfq34SpwsGriembr4OPEOAcMQ7LcO_l9JAJKuAYBLQD1xa_EeXC1ZIW8GNbE8849OHo1Zjc_bAQ2AEy5Wg2-UQ52fUWLEs_uE5oBWnEpKRC9JNIVIr9gErPZTq8OXH10b_ShLLTG1Q3xet0_s2RZNtrK5OX3S5GUHjSCZP_QP"
        )

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {

            Text("Mariana Restrepo", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Outlined.Star,
                    null,
                    tint = VerviColors.StarFilled,
                    modifier = Modifier.size(16.dp)
                )

                Text("4.9", fontSize = 13.sp)

                Spacer(Modifier.width(4.dp))

                Text(
                    "• Medellín, Antioquia",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(onClick = {}) {
            Icon(Icons.Outlined.Call, null, tint = VerviColors.OrangeSecondary)
        }
    }
}


// -----------------------------------------------------
// ORGANISMS
// -----------------------------------------------------

@Composable
fun WorkSummarySection() {

    Column {

        SectionTitle("Resumen del Trabajo")

        Spacer(Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = VerviColors.CardBackground
            )
        ) {

            Text(
                "Se realizó la revisión técnica completa del sistema central. Se identificó fuga en el serpentín, se procedió a sellado y recarga de gas refrigerante R-410A. Limpieza profunda de filtros y drenaje incluida.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun EvidenceGallery() {

    val images = listOf(
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAqSS5a-xrrQHgeG8aJRjhsQjmDnhoVgSqV7xk7-tl8uWw91Us660y-5igqlR-FCKR9CtUdB_YyNZP6ezIqaznemYPwVGwKxWoBzNq2HUKzD4ibp1kS4_q5uTA9ehFv4Mrg_kEZXA5k7Ifx6PX1A6Q-O3F6d2yIBMOSUg3qScu4KvuHxzw2LwSXZkel36bsovDutGxTznsGPsVPIBGlC60wJ-rf9dWxtWtfvbCVhkl4Vtn1HhSo1onmosKKltJLoTYm31tu0dKBNsWv",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuDhZBU2xxX1oJm7TDWGMbzXqkH12m4QfnmI8vRo5bu5qCy16P3F70yxnwPsZ1UQzfVNc_RcV6UR5nBVUGlCYoI21hnq_q4pMZXuZiiKa63eDka09BRZvq2vQllKMZ2r8g9_XuoSe88f_tCqiMXBiz-9vMK_l27P7zHkTSSA-0wauDkYIWWwZqQWMrd243CtvJnAZBRrO2nvbnF-_noXA1bbuwEOoVn_steHIOf249lWdMMyD4-Ic9-KWSTgCyXWmSpSTb61SOLAa5uZ",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBjAV7UadivynJLQcW0ZCie0huPCq5-NgOsEUKoIixEJv_cQx9QyFr0ZAeV84se42IPISvVAeVfST19okT8dhlxRd_SNrS0Ja0kAforSZ8ItFM1xxUEZGQ12UaaNekxVTioulr46maivNSO05w7naHieBStO7kQee2Vi5137VfqTxCn1xQMGKPitZAu9GclF6BF5ymob3ewjysuuFkdwCi2bKrb_5V9WJ4CSaCRT6chQXRkGX0vdXF-Kvp5rKXEfeOIqlpA6iq4u7VQ"
    )

    Column {

        SectionTitle("Evidencia del Servicio")

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(120.dp)
        ) {

            items(images.size) {

                AsyncImage(
                    model = images[it],
                    contentDescription = null,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun ClientSection() {

    Column {

        SectionTitle("Cliente")

        Spacer(Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(VerviColors.CardBackground)
        ) {
            Box(Modifier.padding(16.dp)) {
                ClientRow()
            }
        }
    }
}


// -----------------------------------------------------
// SCREEN
// -----------------------------------------------------

@Composable
fun RequestDetailsScreen(navController: NavController) {

    Scaffold(

        topBar = {
            VerviTopBar("Detalles del servicio", { navController.popBackStack()})
        },

        bottomBar = {

            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                PrimaryButton(
                    "Calificar Servicio",
                    Icons.Outlined.Grade
                ) {
                    navController.navigate("service/rate")
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .background(VerviColors.BgColor)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(12.dp))

            Row {

                StatusBadge("Como: Prestador", VerviColors.Primary)

                Spacer(Modifier.width(8.dp))

                StatusBadge("Completado", VerviColors.StatusSuccess)
            }

            Spacer(Modifier.height(16.dp))

            ServiceHeader()

            Spacer(Modifier.height(24.dp))

            WorkSummarySection()

            Spacer(Modifier.height(24.dp))

            EvidenceGallery()

            Spacer(Modifier.height(24.dp))

            ChatCard(navController)

            Spacer(Modifier.height(24.dp))

            ClientSection()

            Spacer(Modifier.height(80.dp))
        }
    }
}