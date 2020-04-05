package com.espresso.data.models.pay

import java.io.Serializable

data class PayWithReceiptModel(
    val userId: Long,
    val paymentMethod: String,
    val positionsOnReceipt: PositionsOnReceipt
) : Serializable

data class PositionsOnReceipt(
    val productId: Long,
    val quantity: Double
) : Serializable
