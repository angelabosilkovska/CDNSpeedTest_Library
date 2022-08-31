package com.android.example.cdnspeedtest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.util.*


interface ApiInterface {

    @GET("get-endpoints")
    fun getEndpoints(): Call<GetEndpointsModel>

    @GET(".")
    fun getUrl(): Call<UrlModel>

    @POST(".")
    fun sendResults(@Body results: List<Results>) :Call<List<Results>>

//    @GET("users/{user}")
//    fun getUser(@Path("user") user: String?): Observable<User?>?

}