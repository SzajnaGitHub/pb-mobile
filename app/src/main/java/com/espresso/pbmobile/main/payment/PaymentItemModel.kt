package com.espresso.pbmobile.main.payment

typealias PaymentItemClickHandler = (PaymentItemModel) -> Unit
typealias DocumentItemClickHandler = (DocumentItemModel) -> Unit

data class PaymentItemModel(
    val id: Int,
    val isClicked: Boolean = false,
    val title: String,
    val image: Int,
    val clickHandler: PaymentItemClickHandler
)

data class DocumentItemModel(
    val id: Int,
    val isClicked: Boolean = false,
    val title: String,
    val clickHandler: DocumentItemClickHandler
)
