package com.espresso.pbmobile.main.carwash

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R

typealias CarWashItemClickHandler = (CarWashItemModel) -> Unit

data class CarWashItemModel(
    val id: Long,
    val title: String,
    val clickHandler: CarWashItemClickHandler,
    val clicked: Boolean = false
) {
    fun resolveImage(context: Context) = when (id) {
        1L -> ContextCompat.getDrawable(context, R.drawable.car_wash)
        2L -> ContextCompat.getDrawable(context, R.drawable.car_wash_wax)
        else -> ContextCompat.getDrawable(context, R.drawable.car_wash)
    }

    companion object {
        const val CAR_WASH = "Myjnia"
        const val CAR_WASH_WAX = "Myjnia z woskiem"
    }
}
