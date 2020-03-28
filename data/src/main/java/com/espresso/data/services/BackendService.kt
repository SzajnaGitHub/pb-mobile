package com.espresso.data.services

import com.espresso.data.models.profile.UserProfile
import com.espresso.data.models.profile.UserRegisterInfo
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendService {
    //USER
    @GET("/user/{id}")
    fun getUser(@Path("id") id: Long): Single<UserProfile>

    @GET("/user/profile/{id}")
    fun profile(@Path("id") id: Long): Single<UserProfile>

    @POST("/user/register")
    fun register(@Body userInfo: UserRegisterInfo): Single<UserProfile>

}
