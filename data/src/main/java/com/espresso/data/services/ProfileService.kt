package com.espresso.data.services

import com.espresso.data.models.UserProfile
import io.reactivex.Single
import retrofit2.http.GET

interface ProfileService {
    @GET("/")
    fun profile(): Single<UserProfile>
}
