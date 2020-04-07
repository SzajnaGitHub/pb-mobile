package com.espresso.pbmobile.main.rewards

import android.content.Context
import androidx.core.content.ContextCompat
import com.espresso.pbmobile.R
import java.io.Serializable

typealias RewardItemClickHandler = (RewardsItemModel) -> Unit

data class RewardsItemModel(
    val id: Long,
    val title: String,
    val points: Int,
    val clickHandler: RewardItemClickHandler? = null
) : Serializable {
    fun resolveImage(context: Context) = when (id) {
        PETROL_ID -> ContextCompat.getDrawable(context, R.drawable.ic_rewards_gas)
        LPG_ID -> ContextCompat.getDrawable(context, R.drawable.ic_rewards_lpg)
        CAR_WASH_ID -> ContextCompat.getDrawable(context, R.drawable.ic_rewards_car_was)
        CAR_WASH_WAX_ID -> ContextCompat.getDrawable(context, R.drawable.ic_rewards_car_was_wax)
        else -> null
    }

    companion object {
        private const val PETROL_ID = 1L
        private const val LPG_ID = 2L
        private const val CAR_WASH_ID = 3L
        private const val CAR_WASH_WAX_ID = 4L
    }
}
