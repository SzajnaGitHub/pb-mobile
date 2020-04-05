package com.espresso.data.models.company

data class InfoModel(
    val company: CompanyModel,
    val telephoneNumber: String,
    val ownerName: String,
    val workers: List<WorkerModel>
)

data class WorkerModel(
    val name: String,
    val function: String
)
