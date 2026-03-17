package com.example.verviapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviBottomBar
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors


/* ------------------------------------------------ */
/* DATA */
/* ------------------------------------------------ */

data class ServiceHistoryItem(
    val title: String,
    val provider: String,
    val date: String,
    val price: String,
    val imageUrl: String,
    val rating: Float?,
)

/* ------------------------------------------------ */
/* COLORS */
/* ------------------------------------------------ */

val PrimaryColor = Color(0xFF32619F)
/* ------------------------------------------------ */
/* ATOMS */
/* ------------------------------------------------ */

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = PrimaryColor,
        modifier = modifier
    )
}

@Composable
fun ServiceImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier
) {

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {

        repeat(5) { index ->
            val filled = index < rating.toInt()

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Text(
            text = rating.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UnratedBadge(
    modifier: Modifier = Modifier
) {

    Box(
        modifier
            .background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            "SIN CALIFICAR",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

/* ------------------------------------------------ */
/* MOLECULES */
/* ------------------------------------------------ */

@Composable
fun ServiceInfo(
    item: ServiceHistoryItem,
    modifier: Modifier = Modifier
) {

    Column(modifier) {

        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = "${item.provider} • ${item.date}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(4.dp))

        PrimaryText(item.price)
    }
}

@Composable
fun RatingSection(
    item: ServiceHistoryItem,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (item.rating != null) {
            RatingStars(item.rating)
        } else {
            UnratedBadge()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = if (item.rating != null) "Ver detalles" else "Calificar ahora",
                color = PrimaryColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                imageVector =
                    if (item.rating != null)
                        Icons.Default.ChevronRight
                    else
                        Icons.Default.Star,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/* ------------------------------------------------ */
/* ORGANISMS */
/* ------------------------------------------------ */

@Composable
fun ServiceHistoryCard(
    item: ServiceHistoryItem,
    modifier: Modifier = Modifier
) {

    Column(
        modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, Color(0xFFE5E7EB)),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ServiceInfo(
                item,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(12.dp))

            ServiceImage(item.imageUrl)
        }

        Spacer(Modifier.height(12.dp))

        Divider()

        Spacer(Modifier.height(8.dp))

        RatingSection(item)
    }
}


@Composable
fun TabRow(
    modifier: Modifier = Modifier
) {

    var selectedTab by remember { mutableStateOf(0) }

    Row(modifier.fillMaxWidth()) {

        TabButton(
            text = "Como Cliente",
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
        )

        TabButton(
            text = "Como Prestador",
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (selected) PrimaryColor else Color.Gray
        )

        Spacer(Modifier.height(6.dp))

        Box(
            Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(if (selected) PrimaryColor else Color.Transparent)
        )
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier
) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Inicio", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Explorar", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.History, null) },
            label = { Text("Historial", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil", fontSize = 10.sp) }
        )
    }
}

/* ------------------------------------------------ */
/* SCREEN / TEMPLATE */
/* ------------------------------------------------ */

@Composable
fun ServiceHistoryScreen(navController: NavController) {

    val services = listOf(

        ServiceHistoryItem(
            "Lavado de alfombras",
            "Sofía García",
            "12 Oct 2023",
            "$85.000 COP",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCI7wY-NP3RRke6e9YPkC_C4JJGTY6-sZWwol_N6RmV9LxlHUdBmZT5tARZWZAz5kUFUK4KELUUjt4HEZBR_E7Fd0R5-C8cqi6u7BqfOgtytm03utzPZrD5p43rlNIcrJkBIpIOEsEubkaMSL5iKzWmKNAmlJKSAzM7mjQ-UTfwlVyD5trH-6VmfSv-TnCSMuXEmRhEOGo9OdKO90sLqbUJZL8yZQ4oaelTvg-B0Xn52YD8OEK1AaU9aQVvt5bm_z3_WWIWmip233u0",
            5f
        ),

        ServiceHistoryItem(
            "Reparación Eléctrica",
            "Carlos Martínez",
            "05 Oct 2023",
            "$120.000 COP",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBij0dg11e1wjROPz0TeFPDue7tFDavmjgwzxrjSKLaYlyTML5uMU6LU9AdlGuxjUizWHMyR7qjkaQd9b6c86erHlBMKukYGUSLLJcbeOyTNrETPJj4lwtC7EspVj7cpPssQpnmJt4TlJzJ2hC40xH14_rtyPt2Q-UcNiXGIhoTEAjpAmTQxWEpomoEa6WnBNK7Y_oZRjjBWoIbeRc2wYkc7-7fP3EYpu-Iupe7jLgdEAg3-C53MFe6o0hzTDdLanaU7KPrVo3U_IXf",
            4f
        ),

        ServiceHistoryItem(
            "Paseo de Mascotas",
            "Laura Beltrán",
            "28 Sep 2023",
            "$35.000 COP",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAVbi0mLTyrm5jOwg7uitaxLafYmlwSpi9VdKzhzU0oMxWAQ10hmG2Dhk-8S8b1QVJuBH9RXVEfLacsEzygQikVb8rLfOoSkehboWW-TMl1wI4f33YsFBQkCZquQRywjlLQBu0kR-1UNwQi81I0jt3GIkjRXT0QQCWZ6y2BlEghA82vRIbT4SzL3GHd6sFac1STgkGtdjiX_gbPG1TZlOgGfaAfTT9-_mUKkNWO7MPn4x3sUzrxK8WoLEwcvD8bAdrMcMevOLwbiXAM",
            null
        ),

        ServiceHistoryItem(
            "Limpieza de Vidrios",
            "Mateo Ruiz",
            "15 Sep 2023",
            "$60.000 COP",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBPErMB0nkS4BOI8vq_hidq8Uo7CW-9L5MwPLI_zEemUFIEtVLwQIC50llT75CPCoiBFFRhzvZwapXXUYfJ5sA_lOg94TfugYiqB5xFfD3cJ-C9RsjN-3x7UvhDIrDC1lc_p79SmXneXqMBkbn-bfUdSZewVaJZK9AdIkn5xCd3GU1da8S7CQw3OIltmEmIco5WktCWlPG7cEX8-Hp1bFE2b9gN0lqUDBRQitaQEAw6wHZtun4ZDs4kMw-A9LAT3XV0bQQPhKOab4zH",
            4.8f
        )
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
                    VerviTopBar("Historial de servicios", { navController.popBackStack()})
                 },
        bottomBar = { VerviBottomBar(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .background(VerviColors.BgColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(services) {
                ServiceHistoryCard(it)
            }
        }
    }
}
