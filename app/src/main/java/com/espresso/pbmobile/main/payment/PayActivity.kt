package com.espresso.pbmobile.main.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.espresso.data.RetrofitClientInstance
import com.espresso.data.models.company.CompanyModel
import com.espresso.data.models.pay.PayWithInvoiceModel
import com.espresso.data.models.pay.PayWithReceiptModel
import com.espresso.data.models.pay.PositionsOnReceipt
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityPayBinding
import com.espresso.pbmobile.main.company.InsertCompanyDetailsActivity
import com.espresso.pbmobile.main.refueling.RefuelItemDetailsModel
import com.espresso.pbmobile.utlis.RecyclerMarginDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.text.DateFormat
import java.util.*
import kotlin.random.Random

class PayActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivityPayBinding>(this, R.layout.activity_pay) }
    private val detailsModel: RefuelItemDetailsModel by lazy(LazyThreadSafetyMode.NONE) { intent.getParcelableExtra(MODEL) as RefuelItemDetailsModel }
    private val disposables = CompositeDisposable()
    private val service = RetrofitClientInstance.getInstance()
    private val currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().time)
    private lateinit var store: Store
    private var paymentMethodChoosed = false
    private var documentTypeChoosed = false
    private var actualPaymentItem: PaymentItemModel? = null
    private var actualDocumentItem: DocumentItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = Store(this)
    }

    override fun onResume() {
        super.onResume()
        setupRecycler()
        setupBinding()
    }

    private fun setupRecycler() {
        binding.paymentItems = listOf(
            PaymentItemModel(id = 1, title = "karta", image = R.drawable.ic_pay_card, clickHandler = ::paymentItemClickHandler),
            PaymentItemModel(id = 2, title = "blik", image = R.drawable.ic_pay_blik, clickHandler = ::paymentItemClickHandler),
            PaymentItemModel(id = 3, title = "przelew", image = R.drawable.ic_pay_transfer, clickHandler = ::paymentItemClickHandler)
        )
        binding.documentItems = listOf(
            DocumentItemModel(id = 1, title = "paragon", clickHandler = ::documentItemClickHandler),
            DocumentItemModel(id = 2, title = "faktura", clickHandler = ::documentItemClickHandler)
        )
        binding.documentTypeRecycler.apply {
            addItemDecoration(RecyclerMarginDecorator(this@PayActivity))
        }
        binding.paymentTypeRecycler.layoutManager = GridLayoutManager(this@PayActivity, 3, GridLayoutManager.VERTICAL, false)

    }

    private fun paymentItemClickHandler(model: PaymentItemModel) {
        actualPaymentItem = model
        paymentMethodChoosed = true
        binding.paymentItems?.map {
            it.copy(isClicked = it.id == model.id)
        }.let(binding::setPaymentItems)
    }

    private fun documentItemClickHandler(model: DocumentItemModel) {
        actualDocumentItem = model
        documentTypeChoosed = true
        binding.documentItems?.map {
            it.copy(isClicked = it.id == model.id)
        }.let(binding::setDocumentItems)
    }

    private fun setupBinding() {
        binding.payButton.setOnClickListener {
            if (documentTypeChoosed && paymentMethodChoosed) {
                when (actualDocumentItem?.id) {
                    1 -> handleReceipt()
                    2 -> checkUserCompany()
                }
            }
        }
    }

    private fun handleReceipt() {
        val receiptModel = PayWithReceiptModel(
            userId = store.userId,
            paymentMethod = actualPaymentItem?.title ?: "",
            positionsOnReceipt = PositionsOnReceipt(
                productId = detailsModel.id,
                quantity = detailsModel.capacity
            )
        )
        service.payWithReceipt(receiptModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                handlePayment("Paragon")
            }
            .let(disposables::add)
    }

    private fun handleInvoice(companyModel: CompanyModel) {
        val invoiceModel = PayWithInvoiceModel(
            userId = store.userId,
            paymentTermDay = Random.nextInt(7, 23),
            positionsOnReceipt = PositionsOnReceipt(
                productId = detailsModel.id,
                quantity = detailsModel.capacity
            ),
            paymentMethod = actualPaymentItem?.title ?: "",
            companyInfo = companyModel
        )
        service.payWithInvoice(invoiceModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                handlePayment("Faktura", nip = companyModel.nip)
            }
            .let(disposables::add)
    }

    private fun checkUserCompany() {
        service.getUserCompany(store.userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ company ->
                println("TEKST COMPANY $company")
                if (company.nip == "null") addUserCompany()
                else handleInvoice(company)
            }, {
                handleCompanyError(it)
            })
            .let(disposables::add)
    }

    private fun handleCompanyError(throwable: Throwable) {
        val error = throwable as HttpException
        val jsonResponse = error.response().errorBody()?.string()?.let { JSONObject(it) }
        when (jsonResponse?.optInt("errorNumber")) {
            40004 -> {
                println("TEKST HANDLED ERROR")
                addUserCompany()
            }
            else -> {
                println("TEKST ${jsonResponse?.optInt("errorNumber")}")
                println("TEKST THROW")
                //throw throwable
            }
        }
    }

    private fun addUserCompany() {
        startActivityForResult(InsertCompanyDetailsActivity.createIntent(this), INSERT_COMPANY_DETAILS_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INSERT_COMPANY_DETAILS_CODE && resultCode == Activity.RESULT_OK) {
            handleInvoice(data?.getParcelableExtra(RETURN_COMPANY_MODEL) as CompanyModel)
            println("TEKST OK")
        }
    }

    private fun handlePayment(title: String, nip: String = "") {
        startActivity(
            PaymentResultActivity.createIntent(
                this, ResultModel(
                    title = title,
                    nip = nip,
                    paymentMethod = actualPaymentItem?.title ?: "Brak danych",
                    cost = detailsModel.price,
                    product = detailsModel.product,
                    date = currentDate
                )
            )
        )
        finish()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        const val RETURN_COMPANY_MODEL = "company model"
        private const val MODEL = "model"
        private const val INSERT_COMPANY_DETAILS_CODE = 1
        fun createIntent(context: Context, detailsModel: RefuelItemDetailsModel) = Intent(context, PayActivity::class.java).apply {
            putExtra(MODEL, detailsModel)
        }
    }
}
