package com.espresso.pbmobile.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityMainBinding
import com.espresso.pbmobile.main.carwash.CarWashFragment
import com.espresso.pbmobile.main.dashboard.DashboardFragment
import com.espresso.pbmobile.main.info.InfoFragment
import com.espresso.pbmobile.main.refueling.RefuelingFragment
import com.espresso.pbmobile.main.rewards.RewardsFragment

class MainActivity : AppCompatActivity(), DashboardFragment.Delegate{
    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }
    private lateinit var store: Store
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = Store(this)
        handleFragmentChange(DashboardFragment.createInstance(), DashboardFragment::class.java.name)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val isRegistered = store.userType != UserProfile.TYPE_UNREGISTERED
        binding.bottomNav.apply {
            if (isRegistered) {
                inflateMenu(R.menu.bottom_navigation_menu)
            } else {
                inflateMenu(R.menu.botom_navigation_menu_for_guests)
            }
            selectedItemId = R.id.dashboard
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.refuel -> handleFragmentChange(RefuelingFragment.createInstance(), RefuelingFragment::class.java.name)
                    R.id.car_wash -> if(isRegistered) handleFragmentChange(CarWashFragment.createInstance(), CarWashFragment::class.java.name) else false
                    R.id.dashboard -> handleFragmentChange(DashboardFragment.createInstance(), DashboardFragment::class.java.name)
                    R.id.rewards -> if(isRegistered) handleFragmentChange(RewardsFragment.createInstance(), RewardsFragment::class.java.name) else false
                    R.id.info -> handleFragmentChange(InfoFragment.createInstance(), InfoFragment::class.java.name)
                    else -> throw IllegalArgumentException(ILLEGAL_BOTTOM_ARGUMENT_EXCEPTION_TEXT)
                }
            }
        }
    }

    private fun handleFragmentChange(fragment: Fragment, tag: String): Boolean {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, tag).commit()
        return true
    }

    override fun openRefuelingFragment() {
        binding.bottomNav.selectedItemId = R.id.refuel
    }

    override fun openPointsFragment() {
        binding.bottomNav.selectedItemId = R.id.rewards
    }

    override fun openCarWashFragment() {
        binding.bottomNav.selectedItemId = R.id.car_wash
    }

    companion object {
        private const val ILLEGAL_BOTTOM_ARGUMENT_EXCEPTION_TEXT = "WRONG BOTTOM NAVIGATION ID"
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
