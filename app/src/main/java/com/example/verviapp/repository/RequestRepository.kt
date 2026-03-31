package com.example.verviapp.repository

import com.example.verviapp.model.RequestDetail

interface RequestRepository {
    suspend fun getRequest(requestId: String): RequestDetail
}

