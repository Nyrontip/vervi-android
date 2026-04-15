package com.example.verviapp.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val bio: String = "",
    val location: String = "",
    val photoUrl: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val suggestedPriceCop: Long? = null,
    val isProvider: Boolean = false,
    val projectCount: Int = 0,
    val requestCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
