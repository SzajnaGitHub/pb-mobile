package com.espresso.pbmobile.main.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRewardsBinding

class RewardsFragment : Fragment() {
    private lateinit var binding: FragmentRewardsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rewards, container, false)
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        binding.rewardsRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        }
    }

    companion object {
        fun createInstance() = RewardsFragment()
    }
}
