package com.example.verviapp.repository

import com.example.verviapp.model.ServiceHistoryItem

interface ServiceHistoryRepository {
    suspend fun getHistory(): List<ServiceHistoryItem>
}

