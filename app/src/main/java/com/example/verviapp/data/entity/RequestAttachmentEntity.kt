package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "request_attachments",
    foreignKeys = [
        ForeignKey(
            entity = RequestEntity::class,
            parentColumns = ["id"],
            childColumns = ["requestId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["requestId"])]
)
data class RequestAttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val requestId: Int,
    val uri: String,
    val mimeType: String? = null,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

