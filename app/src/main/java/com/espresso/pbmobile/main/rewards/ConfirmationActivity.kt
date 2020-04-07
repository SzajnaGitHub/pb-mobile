package com.espresso.pbmobile.main.rewards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityConfirmationBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private lateinit var store: Store
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirmation)
        binding.model = intent.getSerializableExtra(MODEL) as RewardsItemModel
        store = Store(this)
        binding.changeButton.setOnClickListener {
            getReward()
        }
    }

    private fun getReward() {
        binding.model?.id?.let {
            service.getReward(store.userId, it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Toast.makeText(this, getText(R.string.message_price_added), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .let(disposables::add)
        }
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, model: RewardsItemModel) = Intent(context, ConfirmationActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
