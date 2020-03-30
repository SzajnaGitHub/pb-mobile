package com.espresso.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClientInstance {
    private lateinit var backendService: BackendService

    fun initRetrofitClient() {
        backendService = Retrofit.Builder()
            .baseUrl("https://pbmobilespringbootapi.herokuapp.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BackendService::class.java)
    }

    fun getInstance() = backendService

}
