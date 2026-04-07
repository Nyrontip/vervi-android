package com.example.verviapp.Domain.repository

import com.example.verviapp.Presentation.model.NotificationItem

/**
 * Interfaz para acceder y actualizar notificaciones.
 * Implementaciones pueden ser locales (in-memory), base de datos o red.
 */
interface NotificationsRepository {
    fun getAll(): List<NotificationItem>
    fun update(item: NotificationItem)
}