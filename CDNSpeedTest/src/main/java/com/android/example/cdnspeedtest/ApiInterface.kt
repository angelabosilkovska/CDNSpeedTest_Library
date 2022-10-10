package com.android.example.cdnspeedtest

import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiInterface {

    @GET("get-endpoints/{appKey}")
    fun getEndpoints(@Path("appKey") appKey: String): Call<GetEndpointsModel>

    @GET(".")
    fun getUrl(): Call<UrlModel>

    @POST(".")
    fun sendResults(@Body results: List<Results>) :Call<List<Results>>
}