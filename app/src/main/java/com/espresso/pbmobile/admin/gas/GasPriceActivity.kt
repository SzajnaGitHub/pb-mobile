package com.espresso.pbmobile.admin.gas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.models.refuel.RefuelProduct
import com.espresso.data.models.refuel.RefuelProductsRepo
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityGasPriceBinding
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GasPriceActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityGasPriceBinding>(this, R.layout.activity_gas_price)
    }
    private var gasItems: List<RefuelProduct> = listOf()
    private val service = RetrofitClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGasPrices()
        setupBindings()
    }

    private fun setupBindings() {
        binding.changePriceButton.clicks()
            .subscribe {
                updatePrices()
            }
            .let(disposables::add)
    }

    private fun getGasPrices() {
        RefuelProductsRepo.products(fromService = true)
            .map {
                it.filter { product -> product.category == FILTER_PREDICATE }
            }
            .doOnSubscribe { binding.loading = true }
            .doAfterTerminate { binding.loading = false }
            .subscribe { items ->
                gasItems = items
                binding.items = items
            }
            .let(disposables::add)
    }

    private fun updatePrices() {
        var gasPricesItems: List<Double?>
        try {
            gasPricesItems = listOf(
                binding.textOn.text.toString().toDoubleOrNull(),
                binding.text95.text.toString().toDoubleOrNull(),
                binding.text98.text.toString().toDoubleOrNull(),
                binding.textLpg.text.toString().toDoubleOrNull()
            )
        } catch (e: NumberFormatException) {
            Toast.makeText(this, getString(R.string.message_wrong_input_data), Toast.LENGTH_SHORT).show()
            return
        }

        gasPricesItems.forEach {
            if (it == null || it == 0.0) {
                Toast.makeText(this, getString(R.string.message_wrong_input_data), Toast.LENGTH_SHORT).show()
                return
            }
        }

        gasItems.forEachIndexed { index, product ->
            gasPricesItems[index]?.let { product.copy(priceBrutto = it) }?.let {
                service.updateProduct(product.id, it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { binding.loading = true }
                    .doAfterTerminate { binding.loading = false }
                    .subscribe {
                        Toast.makeText(this, getString(R.string.message_prices_changed_succesfully), Toast.LENGTH_LONG).show()
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
        private const val FILTER_PREDICATE = "Petrol"
        fun createIntent(context: Context) = Intent(context, GasPriceActivity::class.java)
    }
}
