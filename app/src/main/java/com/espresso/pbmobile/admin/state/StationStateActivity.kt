package com.espresso.pbmobile.admin.state

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.models.admin.StationStateModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityStationStateBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StationStateActivity : AppCompatActivity() {
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()
    private val binding by lazy { DataBindingUtil.setContentView<ActivityStationStateBinding>(this, R.layout.activity_station_state) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBindings()
    }

    override fun onResume() {
        super.onResume()
        getStationState()
    }

    private fun setBindings() {
        binding.apply {
            seekBar95.isEnabled = false
            seekbar98.isEnabled = false
            seekbarLpg.isEnabled = false
            seekbarOn.isEnabled = false

            textOn.setOnClickListener { handleItemClick(binding?.items?.get(0)) }
            text95.setOnClickListener { handleItemClick(binding?.items?.get(1)) }
            text98.setOnClickListener { handleItemClick(binding?.items?.get(2)) }
            textLpg.setOnClickListener { handleItemClick(binding?.items?.get(3)) }
        }
    }

    private fun getStationState() {
        service.getStationState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                binding.items = items
            }
            .let(disposables::add)
    }

    private fun handleItemClick(model: StationStateModel?) {
        startActivity(model?.let { OrderProductActivity.createIntent(this, it) })
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, StationStateActivity::class.java)
    }
}
