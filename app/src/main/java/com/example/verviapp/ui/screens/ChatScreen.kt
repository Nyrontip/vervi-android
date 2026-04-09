package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.verviapp.viewmodel.ChatViewModel
import com.example.verviapp.model.ChatMessage
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.theme.VerviColors
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn

// ---------- ATOMS ----------

@Composable
fun Avatar(imageUrl: String, size: Dp = 40.dp) {
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
fun StatusDot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(10.dp)
            .background(VerviColors.StatusOnline, CircleShape)
            .border(2.dp, VerviColors.StatusBorder, CircleShape)
    )
}

@Composable
fun IconButtonCircle(icon: ImageVector, tint: Color = VerviColors.Primary, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .background(Color.Transparent, CircleShape)
    ) {
        Icon(icon, contentDescription = null, tint = tint)
    }
}

@Composable
fun MessageBubble(message: String, isUser: Boolean) {
    val background =
        if (isUser) VerviColors.Primary.copy(alpha = 0.9f)
        else VerviColors.BackgroundOther

    val textColor =
        if (isUser) VerviColors.TextUser
        else VerviColors.TextOther

    Box(
        modifier = Modifier
            .background(background, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(text = message, color = textColor, fontSize = 15.sp)
    }
}

@Composable
fun AttachmentMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onFile: () -> Unit
) {
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(VerviColors.CardBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AttachmentItem("Galería", Icons.Default.Image, onGallery)
            AttachmentItem("Cámara", Icons.Default.CameraAlt, onCamera)
            AttachmentItem("Archivo", Icons.Default.InsertDriveFile, onFile)
        }
    }
}

@Composable
fun AttachmentItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = VerviColors.Primary)
        Spacer(Modifier.width(12.dp))
        Text(text, color = VerviColors.TextPrimary)
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {

    val alignment =
        if (message.isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {

        if (!message.isUser && message.avatar != null) {
            Avatar(message.avatar, 32.dp)
            Spacer(Modifier.width(8.dp))
        }

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() +
                    slideInHorizontally(
                        initialOffsetX = { if (message.isUser) it else -it }
                    ) +
                    scaleIn(initialScale = 0.8f)
        ) {

            Column(
                horizontalAlignment =
                    if (message.isUser) Alignment.End else Alignment.Start
            ) {

                // 💬 BURBUJA MEJORADA
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp,
                                bottomStart = if (message.isUser) 18.dp else 4.dp,
                                bottomEnd = if (message.isUser) 4.dp else 18.dp
                            )
                        )
                        .background(
                            if (message.isUser)
                                VerviColors.Primary
                            else
                                VerviColors.InputBackground
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message.text,
                        color = if (message.isUser)
                            VerviColors.TextUser
                        else
                            VerviColors.TextOther,
                        fontSize = 15.sp
                    )
                }

                Text(
                    message.time,
                    fontSize = 11.sp,
                    color = VerviColors.TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// ---------- CHAT INPUT ----------

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onAddFile: () -> Unit = {}
) {
    val isEnabled = text.isNotBlank()

    val sendColor by animateColorAsState(
        targetValue = if (isEnabled)
            VerviColors.Primary
        else
            VerviColors.CommentInputBackground,
        label = "sendColor"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 3.dp,
        color = VerviColors.InputBackground
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 📎 Adjuntar (más minimalista)
            IconButton(
                onClick = onAddFile,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adjuntar",
                    tint = VerviColors.Primary
                )
            }

            // ✏️ Input integrado (sin borde duro)
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text("Escribe un mensaje…")
                },
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = VerviColors.Primary
                ),
                maxLines = 4
            )

            // ➤ Botón enviar moderno
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(sendColor),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onSend,
                    enabled = isEnabled
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = if (isEnabled) Color.White else VerviColors.TextSecondary
                    )
                }
            }
        }
    }
}

// ---------- TOP BAR ----------

@Composable
fun ChatTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerviColors.Primary)
            .padding(vertical = 4.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButtonCircle(Icons.Default.ArrowBack, tint = VerviColors.TextWhite) { onBack() }
        Spacer(Modifier.width(8.dp))
        Box {
            Avatar(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuBbXJ6mwDBe7aVLNNLYT3qvuXHAzHznWBIhM55cvQSvU3-8xDX56fHQDumSJVMqGfoYWmwPoX4mSuQWf4VALZUafhYNLfT4pb--W3VdnHpbdtPORb_0_2LyxIII_-1wFKn0AjefyIk25IPTNcdTGF-vr3HOEcEuuPyi2AW9ZjRRMgwr04DSwDnUxNB35QZ4HzznnUcv80f768GU2yLXN1lnsoOHF1yKM8_DM4NX6MSHXjeBbTTRZS2fIU5_kRzqGaM830P3vJKh7akT"
            )
            StatusDot(modifier = Modifier.align(Alignment.BottomEnd))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text("Carlos Ruiz", fontWeight = FontWeight.SemiBold, color = VerviColors.TextWhite)
            Text("Proveedor de Limpieza", fontSize = 12.sp, color = VerviColors.TextWhite)
        }
        IconButtonCircle(Icons.Default.Call, tint = VerviColors.TextWhite) {}
        //IconButtonCircle(Icons.Default.MoreVert, tint = VerviColors.TextWhite) {} No es util por ahora
    }
}

// ---------- MESSAGES LIST ----------

@Composable
fun ChatMessagesList(messages: List<ChatMessage>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        reverseLayout = false
    ) {
        items(
            items = messages,
            key = { it.hashCode() }
        ) { message ->
            ChatMessageItem(message)
        }
    }
}

// ---------- SCREEN ----------

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val messages = state.messages

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(VerviColors.BgColor)
    ) {
        ChatTopBar { navController.popBackStack() }
        ChatMessagesList(messages = messages, modifier = Modifier.weight(1f))
        AttachmentMenu(
            expanded = state.showAttachments,
            onDismiss = { viewModel.toggleAttachments() },
            onGallery = { viewModel.toggleAttachments() },
            onCamera = { viewModel.toggleAttachments() },
            onFile = { viewModel.toggleAttachments() }
        )
        ChatInput(
            text = state.inputText,
            onTextChange = { viewModel.onInputChange(it) },
            onSend = { viewModel.sendMessage() },
            onAddFile = { viewModel.toggleAttachments() }
        )
    }
}