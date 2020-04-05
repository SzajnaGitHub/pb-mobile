package com.espresso.pbmobile.history

import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.carwash.CarWashItemModel.Companion.CAR_WASH
import com.espresso.pbmobile.main.carwash.CarWashItemModel.Companion.CAR_WASH_WAX
import java.io.Serializable

data class RefuelHistoryItemModel(
    val date: String,
    val fuelType: String,
    val cost: Double,
    val points: Int
) : Serializable

data class CarWashHistoryItemModel(
    val date: String,
    val hour: String,
    val type: String
) : Serializable {

    fun resolveImage() = when (type) {
        CAR_WASH -> R.drawable.ic_wash
        CAR_WASH_WAX -> R.drawable.ic_wax
        else -> R.drawable.car_wash
    }
}
