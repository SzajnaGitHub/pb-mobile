package com.espresso.pbmobile.admin.state

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.models.admin.StationStateModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityWarningPopupBinding

class WarningPopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityWarningPopupBinding>(this, R.layout.activity_warning_popup)
        binding.model = intent.getSerializableExtra(MODEL) as StationStateModel
        binding.checkStateButton.setOnClickListener {
            startActivity(StationStateActivity.createIntent(this))
            finish()
        }
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, model: StationStateModel) = Intent(context, WarningPopupActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
