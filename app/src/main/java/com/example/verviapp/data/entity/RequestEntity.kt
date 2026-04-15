package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "requests",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientUserId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["clientUserId"]),
        Index(value = ["categoryId"]),
        Index(value = ["status"]),
        Index(value = ["isActive"]),
        Index(value = ["requiredDateMillis"])
    ]
)
data class RequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientUserId: Int? = null,
    val categoryId: Int? = null,
    val status: String,
    val title: String,
    val description: String = "",
    val date: String,
    val location: String = "",
    val budgetCop: Long? = null,
    val requiredDateMillis: Long? = null,
    val applications: String,
    val applicationCount: Int = 0,
    val imageUrl: String,
    val buttonText: String,
    val isUrgent: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val closedAt: Long? = null
)

