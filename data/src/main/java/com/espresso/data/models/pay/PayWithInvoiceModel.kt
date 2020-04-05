package com.espresso.data.models.pay

import com.espresso.data.models.company.CompanyModel

data class PayWithInvoiceModel(
    val userId: Long,
    val paymentMethod: String,
    val positionsOnReceipt: PositionsOnReceipt,
    val paymentTermDay: Int,
    val companyInfo: CompanyModel
)
