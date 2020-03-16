package com.espresso.pbmobile.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.RetrofitClientInstance
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityMainBinding
import com.espresso.pbmobile.main.carwash.CarWashFragment
import com.espresso.pbmobile.main.dashboard.DashboardFragment
import com.espresso.pbmobile.main.info.InfoFragment
import com.espresso.pbmobile.main.refueling.RefuelingFragment
import com.espresso.pbmobile.main.rewards.RewardsFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClientInstance()
        handleFragmentChange(DashboardFragment.createInstance(), DashboardFragment::class.java.name)
        binding.bottomNav.selectedItemId = R.id.dashboard
        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.refuel -> handleFragmentChange(RefuelingFragment.createInstance(), RefuelingFragment::class.java.name)
                R.id.car_wash -> handleFragmentChange(CarWashFragment.createInstance(), CarWashFragment::class.java.name)
                R.id.dashboard -> handleFragmentChange(DashboardFragment.createInstance(), DashboardFragment::class.java.name)
                R.id.rewards -> handleFragmentChange(RewardsFragment.createInstance(), RewardsFragment::class.java.name)
                R.id.info -> handleFragmentChange(InfoFragment.createInstance(), InfoFragment::class.java.name)
                else -> throw IllegalArgumentException(ILLEGAL_BOTTOM_ARGUMENT_EXCEPTION_TEXT)
            }
        }
    }

    private fun handleFragmentChange(fragment: Fragment, tag: String): Boolean {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, tag).commit()
        return true
    }

    companion object {
        private const val ILLEGAL_BOTTOM_ARGUMENT_EXCEPTION_TEXT = "WRONG BOTTOM NAVIGATION ID"
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
