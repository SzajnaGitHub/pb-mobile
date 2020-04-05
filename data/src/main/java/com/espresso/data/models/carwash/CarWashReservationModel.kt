package com.espresso.data.models.carwash

typealias CarWashReservationItemClick = (CarWashReservationModel) -> Unit

data class CarWashReservationModel(
    val hour: String,
    val clickHandler: CarWashReservationItemClick
)
