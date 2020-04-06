package com.espresso.data.models.pay

data class PayWithReceiptModel(
    val userId: Long,
    val paymentMethod: String,
    val positionsOnReceipt: PositionsOnReceipt
)

data class PositionsOnReceipt(
    val productId: Long,
    val quantity: Double
)
