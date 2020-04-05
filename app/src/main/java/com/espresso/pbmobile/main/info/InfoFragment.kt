package com.espresso.pbmobile.main.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.models.company.CompanyInfoRepository
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentInfoBinding
import io.reactivex.disposables.CompositeDisposable

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        getCompanyInfo()
        return binding.root
    }

    private fun getCompanyInfo() {
        CompanyInfoRepository.info()
            .doOnSubscribe{binding.loading = true}
            .doAfterTerminate { binding.loading = false }
            .subscribe { model ->
                binding.model = model
            }
            .let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun createInstance() = InfoFragment()
    }
}
