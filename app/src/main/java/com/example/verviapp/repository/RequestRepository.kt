package com.example.verviapp.repository

import com.example.verviapp.model.RequestItem

interface RequestRepository {
    suspend fun getRequests(activeOnly: Boolean? = null): List<RequestItem>
}

