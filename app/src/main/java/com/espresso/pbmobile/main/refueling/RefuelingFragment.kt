package com.espresso.pbmobile.main.refueling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.espresso.pbmobile.R

class RefuelingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_refueling, container, false)
    }

    companion object {
        fun createInstance() = RefuelingFragment()
    }
}
