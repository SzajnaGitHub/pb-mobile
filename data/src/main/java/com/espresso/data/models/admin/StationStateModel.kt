package com.espresso.data.models.admin

import java.io.Serializable
import kotlin.math.roundToInt

data class StationStateModel(
    val idProduct: Long,
    val productName: String,
    val productQuantityPercent: Double
) : Serializable {
    fun percentValue() = productQuantityPercent.toInt()
    fun percentValueText() = "${productQuantityPercent.roundToInt()}%"
}
