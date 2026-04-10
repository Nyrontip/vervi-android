package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verviapp.data.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query(
        """
        SELECT * FROM notifications
        WHERE userId = :userId
        ORDER BY createdAt DESC
        """
    )
    fun observeNotifications(userId: Int): Flow<List<NotificationEntity>>

    @Query(
        """
        SELECT * FROM notifications
        WHERE userId = :userId AND isUnread = 1
        ORDER BY createdAt DESC
        """
    )
    fun observeUnreadNotifications(userId: Int): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isUnread = 0 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Int)

    @Query("UPDATE notifications SET isUnread = 0 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Int)
}

