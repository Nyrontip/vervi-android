package com.example.verviapp.repository

import com.example.verviapp.model.RatingData

class RatingsRepositoryImpl : RatingsRepository {

    private val pending = mutableListOf<RatingData>()

    override fun submitRating(rating: RatingData) {
        // Simular envío agregándolo a pending
        pending.add(rating)
    }

    override fun getPending(): List<RatingData> = pending.toList()
}

