package com.example.verviapp.data.repository
import com.example.verviapp.data.entity.ConversationEntity
import com.example.verviapp.data.entity.MessageEntity
import com.example.verviapp.data.entity.RequestEntity
import com.example.verviapp.data.entity.UserEntity
/**
 * Datos de muestra para inicializar la BD en primera ejecuci�n.
 * Centralizados aqu� para f�cil mantenimiento y reutilizaci�n.
 */
object SampleData {
    const val CHAT_LOCAL_USER_ID = 9001
    const val CHAT_REMOTE_USER_ID = 9002
    const val CHAT_CONVERSATION_ID = 5001
    const val CHAT_LOCAL_EMAIL = "chat.local@vervi.app"
    const val CHAT_REMOTE_EMAIL = "chat.remote@vervi.app"

    val sampleUsers = listOf(
        UserEntity(
            id = CHAT_LOCAL_USER_ID,
            name = "Cliente",
            email = CHAT_LOCAL_EMAIL,
            isProvider = false,
            isOnline = true
        ),
        UserEntity(
            id = CHAT_REMOTE_USER_ID,
            name = "Carlos Ruiz",
            email = CHAT_REMOTE_EMAIL,
            isProvider = true,
            isOnline = true,
            photoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDRoJbuz12pcHLb_QstqeS_pEkfsnOEmftV2Ed727AU3t7bOSHjkSfmCY2JfVePqvopofXX7vTDuvAtMCWbilnFLg_UteSicoAczML_D9PBjI0u3D_lCZ-2105dT2Mwv4OwMnkAO0CpuprG8o6wkdVqagwkTNqWhUPtwj3dZv7Vrx1mEuxbBl4UEjgLoTuGsL33f_JCbYv5gu00GBxBjHtqO18o4EutH2iWhrnjS8BqynnfujcsdMGAbM7C8sUbaRuSwpja0lZrAMUJ"
        )
    )

    val sampleConversations = listOf(
        ConversationEntity(
            id = CHAT_CONVERSATION_ID,
            participantAUserId = CHAT_LOCAL_USER_ID,
            participantBUserId = CHAT_REMOTE_USER_ID,
            lastMessagePreview = "Hola Carlos, necesito una cotizacion para limpieza.",
            lastMessageAt = System.currentTimeMillis() - 60_000
        )
    )

    val sampleMessages = listOf(
        MessageEntity(
            id = 7001,
            conversationId = CHAT_CONVERSATION_ID,
            senderUserId = CHAT_REMOTE_USER_ID,
            body = "Hola, en que puedo ayudarte hoy?",
            sentAt = System.currentTimeMillis() - 3 * 60_000
        ),
        MessageEntity(
            id = 7002,
            conversationId = CHAT_CONVERSATION_ID,
            senderUserId = CHAT_LOCAL_USER_ID,
            body = "Hola Carlos, necesito una cotizacion para limpieza.",
            sentAt = System.currentTimeMillis() - 60_000
        )
    )

    val sampleRequests = listOf(
        RequestEntity(
            status = "En curso",
            title = "Mantenimiento de Aire Acondicionado",
            date = "12 Oct 2023",
            applications = "3 Postulaciones",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB67969hdR51hZJ4MMl2KcD7Be19oLLeVWL6ROGoIgDcM5U8XflIO3tkfgCzwuH2MJSl_8_Gheu3jkhhVNTufiHwaXoHb7SQgsbVTt4ldPwE8EZWXv_CVNcD3c_ohEvLSEQECdZzXvKyJzDDMy7F2KJt6ksuHGcG9YPaLmghhRMZ-w50KFxTEAAEexjyZI93LFChiCnrTJaogs1fQs6Zg7JZew6NBm_Ul9K9ZD0v3ViKD5bnk4NnJMmmYK7kKDu899cW_Owf4R2VWgC",
            buttonText = "Gestionar",
            isActive = true,
            description = "Se requiere revisión técnica completa del sistema de aire acondicionado central.",
            location = "Bogotá, Chapinero",
            applicationCount = 3,
            isUrgent = true
        ),
        RequestEntity(
            status = "Pendiente",
            title = "Clase Particular de Matemáticas",
            date = "10 Oct 2023",
            applications = "1 Postulación",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnl5p6ez-eg1L9zbqiPi776Iuugs_VciEqTLCanHi4PTptvUeBTYJuf-37hVntOaYzL8bDnsTTKRHFJwN13XDMvun57PjsABpdDapeKr5jXLYXSeSCOgbXTSjBlR0fXGcccOJv4v65jC0UpTk08VTtb8zFh-dMNEbmLlrK_d1oKkSCctoal7ERBV0fzFgeWYoZgzuJkqdx3GOQcCisgg-a5Q62_eZGbY52d-xyOXEzl-9nVrhRf83hHkWdJb6apj-WOF2Jhibnkhgr",
            buttonText = "Gestionar",
            isActive = true,
            description = "Necesito ayuda con cálculo vectorial y ecuaciones diferenciales.",
            location = "Bogotá, Usaquén",
            applicationCount = 1,
            isUrgent = false
        ),
        RequestEntity(
            status = "Borrador",
            title = "Reparación de Fuga de Agua",
            date = "Hoy",
            applications = "0 Postulaciones",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1bULF4g5fXFqhPjmuBYe3t3C2ROTKCc-X0AhOtpq620jekQb5iaUTK4G9oJP8fgGCm0gAyfMrtOmohM7ZaGZWqqQAnEhUOuusSHcDw25yFhxbiVhJx49fM57C8qxUHvQOcZ9IhDMoGNVrjiSWf7FMd7ypM-yxc9r2N8jGchiccQVbJNk3dabVUAOzIsaFOHeo9KzbjjMGsXseOWuLzOKdRN7EAguL-tsgVPTd-Dcs-joD9YSmKv6lHC8brYllkaAd8GFT31KpmHQv",
            buttonText = "Continuar",
            isActive = false,
            description = "Fuga activa en la tuber�a principal de la cocina.",
            location = "Bogotá, Cedritos",
            applicationCount = 0,
            isUrgent = true
        )
    )
}
