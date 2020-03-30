package com.espresso.pbmobile.main.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityPaymentResultBinding

class PaymentResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPaymentResultBinding>(this, R.layout.activity_payment_result)
        binding.model = intent.getParcelableExtra(MODEL) as ResultModel
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, resultModel: ResultModel) = Intent(context, PaymentResultActivity::class.java).apply {
            putExtra(MODEL, resultModel)
        }
    }
}
