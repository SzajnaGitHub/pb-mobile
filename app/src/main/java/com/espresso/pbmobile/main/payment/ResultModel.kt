package com.espresso.pbmobile.main.payment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ResultModel(
    val title: String,
    val product: String,
    val nip: String = "",
    val date: String,
    val cost: Double,
    val paymentMethod: String,
    val randomShortText: String = UUID.randomUUID().toString().substring(0, 6),
    val randomLongText: String = UUID.randomUUID().toString()
) : Parcelable
