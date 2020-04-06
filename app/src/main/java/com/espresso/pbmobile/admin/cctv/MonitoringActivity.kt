package com.espresso.pbmobile.admin.cctv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityMonitoringBinding

class MonitoringActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMonitoringBinding>(this, R.layout.activity_monitoring)
        binding.viewPager.adapter = MonitoringPagerAdapter(this)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MonitoringActivity::class.java)
    }
}
