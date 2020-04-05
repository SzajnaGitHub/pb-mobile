package com.espresso.pbmobile.main.carwash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityConfirmReservationBinding

class ConfirmReservationActivity : AppCompatActivity() {

    private val binding by lazy { DataBindingUtil.setContentView<ActivityConfirmReservationBinding>(this, R.layout.activity_confirm_reservation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = intent.getSerializableExtra(MODEL) as ConfirmReservationModel
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, model: ConfirmReservationModel) = Intent(context, ConfirmReservationActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
