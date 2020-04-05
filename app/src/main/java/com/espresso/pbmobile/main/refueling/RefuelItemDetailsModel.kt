package com.espresso.pbmobile.main.refueling

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefuelItemDetailsModel(
    val product: String = "",
    val id: Long = 0L,
    val pricePerUnit: Double = 0.0,
    val capacity: Double = 0.0,
    val price: Double = 0.0,
    val percentageValue: Int = 0
) : Parcelable {
    @IgnoredOnParcel
    val percentageText = "$percentageValue%"
}
