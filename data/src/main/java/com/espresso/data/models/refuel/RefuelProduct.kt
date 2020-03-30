package com.espresso.data.models.refuel

data class RefuelProduct(
    private val priceNetto: Double,
    val priceBrutto: Double,
    val category: String,
    val id: Long,
    val productName: String
)
