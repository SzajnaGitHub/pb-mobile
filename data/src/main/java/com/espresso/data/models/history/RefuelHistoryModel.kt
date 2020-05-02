package com.espresso.data.models.history

data class RefuelHistoryModel(
    val id: Long,
    val quantity: Double,
    val points: Double,
    val dateRefueling: String,
    val product: com.espresso.pbmobile.main.refueling.RefuelItemModel
)

data class CarWashHistoryModel(
    val id: Long,
    val dateofBooking: String,
    val dateOfReservation: String,
    val type: String
)
