package com.espresso.data.models.history

import com.espresso.data.models.refuel.RefuelProduct

data class RefuelHistoryModel(
    val id: Long,
    val quantity: Double,
    val points: Double,
    val dateRefueling: String,
    val product: RefuelProduct
)
