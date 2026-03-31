package com.example.verviapp.repository

import com.example.verviapp.model.RatingData

interface RatingsRepository {
    fun submitRating(rating: RatingData)
    fun getPending(): List<RatingData>
}

