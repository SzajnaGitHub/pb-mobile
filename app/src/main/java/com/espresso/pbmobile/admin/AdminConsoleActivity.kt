package com.espresso.pbmobile.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.admin.cctv.MonitoringActivity
import com.espresso.pbmobile.admin.gas.GasPriceActivity
import com.espresso.pbmobile.admin.loyalty.LoyaltyProgramActivity
import com.espresso.pbmobile.admin.state.StationStateActivity
import com.espresso.pbmobile.databinding.ActivityAdminConsoleBinding

class AdminConsoleActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityAdminConsoleBinding>(this, R.layout.activity_admin_console)
    }
    private val store by lazy { Store(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.isOwner = store.userType == UserProfile.TYPE_OWNER
        binding.cctvModel = AdminOptionModel(1, getString(R.string.title_cctv), ::handleCCTVClick)
        binding.stationStateModel = AdminOptionModel(2, getString(R.string.title_station_state), ::handleStationStateClick)
        binding.gasPriceModel = AdminOptionModel(3, getString(R.string.title_gas_prices), ::handleGasPriceClick)
        binding.loyaltyProductsModel = AdminOptionModel(4, getString(R.string.title_loyalty_program), ::handleLoyaltyProductsClick)
    }

    private fun handleCCTVClick() {
        startActivity(MonitoringActivity.createIntent(this))
    }

    private fun handleStationStateClick() {
        startActivity(StationStateActivity.createIntent(this))
    }

    private fun handleGasPriceClick() {
        startActivity(GasPriceActivity.createIntent(this))
    }

    private fun handleLoyaltyProductsClick() {
        startActivity(LoyaltyProgramActivity.createIntent(this))
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, AdminConsoleActivity::class.java)
    }
}