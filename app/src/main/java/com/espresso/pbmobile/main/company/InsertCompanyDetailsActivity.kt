package com.espresso.pbmobile.main.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.models.address.AddressModel
import com.espresso.data.models.company.CompanyModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityInsertCompanyDetailsBinding
import com.espresso.pbmobile.main.payment.PayActivity

class InsertCompanyDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityInsertCompanyDetailsBinding>(this, R.layout.activity_insert_company_details)
        binding.addButton.setOnClickListener {
            val model = CompanyModel(
                name = binding.companyNameText.text.toString(),
                nip = binding.nipText.toString(),
                regon = binding.regonText.toString(),
                address = AddressModel(
                    country = binding.countryText.toString(),
                    city = binding.cityText.toString(),
                    street = binding.streetText.toString(),
                    zipCode = binding.zipCodeText.toString()
                )
            )
            setResult(Activity.RESULT_OK, Intent().apply { putExtra(PayActivity.RETURN_COMPANY_MODEL, model) })
            finish()
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, InsertCompanyDetailsActivity::class.java)
    }
}
