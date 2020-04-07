package com.espresso.pbmobile.main.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.models.company.CompanyInfoRepository
import com.espresso.data.models.company.CompanyModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityInvoiceResultBinding
import io.reactivex.disposables.CompositeDisposable

class InvoiceResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceResultBinding
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice_result)
        getCompanyInfo()
        binding.model = intent.getParcelableExtra(RESULT_MODEL) as ResultModel
        binding.buyerModel = intent.getParcelableExtra(BUYER_COMPANY_MODEL) as CompanyModel
    }

    private fun getCompanyInfo() {
        CompanyInfoRepository.info()
            .doOnSubscribe { binding.loading = true }
            .doAfterTerminate { binding.loading = false }
            .subscribe { model ->
                binding.sellerModel = model
            }
            .let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    companion object {
        private const val RESULT_MODEL = "model"
        private const val BUYER_COMPANY_MODEL = "comp_model"
        fun createIntent(context: Context, buyerModel: CompanyModel, resultModel: ResultModel) = Intent(context, InvoiceResultActivity::class.java).apply {
            putExtra(RESULT_MODEL, resultModel)
            putExtra(BUYER_COMPANY_MODEL, buyerModel)
        }
    }
}
