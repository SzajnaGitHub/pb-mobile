package com.espresso.pbmobile.history

import com.espresso.pbmobile.R
import com.espresso.pbmobile.history.HistoryActivity.Companion.TYPE_CAR_WASH
import com.espresso.pbmobile.history.HistoryActivity.Companion.TYPE_REFUEL
import java.io.Serializable

data class HistoryActivityViewModel(
    val items: List<Any>,
    val type: String
) : Serializable {

    fun getLayoutId() = when (type) {
        TYPE_REFUEL -> R.layout.item_history_refuel
        TYPE_CAR_WASH -> R.layout.item_history_car_wash
        else -> R.layout.item_history_refuel
    }
}
