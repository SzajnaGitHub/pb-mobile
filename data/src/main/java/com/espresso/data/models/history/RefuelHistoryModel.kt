package com.espresso.data.models.history

import com.espresso.data.models.refuel.RefuelProduct
import java.util.*

data class RefuelHistoryModel(
    val id: Long,
    val quantity: Double,
    val dateRefueling: Date,
    val product: RefuelProduct
)
