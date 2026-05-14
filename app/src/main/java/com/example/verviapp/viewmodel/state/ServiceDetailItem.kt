package com.example.verviapp.viewmodel.state

data class ServiceDetailItem(
    val serviceId: Int,
    val title: String,
    val summary: String,
    val location: String,
    val totalPriceText: String,
    val dateText: String,
    val statusText: String,
    val roleLabel: String,
    val counterpartName: String,
    val counterpartRatingText: String,
    val counterpartLocation: String,
    val counterpartAvatarUrl: String? = null,
    val evidenceImageUrls: List<String>,
    val imageUrl: String? = null,
    val chatSummaryText: String
)

