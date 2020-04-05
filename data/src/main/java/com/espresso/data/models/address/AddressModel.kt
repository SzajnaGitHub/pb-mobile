package com.espresso.data.models.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressModel(
    val country: String,
    val city: String,
    val street: String,
    val zipCode: String
) : Parcelable
