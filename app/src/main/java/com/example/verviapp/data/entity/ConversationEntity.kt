package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversations",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["participantAUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["participantBUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RequestEntity::class,
            parentColumns = ["id"],
            childColumns = ["requestId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["participantAUserId"]),
        Index(value = ["participantBUserId"]),
        Index(value = ["requestId"]),
        Index(value = ["serviceId"]),
        Index(value = ["lastMessageAt"])
    ]
)
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val participantAUserId: Int,
    val participantBUserId: Int,
    val requestId: Int? = null,
    val serviceId: Int? = null,
    val lastMessagePreview: String? = null,
    val lastMessageAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

