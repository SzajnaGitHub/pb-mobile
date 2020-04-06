package com.espresso.pbmobile.admin

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R

typealias AdminOptionItemClick = () -> Unit

data class AdminOptionModel(
    val id: Long,
    val title: String,
    val clickHandler: AdminOptionItemClick
) {
    fun resolveImage(context: Context) = when (id) {
        1L -> ContextCompat.getDrawable(context, R.drawable.ic_monitoring)
        2L -> ContextCompat.getDrawable(context, R.drawable.station_lvl)
        3L -> ContextCompat.getDrawable(context, R.drawable.ic_change_prices)
        4L -> ContextCompat.getDrawable(context, R.drawable.admin_layalty)
        else -> ContextCompat.getDrawable(context, R.drawable.ic_monitoring)
    }
}
