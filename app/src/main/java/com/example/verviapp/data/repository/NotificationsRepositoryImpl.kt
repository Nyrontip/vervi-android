package com.example.verviapp.data.repository

import com.example.verviapp.repository.NotificationsRepository
import com.example.verviapp.viewmodel.state.NotificationItem
import com.example.verviapp.viewmodel.state.NotificationType

/** Implementación sencilla in-memory para desarrollo y pruebas. */
class NotificationsRepositoryImpl : NotificationsRepository {

	private val items = mutableListOf(
        NotificationItem(
            title = "Nueva postulación recibida",
            description = "En el servicio: Reparación de n Bogotá.",
            time = "hace 5 min",
            type = NotificationType.APPLICATION,
            unread = true
        ),
        NotificationItem(
            title = "Nuevo mensaje de Juan",
            description = "¿A qué hora podrías venir a revisar el daño mañana?",
            time = "hace 15 min",
            type = NotificationType.MESSAGE,
            unread = false
        ),
        NotificationItem(
            title = "Servicio confirmado",
            description = "Mantenimiento aire acondicionado ha sido agendado exitosamente.",
            time = "hace 1 h",
            type = NotificationType.CONFIRMED,
            unread = false
        ),
        NotificationItem(
            title = "Pago recibido",
            description = "Has recibido COP $45.000 por Limpieza General.",
            time = "hace 3 h",
            type = NotificationType.PAYMENT,
            unread = false
        ),
        NotificationItem(
            title = "Recordatorio de servicio",
            description = "Recuerda tu cita de mañana a las 8:00 AM para Jardinería.",
            time = "hace 5 h",
            type = NotificationType.REMINDER,
            unread = false
        )
	)

	override fun getAll(): List<NotificationItem> = items.toList()

	override fun update(item: NotificationItem) {
		val index = items.indexOfFirst { it.id == item.id }
		if (index >= 0) {
			items[index] = item
		}
	}
}


