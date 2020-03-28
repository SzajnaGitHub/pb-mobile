package com.espresso.pbmobile.main.refueling

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_95
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_98
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_LPG
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_ON

data class RefuelFragmentViewModel(
    private val initialRefuelValue: Int,
    private val activeItem: RefuelItemModel? = null,
    private val state: FuelingState = FuelingState.INACTION,
    val detailsModel: RefuelItemDetailsModel = RefuelItemDetailsModel(),
    val isRegistered: Boolean
) {

    val fuelButtonEnabled = state != FuelingState.FULL_TANK


    fun refuelImageResolver(context: Context) = when (activeItem?.id) {
        ITEM_REFUEL_95 -> ContextCompat.getDrawable(context, R.drawable.gun_95)
        ITEM_REFUEL_98 -> ContextCompat.getDrawable(context, R.drawable.gun_98)
        ITEM_REFUEL_ON -> ContextCompat.getDrawable(context, R.drawable.gun_on)
        ITEM_REFUEL_LPG -> ContextCompat.getDrawable(context, R.drawable.gun_lpg)
        else -> null
    }

    companion object {
        const val REFUEL_MAX_VALUE = 100
    }
}
