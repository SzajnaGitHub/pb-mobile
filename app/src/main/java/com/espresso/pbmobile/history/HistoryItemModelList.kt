package com.espresso.pbmobile.history

import java.io.Serializable

data class RefuelHistoryItemModel(
    val date: String,
    val hour: String = "",
    val fuelType: String,
    val cost: Double
) : Serializable

data class CarWashHistoryItemModel(
    val date: String,
    val hour: String,
    val fuelType: String,
    val cost: String
) : Serializable
