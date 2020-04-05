package com.espresso.pbmobile.main.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
                nip = binding.nipText.text.toString(),
                regon = binding.regonText.text.toString(),
                address = AddressModel(
                    country = binding.countryText.text.toString(),
                    city = binding.cityText.text.toString(),
                    street = binding.streetText.text.toString(),
                    zipCode = binding.zipCodeText.text.toString()
                )
            )
            checkCompany(model)
        }
    }

    private fun finishActivity(model: CompanyModel) {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(PayActivity.RETURN_COMPANY_MODEL, model) })
        finish()
    }

    private fun checkCompany(model: CompanyModel) {
        model.apply {
            if (name.isNullOrEmpty() || nip.isNullOrEmpty() || regon.isNullOrEmpty() || address?.city.isNullOrEmpty() ||
                address?.street.isNullOrEmpty() || address?.country.isNullOrEmpty() || address?.zipCode.isNullOrEmpty()
            ) Toast.makeText(this@InsertCompanyDetailsActivity, "Proszę wypenij wszystkie dane", Toast.LENGTH_SHORT).show()
            else finishActivity(model)
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, InsertCompanyDetailsActivity::class.java)
    }
}
