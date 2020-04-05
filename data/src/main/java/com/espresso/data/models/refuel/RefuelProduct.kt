package com.espresso.data.models.refuel

data class RefuelProduct(
    val id: Long,
    val productName: String,
    private val priceNetto: Double,
    val priceBrutto: Double,
    val category: String
)
