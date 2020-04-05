package com.espresso.pbmobile.main.carwash

import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.carwash.CarWashItemModel.Companion.CAR_WASH
import com.espresso.pbmobile.main.carwash.CarWashItemModel.Companion.CAR_WASH_WAX
import java.io.Serializable

data class ConfirmReservationModel(
    val carWashType: String,
    val date: String,
    val hour: String
) : Serializable {

    fun resolveImage() = when (carWashType) {
        CAR_WASH -> R.drawable.ic_rewards_car_was
        CAR_WASH_WAX -> R.drawable.ic_rewards_car_was_wax
        else -> R.drawable.ic_rewards_car_was
    }
}
