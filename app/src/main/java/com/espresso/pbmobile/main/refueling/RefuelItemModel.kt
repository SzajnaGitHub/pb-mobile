package com.espresso.pbmobile.main.refueling

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R

data class RefuelItemModel(
    val isClicked: Boolean = false,
    val text: String,
    val id: Long,
    val pricePerUnit: Double,
    val clickHandler: (RefuelItemModel) -> Unit
) {
    fun refuelImageResolver(context: Context) = when (id) {
        ITEM_REFUEL_95 -> ContextCompat.getDrawable(context, R.drawable.r95)
        ITEM_REFUEL_98 -> ContextCompat.getDrawable(context, R.drawable.r98)
        ITEM_REFUEL_ON -> ContextCompat.getDrawable(context, R.drawable.ron)
        ITEM_REFUEL_LPG -> ContextCompat.getDrawable(context, R.drawable.rlpg)
        else -> ContextCompat.getDrawable(context, R.drawable.r95)
    }

    companion object {
        const val ITEM_REFUEL_95 = 11111111L
        const val ITEM_REFUEL_98 = 22222222L
        const val ITEM_REFUEL_ON = 33333333L
        const val ITEM_REFUEL_LPG = 44444444L
    }
}
