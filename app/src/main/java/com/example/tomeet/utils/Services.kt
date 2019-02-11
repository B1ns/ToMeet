package com.example.tomeet.utils

import com.example.tomeet.data.Register
import com.example.tomeet.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Services {

    @POST("/login")
    fun login(
        @Body user: User
    ): Call<com.example.tomeet.data.Result>


    @POST("/join")
    fun register(@Body reigster : Register): Call<com.example.tomeet.data.Result>

}