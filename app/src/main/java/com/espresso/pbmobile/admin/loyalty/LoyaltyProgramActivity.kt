package com.espresso.pbmobile.admin.loyalty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.models.loyaltyproducts.LoyaltyProductModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityLoyaltyProgramBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoyaltyProgramActivity : AppCompatActivity() {
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()
    private var loyaltyProducts: List<LoyaltyProductModel> = listOf()
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityLoyaltyProgramBinding>(this, R.layout.activity_loyalty_program)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLoyaltyProducts()
        binding.changePointsButton.setOnClickListener {
            updateProducts()
        }
    }

    private fun getLoyaltyProducts() {
        service.getLoyaltyProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { products ->
                loyaltyProducts = products
                binding.items = products
            }
            .let(disposables::add)
    }

    private fun updateProducts() {
        val loyaltyItems: List<Int?>
        try {
            loyaltyItems = listOf(
                binding.textGas.text.toString().toIntOrNull(),
                binding.textLpg.text.toString().toIntOrNull(),
                binding.textCarWash.text.toString().toIntOrNull(),
                binding.textCarWashWax.text.toString().toIntOrNull()
            )
        } catch (e: NumberFormatException) {
            Toast.makeText(this, getString(R.string.message_wrong_input_data), Toast.LENGTH_SHORT).show()
            return
        }

        loyaltyItems.forEach {
            if (it == null || it <= 0) {
                Toast.makeText(this, getString(R.string.message_wrong_input_data), Toast.LENGTH_SHORT).show()
                return
            }
        }
        loyaltyProducts.forEachIndexed { index, product ->
            loyaltyItems[index]?.let { product.copy(points = it) }?.let {
                service.updateLoyaltyProduct(it.product.id, it.points)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { binding.loading = true }
                    .doAfterTerminate { binding.loading = false }
                    .subscribe {
                        Toast.makeText(this, getString(R.string.message_products_changed_succesfully), Toast.LENGTH_LONG).show()
                    }
                    .let(disposables::add)
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, LoyaltyProgramActivity::class.java)
    }
}
