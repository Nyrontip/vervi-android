package com.example.verviapp.data.repository

import com.example.verviapp.data.entity.CategoryEntity
import com.example.verviapp.data.entity.RequestEntity
import com.example.verviapp.data.entity.RequestAttachmentEntity
import com.example.verviapp.data.entity.ReviewEntity
import com.example.verviapp.data.entity.ServiceEntity
import com.example.verviapp.data.entity.ServiceEvidenceEntity
import com.example.verviapp.data.entity.UserEntity
import com.example.verviapp.data.entity.ConversationEntity
import com.example.verviapp.data.entity.MessageEntity
import com.example.verviapp.data.entity.UserCategoryCrossRef
import com.example.verviapp.data.entity.NotificationEntity
/**
 * Datos de muestra para inicializar la BD en primera ejecucion.
 * Centralizados aqui para facil mantenimiento y reutilizacion.
 */
object SampleData { 
    const val DEMO_PROVIDER_USER_ID = 9000
    const val CHAT_LOCAL_USER_ID = 9001
    const val CHAT_REMOTE_USER_ID = 9002
    const val CHAT_CONVERSATION_ID = 5001
    const val CHAT_LOCAL_EMAIL = "chat.local@vervi.app"
    const val CHAT_REMOTE_EMAIL = "chat.remote@vervi.app"
    const val REQUEST_DETAILS_REQUEST_ID = 1001
    const val REQUEST_DETAILS_CLIENT_ID = 9101
    const val REQUEST_DETAILS_CONVERSATION_ID = 5101
    const val SERVICE_LOCAL_USER_ID = CHAT_LOCAL_USER_ID
    const val SERVICE_REMOTE_USER_ID = CHAT_REMOTE_USER_ID
    const val NOTIFICATIONS_USER_ID = CHAT_LOCAL_USER_ID

    const val CATEGORY_CARPINTEROS_ID = 1101
    const val CATEGORY_PLOMEROS_ID = 1102
    const val CATEGORY_ELECTRICISTAS_ID = 1103
    const val CATEGORY_LIMPIEZA_ID = 1104
    const val CATEGORY_TUTORIAS_ID = 1105

    val sampleUsers = listOf(
        UserEntity(
            id = DEMO_PROVIDER_USER_ID,
            name = "Usuario Demo",
            email = "test@vervi.com",
            password = "123456",
            bio = "Usuario base para pruebas de login local.",
            location = "Bogota, Colombia",
            isProvider = true,
            rating = 4.6f,
            reviewCount = 18,
            suggestedPriceCop = 65000
        ),
        UserEntity(
            id = CHAT_LOCAL_USER_ID,
            name = "Mateo Gomez",
            email = CHAT_LOCAL_EMAIL,
            isProvider = true,
            isOnline = true,
            password = "123456",
            rating = 4.7f,
            reviewCount = 32,
            suggestedPriceCop = 75000
        ),
        UserEntity(
            id = CHAT_REMOTE_USER_ID,
            name = "Carlos Ruiz",
            email = CHAT_REMOTE_EMAIL,
            isProvider = true,
            isOnline = true,
            password = "123456",
            rating = 4.9f,
            reviewCount = 48,
            suggestedPriceCop = 80000,
            photoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDRoJbuz12pcHLb_QstqeS_pEkfsnOEmftV2Ed727AU3t7bOSHjkSfmCY2JfVePqvopofXX7vTDuvAtMCWbilnFLg_UteSicoAczML_D9PBjI0u3D_lCZ-2105dT2Mwv4OwMnkAO0CpuprG8o6wkdVqagwkTNqWhUPtwj3dZv7Vrx1mEuxbBl4UEjgLoTuGsL33f_JCbYv5gu00GBxBjHtqO18o4EutH2iWhrnjS8BqynnfujcsdMGAbM7C8sUbaRuSwpja0lZrAMUJ"
        ),
        UserEntity(
            id = REQUEST_DETAILS_CLIENT_ID,
            name = "Mariana Restrepo",
            email = "mariana.restrepo@vervi.app",
            password = "123456",
            location = "Medellin, Antioquia",
            photoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA4proU8h62Ta-hxlsKb4sn_tOVh71LEFwXcF5QBLhKrk3B8tPVOoHJSCGLo_DkCMtADx2mppfKT4TZv0R6MWuwCQCV_oBhvsm6q5Hlfq34SpwsGriembr4OPEOAcMQ7LcO_l9JAJKuAYBLQD1xa_EeXC1ZIW8GNbE8849OHo1Zjc_bAQ2AEy5Wg2-UQ52fUWLEs_uE5oBWnEpKRC9JNIVIr9gErPZTq8OXH10b_ShLLTG1Q3xet0_s2RZNtrK5OX3S5GUHjSCZP_QP",
            rating = 4.9f,
            reviewCount = 12
        )
    )

    val sampleCategories = listOf(
        CategoryEntity(id = CATEGORY_CARPINTEROS_ID, name = "Carpinteros"),
        CategoryEntity(id = CATEGORY_PLOMEROS_ID, name = "Plomeros"),
        CategoryEntity(id = CATEGORY_ELECTRICISTAS_ID, name = "Electricistas"),
        CategoryEntity(id = CATEGORY_LIMPIEZA_ID, name = "Limpieza"),
        CategoryEntity(id = CATEGORY_TUTORIAS_ID, name = "Tutorías")
    )

    val sampleUserCategories = listOf(
        UserCategoryCrossRef(userId = DEMO_PROVIDER_USER_ID, categoryId = CATEGORY_ELECTRICISTAS_ID),
        UserCategoryCrossRef(userId = CHAT_LOCAL_USER_ID, categoryId = CATEGORY_CARPINTEROS_ID),
        UserCategoryCrossRef(userId = CHAT_REMOTE_USER_ID, categoryId = CATEGORY_PLOMEROS_ID)
    )

    val sampleConversations = listOf(
        ConversationEntity(
            id = CHAT_CONVERSATION_ID,
            participantAUserId = CHAT_LOCAL_USER_ID,
            participantBUserId = CHAT_REMOTE_USER_ID,
            lastMessagePreview = "Hola Carlos, necesito una cotizacion para limpieza.",
            lastMessageAt = System.currentTimeMillis() - 60_000
        ),
        ConversationEntity(
            id = REQUEST_DETAILS_CONVERSATION_ID,
            participantAUserId = CHAT_LOCAL_USER_ID,
            participantBUserId = REQUEST_DETAILS_CLIENT_ID,
            requestId = REQUEST_DETAILS_REQUEST_ID,
            lastMessagePreview = "Hola, ya quedo listo el equipo?",
            lastMessageAt = System.currentTimeMillis() - 30_000
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
            id = REQUEST_DETAILS_REQUEST_ID,
            clientUserId = REQUEST_DETAILS_CLIENT_ID,
            status = "Completado",
            title = "Reparacion Aire Acondicionado",
            date = "14 de Octubre, 2023 - 10:30 AM",
            applications = "2 Postulaciones",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqSS5a-xrrQHgeG8aJRjhsQjmDnhoVgSqV7xk7-tl8uWw91Us660y-5igqlR-FCKR9CtUdB_YyNZP6ezIqaznemYPwVGwKxWoBzNq2HUKzD4ibp1kS4_q5uTA9ehFv4Mrg_kEZXA5k7Ifx6PX1A6Q-O3F6d2yIBMOSUg3qScu4KvuHxzw2LwSXZkel36bsovDutGxTznsGPsVPIBGlC60wJ-rf9dWxtWtfvbCVhkl4Vtn1HhSo1onmosKKltJLoTYm31tu0dKBNsWv",
            buttonText = "Gestionar",
            isActive = false,
            description = "Se realizo la revision tecnica completa del sistema central. Se identifico fuga en el serpentin, se procedio a sellado y recarga de gas refrigerante R-410A. Limpieza profunda de filtros y drenaje incluida.",
            location = "Medellin, Antioquia",
            applicationCount = 2,
            isUrgent = false,
            budgetCop = 125000
        ),
        RequestEntity(
            status = "En curso",
            categoryId = CATEGORY_ELECTRICISTAS_ID,
            title = "Mantenimiento de Aire Acondicionado",
            date = "12 Oct 2023",
            applications = "3 Postulaciones",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB67969hdR51hZJ4MMl2KcD7Be19oLLeVWL6ROGoIgDcM5U8XflIO3tkfgCzwuH2MJSl_8_Gheu3jkhhVNTufiHwaXoHb7SQgsbVTt4ldPwE8EZWXv_CVNcD3c_ohEvLSEQECdZzXvKyJzDDMy7F2KJt6ksuHGcG9YPaLmghhRMZ-w50KFxTEAAEexjyZI93LFChiCnrTJaogs1fQs6Zg7JZew6NBm_Ul9K9ZD0v3ViKD5bnk4NnJMmmYK7kKDu899cW_Owf4R2VWgC",
            buttonText = "Gestionar",
            isActive = true,
            description = "Se requiere revisión técnica completa del sistema de aire acondicionado central.",
            location = "Bogotá, Chapinero",
            budgetCop = 180000,
            applicationCount = 3,
            isUrgent = true
        ),
        RequestEntity(
            status = "Pendiente",
            categoryId = CATEGORY_TUTORIAS_ID,
            title = "Clase Particular de Matemáticas",
            date = "10 Oct 2023",
            applications = "1 Postulación",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnl5p6ez-eg1L9zbqiPi776Iuugs_VciEqTLCanHi4PTptvUeBTYJuf-37hVntOaYzL8bDnsTTKRHFJwN13XDMvun57PjsABpdDapeKr5jXLYXSeSCOgbXTSjBlR0fXGcccOJv4v65jC0UpTk08VTtb8zFh-dMNEbmLlrK_d1oKkSCctoal7ERBV0fzFgeWYoZgzuJkqdx3GOQcCisgg-a5Q62_eZGbY52d-xyOXEzl-9nVrhRf83hHkWdJb6apj-WOF2Jhibnkhgr",
            buttonText = "Gestionar",
            isActive = true,
            description = "Necesito ayuda con cálculo vectorial y ecuaciones diferenciales.",
            location = "Bogotá, Usaquén",
            budgetCop = 90000,
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

    val sampleRequestAttachments = listOf(
        RequestAttachmentEntity(
            requestId = REQUEST_DETAILS_REQUEST_ID,
            uri = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqSS5a-xrrQHgeG8aJRjhsQjmDnhoVgSqV7xk7-tl8uWw91Us660y-5igqlR-FCKR9CtUdB_YyNZP6ezIqaznemYPwVGwKxWoBzNq2HUKzD4ibp1kS4_q5uTA9ehFv4Mrg_kEZXA5k7Ifx6PX1A6Q-O3F6d2yIBMOSUg3qScu4KvuHxzw2LwSXZkel36bsovDutGxTznsGPsVPIBGlC60wJ-rf9dWxtWtfvbCVhkl4Vtn1HhSo1onmosKKltJLoTYm31tu0dKBNsWv",
            sortOrder = 0
        ),
        RequestAttachmentEntity(
            requestId = REQUEST_DETAILS_REQUEST_ID,
            uri = "https://lh3.googleusercontent.com/aida-public/AB6AXuDhZBU2xxX1oJm7TDWGMbzXqkH12m4QfnmI8vRo5bu5qCy16P3F70yxnwPsZ1UQzfVNc_RcV6UR5nBVUGlCYoI21hnq_q4pMZXuZiiKa63eDka09BRZvq2vQllKMZ2r8g9_XuoSe88f_tCqiMXBiz-9vMK_l27P7zHkTSSA-0wauDkYIWWwZqQWMrd243CtvJnAZBRrO2nvbnF-_noXA1bbuwEOoVn_steHIOf249lWdMMyD4-Ic9-KWSTgCyXWmSpSTb61SOLAa5uZ",
            sortOrder = 1
        ),
        RequestAttachmentEntity(
            requestId = REQUEST_DETAILS_REQUEST_ID,
            uri = "https://lh3.googleusercontent.com/aida-public/AB6AXuBjAV7UadivynJLQcW0ZCie0huPCq5-NgOsEUKoIixEJv_cQx9QyFr0ZAeV84se42IPISvVAeVfST19okT8dhlxRd_SNrS0Ja0kAforSZ8ItFM1xxUEZGQ12UaaNekxVTioulr46maivNSO05w7naHieBStO7kQee2Vi5137VfqTxCn1xQMGKPitZAu9GclF6BF5ymob3ewjysuuFkdwCi2bKrb_5V9WJ4CSaCRT6chQXRkGX0vdXF-Kvp5rKXEfeOIqlpA6iq4u7VQ",
            sortOrder = 2
        )
    )

    val sampleNotifications = listOf(
        NotificationEntity(
            id = 8001,
            userId = NOTIFICATIONS_USER_ID,
            title = "Nueva postulacion recibida",
            description = "En el servicio: Reparacion de nevera en Bogota.",
            type = "APPLICATION",
            isUnread = true,
            createdAt = System.currentTimeMillis() - 5 * 60_000
        ),
        NotificationEntity(
            id = 8002,
            userId = NOTIFICATIONS_USER_ID,
            title = "Nuevo mensaje de Juan",
            description = "A que hora podrias venir a revisar el dano manana?",
            type = "MESSAGE",
            isUnread = false,
            createdAt = System.currentTimeMillis() - 15 * 60_000
        ),
        NotificationEntity(
            id = 8003,
            userId = NOTIFICATIONS_USER_ID,
            title = "Servicio confirmado",
            description = "Mantenimiento aire acondicionado ha sido agendado exitosamente.",
            type = "CONFIRMED",
            isUnread = false,
            createdAt = System.currentTimeMillis() - 60 * 60_000
        ),
        NotificationEntity(
            id = 8004,
            userId = NOTIFICATIONS_USER_ID,
            title = "Pago recibido",
            description = "Has recibido COP $45.000 por Limpieza General.",
            type = "PAYMENT",
            isUnread = false,
            createdAt = System.currentTimeMillis() - 3 * 60 * 60_000
        ),
        NotificationEntity(
            id = 8005,
            userId = NOTIFICATIONS_USER_ID,
            title = "Recordatorio de servicio",
            description = "Recuerda tu cita de manana a las 8:00 AM para Jardineria.",
            type = "REMINDER",
            isUnread = false,
            createdAt = System.currentTimeMillis() - 5 * 60 * 60_000
        )
    )

    val sampleServices = listOf(
        ServiceEntity(
            id = 8001,
            clientUserId = SERVICE_LOCAL_USER_ID,
            providerUserId = SERVICE_REMOTE_USER_ID,
            title = "Lavado de alfombras",
            summary = "Lavado profundo con secado rapido.",
            location = "Bogota, Chapinero",
            totalPriceCop = 85000,
            completedAt = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        ),
        ServiceEntity(
            id = 8002,
            clientUserId = SERVICE_LOCAL_USER_ID,
            providerUserId = SERVICE_REMOTE_USER_ID,
            title = "Reparacion electrica",
            summary = "Cambio de tomas y revision de cableado.",
            location = "Bogota, Usaquen",
            totalPriceCop = 120000,
            completedAt = System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        ),
        ServiceEntity(
            id = 8003,
            clientUserId = SERVICE_REMOTE_USER_ID,
            providerUserId = SERVICE_LOCAL_USER_ID,
            title = "Limpieza de vidrios",
            summary = "Limpieza exterior de ventanales.",
            location = "Bogota, Cedritos",
            totalPriceCop = 60000,
            completedAt = System.currentTimeMillis() - 25L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        ),
        ServiceEntity(
            id = 8004,
            clientUserId = SERVICE_REMOTE_USER_ID,
            providerUserId = SERVICE_LOCAL_USER_ID,
            title = "Paseo de mascotas",
            summary = "Dos salidas de 40 minutos con reporte.",
            location = "Bogota, Teusaquillo",
            totalPriceCop = 35000,
            completedAt = System.currentTimeMillis() - 35L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        ),
        ServiceEntity(
            id = 8010,
            clientUserId = DEMO_PROVIDER_USER_ID,
            providerUserId = SERVICE_REMOTE_USER_ID,
            title = "Instalacion de lamparas",
            summary = "Montaje de dos lamparas LED con verificacion de cableado.",
            location = "Bogota, Suba",
            totalPriceCop = 98000,
            completedAt = System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        ),
        ServiceEntity(
            id = 8011,
            clientUserId = SERVICE_REMOTE_USER_ID,
            providerUserId = DEMO_PROVIDER_USER_ID,
            title = "Mantenimiento de cerradura",
            summary = "Ajuste de cerradura principal y lubricacion preventiva.",
            location = "Bogota, Engativa",
            totalPriceCop = 70000,
            completedAt = System.currentTimeMillis() - 18L * 24 * 60 * 60 * 1000,
            status = "COMPLETED"
        )
    )

    val sampleServiceEvidence = listOf(
        ServiceEvidenceEntity(
            id = 8101,
            serviceId = 8001,
            imageUrl = "https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=300&h=300&fit=crop",
            sortOrder = 0
        ),
        ServiceEvidenceEntity(
            id = 8102,
            serviceId = 8002,
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=300&h=300&fit=crop",
            sortOrder = 0
        ),
        ServiceEvidenceEntity(
            id = 8103,
            serviceId = 8003,
            imageUrl = "https://images.unsplash.com/photo-1522163182402-834f871fd851?w=300&h=300&fit=crop",
            sortOrder = 0
        ),
        ServiceEvidenceEntity(
            id = 8110,
            serviceId = 8010,
            imageUrl = "https://images.unsplash.com/photo-1519710164239-da123dc03ef4?w=300&h=300&fit=crop",
            sortOrder = 0
        ),
        ServiceEvidenceEntity(
            id = 8111,
            serviceId = 8011,
            imageUrl = "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=300&h=300&fit=crop",
            sortOrder = 0
        )
    )

    val sampleReviews = listOf(
        ReviewEntity(
            id = 8201,
            serviceId = 8001,
            reviewerUserId = SERVICE_LOCAL_USER_ID,
            reviewedUserId = SERVICE_REMOTE_USER_ID,
            rating = 5,
            comment = "Excelente trabajo"
        ),
        ReviewEntity(
            id = 8202,
            serviceId = 8003,
            reviewerUserId = SERVICE_LOCAL_USER_ID,
            reviewedUserId = SERVICE_REMOTE_USER_ID,
            rating = 4,
            comment = "Todo correcto"
        ),
        ReviewEntity(
            id = 8210,
            serviceId = 8010,
            reviewerUserId = DEMO_PROVIDER_USER_ID,
            reviewedUserId = SERVICE_REMOTE_USER_ID,
            rating = 5,
            comment = "Trabajo impecable"
        )
    )
}
