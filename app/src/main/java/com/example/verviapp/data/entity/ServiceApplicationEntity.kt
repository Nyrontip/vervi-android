package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_applications",
    foreignKeys = [
        ForeignKey(
            entity = RequestEntity::class,
            parentColumns = ["id"],
            childColumns = ["requestId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["providerUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["requestId"]),
        Index(value = ["providerUserId"]),
        Index(value = ["status"])
    ]
)
data class ServiceApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val requestId: Int,
    val providerUserId: Int,
    val presentationMessage: String,
    val proposedPriceCop: Long? = null,
    val evidenceUri: String? = null,
    val immediateAvailability: Boolean = false,
    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

