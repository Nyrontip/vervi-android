package com.example.verviapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.verviapp.data.dao.ChatDao
import com.example.verviapp.data.dao.RequestDetailsDao
import com.example.verviapp.data.dao.NotificationDao
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.data.dao.ServiceDao
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.CategoryEntity
import com.example.verviapp.data.entity.ConversationEntity
import com.example.verviapp.data.entity.MessageEntity
import com.example.verviapp.data.entity.NotificationEntity
import com.example.verviapp.data.entity.RequestEntity
import com.example.verviapp.data.entity.RequestAttachmentEntity
import com.example.verviapp.data.entity.ReviewEntity
import com.example.verviapp.data.entity.ServiceApplicationEntity
import com.example.verviapp.data.entity.ServiceEntity
import com.example.verviapp.data.entity.ServiceEvidenceEntity
import com.example.verviapp.data.entity.UserCategoryCrossRef
import com.example.verviapp.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        UserCategoryCrossRef::class,
        RequestEntity::class,
        RequestAttachmentEntity::class,
        ServiceApplicationEntity::class,
        ServiceEntity::class,
        ServiceEvidenceEntity::class,
        ReviewEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestDao(): RequestDao
    abstract fun requestDetailsDao(): RequestDetailsDao
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun serviceDao(): ServiceDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        const val DATABASE_NAME = "vervi_app.db"
    }
}
