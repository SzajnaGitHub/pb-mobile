package com.espresso.pbmobile.main.refueling

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R

typealias RefuelItemClickHandler = (RefuelItemModel) -> Unit

data class RefuelItemModel(
    val isClicked: Boolean = false,
    val name: String,
    val id: Long,
    val pricePerUnit: Double,
    val clickHandler: RefuelItemClickHandler
) {
    fun refuelImageResolver(context: Context) = when (id) {
        ITEM_REFUEL_95 -> ContextCompat.getDrawable(context, R.drawable.r95)
        ITEM_REFUEL_98 -> ContextCompat.getDrawable(context, R.drawable.r98)
        ITEM_REFUEL_ON -> ContextCompat.getDrawable(context, R.drawable.ron)
        ITEM_REFUEL_LPG -> ContextCompat.getDrawable(context, R.drawable.rlpg)
        else -> ContextCompat.getDrawable(context, R.drawable.r95)
    }

    companion object {
        private fun resolveId(id: Long) = when (id) {
            1L -> ITEM_REFUEL_ON
            2L -> ITEM_REFUEL_95
            3L -> ITEM_REFUEL_98
            4L -> ITEM_REFUEL_LPG
            else -> -1
        }

        fun create(name: String, value: Double, clickHandler: RefuelItemClickHandler, id: Long) = RefuelItemModel(
            name = name,
            pricePerUnit = value,
            clickHandler = clickHandler,
            id = resolveId(id)
        )

        const val ITEM_REFUEL_ON = 1L
        const val ITEM_REFUEL_95 = 2L
        const val ITEM_REFUEL_98 = 3L
        const val ITEM_REFUEL_LPG = 4L
    }
}
