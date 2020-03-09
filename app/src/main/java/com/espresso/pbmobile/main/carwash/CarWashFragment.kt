package com.espresso.pbmobile.main.carwash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.espresso.pbmobile.R

class CarWashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car_wash, container, false)
    }

    companion object {
        fun createInstance() = CarWashFragment()
    }
}