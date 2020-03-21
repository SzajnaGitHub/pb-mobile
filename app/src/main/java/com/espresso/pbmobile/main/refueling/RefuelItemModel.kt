package com.espresso.pbmobile.main.refueling

data class RefuelItemModel(
    val isClicked: Boolean = false,
    val text: String,
    val id: String,
    val pricePerUnit: Double,
    val clickHandler: (RefuelItemModel) -> Unit
)
