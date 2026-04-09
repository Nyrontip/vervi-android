package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = ServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["reviewerUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["reviewedUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["serviceId"]),
        Index(value = ["reviewerUserId"]),
        Index(value = ["reviewedUserId"])
    ]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceId: Int,
    val reviewerUserId: Int,
    val reviewedUserId: Int,
    val rating: Int,
    val comment: String? = null,
    val evidenceImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

