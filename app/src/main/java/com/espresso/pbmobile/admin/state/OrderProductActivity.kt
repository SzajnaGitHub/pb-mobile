package com.espresso.pbmobile.admin.state

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.models.admin.StationStateModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityOrderProductBinding
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OrderProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderProductBinding
    private val disposables = CompositeDisposable()
    private val service = RetrofitClient.getInstance()
    private val model by lazy { intent.getSerializableExtra(MODEL) as StationStateModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_product)
        binding.model = model
        binding.productNameText.setBackgroundResource(resolveImage(model.idProduct))
        setupBindings()
    }

    private fun setupBindings() {
        binding.orderGasButton.clicks()
            .subscribe { orderGas() }
            .let(disposables::add)
    }

    private fun orderGas() {
        service.orderGas(model.idProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{binding.loading = true}
            .doAfterTerminate { binding.loading = false }
            .subscribe {
                Toast.makeText(this, "${model.productName}: zostało zamówione", Toast.LENGTH_SHORT).show()
                finish()
            }
            .let(disposables::add)
    }

    fun resolveImage(idProduct: Long) = when (idProduct) {
        1L -> R.drawable.thumb_black_seekbar
        2L -> R.drawable.thumb_green_seekbar
        3L -> R.drawable.thumb_blue_seekbar
        4L -> R.drawable.thumb_yellow_seekbar
        else -> R.drawable.thumb_black_seekbar
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, model: StationStateModel) = Intent(context, OrderProductActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
