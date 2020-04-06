package com.espresso.data.models.loyaltyproducts

import com.espresso.data.models.refuel.RefuelProduct

data class LoyaltyProductModel(
    val id: Long,
    val points: Int,
    val product: RefuelProduct
)
