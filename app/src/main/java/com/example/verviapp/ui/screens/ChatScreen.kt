package com.example.verviapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.navigation.NavController


//ATOMS
    @Composable
    fun Avatar(
        imageUrl: String,
        size: Dp = 40.dp
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "avatar",
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    fun StatusDot(
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(10.dp)
                .background(Color.Green, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }

    @Composable
    fun IconButtonCircle(
        icon: ImageVector,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF32619F))
        }
    }

    @Composable
    fun MessageBubble(
        message: String,
        isUser: Boolean
    ) {

        val background =
            if (isUser) Color(0xFF32619F)
            else Color.White

        val textColor =
            if (isUser) Color.White
            else Color(0xFF1E293B)

        Box(
            modifier = Modifier
                .background(
                    background,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message,
                color = textColor,
                fontSize = 15.sp
            )
        }
    }

// MODULES

    data class ChatMessage(
        val text: String,
        val time: String,
        val isUser: Boolean,
        val avatar: String? = null
    )

    @Composable
    fun ChatMessageItem(message: ChatMessage) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                if (message.isUser) Arrangement.End else Arrangement.Start
        ) {

            if (!message.isUser && message.avatar != null) {
                Avatar(message.avatar, 32.dp)
                Spacer(Modifier.width(8.dp))
            }

            Column(
                horizontalAlignment =
                    if (message.isUser) Alignment.End else Alignment.Start
            ) {

                MessageBubble(
                    message = message.text,
                    isUser = message.isUser
                )

                Text(
                    message.time,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }

    @Composable
    fun ChatInput(
        text: String,
        onTextChange: (String) -> Unit,
        onSend: () -> Unit
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {}) {
                Icon(Icons.Default.Add, null)
            }

            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                placeholder = { Text("Escribe un mensaje...") },
                shape = RoundedCornerShape(50)
            )

            IconButton(onClick = onSend) {
                Icon(Icons.Default.Send, null, tint = Color(0xFF32619F))
            }
        }
    }

//ORGANISM

    @Composable
    fun ChatTopBar() {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButtonCircle(
                icon = Icons.Default.ArrowBack,
                onClick = {}
            )

            Spacer(Modifier.width(8.dp))

            Box {

                Avatar(
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuBbXJ6mwDBe7aVLNNLYT3qvuXHAzHznWBIhM55cvQSvU3-8xDX56fHQDumSJVMqGfoYWmwPoX4mSuQWf4VALZUafhYNLfT4pb--W3VdnHpbdtPORb_0_2LyxIII_-1wFKn0AjefyIk25IPTNcdTGF-vr3HOEcEuuPyi2AW9ZjRRMgwr04DSwDnUxNB35QZ4HzznnUcv80f768GU2yLXN1lnsoOHF1yKM8_DM4NX6MSHXjeBbTTRZS2fIU5_kRzqGaM830P3vJKh7akT"
                )

                StatusDot(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(Modifier.width(8.dp))

            Column(Modifier.weight(1f)) {

                Text(
                    "Carlos Ruiz",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "Proveedor de Limpieza",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButtonCircle(Icons.Default.Call) {}
            IconButtonCircle(Icons.Default.MoreVert) {}
        }
    }


    @Composable
    fun ChatMessagesList(
        messages: List<ChatMessage>,
        modifier: Modifier = Modifier   // ← aquí
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message)
            }
        }
    }

    // TEMPLATE
    @Composable
    fun ChatScreen(navController: NavController) {

        var text by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize()
                .systemBarsPadding()
        ) {

            ChatTopBar()

            ChatMessagesList(
                messages = messages,
                modifier = Modifier.weight(1f)
            )

            ChatInput(
                text = text,
                onTextChange = { text = it },
                onSend = {}
            )
        }
    }



// SCREEN


        val messages = listOf(
            ChatMessage(
                "Hola, ¿en qué puedo ayudarte hoy?",
                "09:12 AM",
                false,
                "https://lh3.googleusercontent.com/aida-public/AB6AXuDRoJbuz12pcHLb_QstqeS_pEkfsnOEmftV2Ed727AU3t7bOSHjkSfmCY2JfVePqvopofXX7vTDuvAtMCWbilnFLg_UteSicoAczML_D9PBjI0u3D_lCZ-2105dT2Mwv4OwMnkAO0CpuprG8o6wkdVqagwkTNqWhUPtwj3dZv7Vrx1mEuxbBl4UEjgLoTuGsL33f_JCbYv5gu00GBxBjHtqO18o4EutH2iWhrnjS8BqynnfujcsdMGAbM7C8sUbaRuSwpja0lZrAMUJ"
            ),
            ChatMessage(
                "Hola Carlos, necesito una cotización para limpieza.",
                "09:15 AM",
                true
            )
        )


