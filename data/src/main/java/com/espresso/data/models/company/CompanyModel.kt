package com.espresso.data.models.company

import android.os.Parcelable
import com.espresso.data.models.address.AddressModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyModel(
    val name: String,
    val nip: String,
    val regon: String,
    val address: AddressModel
) : Parcelable

