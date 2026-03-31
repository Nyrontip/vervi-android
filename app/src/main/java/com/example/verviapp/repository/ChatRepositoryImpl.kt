package com.example.verviapp.repository

import com.example.verviapp.model.ChatMessage

class ChatRepositoryImpl : ChatRepository {

    private val messages = mutableListOf(
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

    override fun getHistory(): List<ChatMessage> = messages.toList()

    override fun send(message: ChatMessage) {
        messages.add(message)
    }

    override fun update(message: ChatMessage) {
        val idx = messages.indexOfFirst { it.hashCode() == message.hashCode() }
        if (idx >= 0) messages[idx] = message
    }
}

