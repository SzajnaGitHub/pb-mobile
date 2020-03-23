package com.espresso.pbmobile.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivityHistoryBinding>(this, R.layout.activity_history) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupHistoryModel()
    }

    private fun setupHistoryModel() {
        val model = intent.getSerializableExtra(MODEL) as HistoryActivityViewModel
        binding.model = HistoryActivityViewModel(items = model.items, type = model.type)
    }

    companion object {
        private const val MODEL = "model"
        const val TYPE_CAR_WASH = "car_wash"
        const val TYPE_REFUEL = "refuel"
        fun createIntent(context: Context, model: HistoryActivityViewModel) = Intent(context, HistoryActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
