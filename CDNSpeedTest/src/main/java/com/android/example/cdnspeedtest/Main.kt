package com.android.example.cdnspeedtest

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.log
import com.segment.analytics.kotlin.android.Analytics
import com.segment.analytics.kotlin.core.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.*
import kotlin.collections.ArrayList


class CDN {

    var key: String = ""

    constructor(appKey: String,ctx: Context){
        key = appKey
        initSegment(key,ctx)
    }

    val retrofitBuilder = RetrofitBuilder()
    var resultsUrl:String = ""
    private lateinit var analytics:Analytics

    private fun initSegment(key:String, ctx:Context){
        analytics = Analytics(key, ctx){

        }
    }

    fun checkCdnSpeed(giveMeRes:(ArrayList<Results>)-> ArrayList<Results>) {
        val call: Call<GetEndpointsModel> = retrofitBuilder.apiInterface.getEndpoints(key)
        call.enqueue(object : Callback<GetEndpointsModel> {
            override fun onResponse(
                call: Call<GetEndpointsModel>,
                response: Response<GetEndpointsModel>
            ) {
                if(response.isSuccessful){
                    println("Responseee body"+ response.body()?.servers)
                    val myEndpoints: List<UrlModel> = response.body()!!.servers
                    resultsUrl = response.body()!!.reportingURL
                    testCDNList(myEndpoints, giveMeRes)
                } else {
                    val emptyArray = ArrayList<Results>()
                    val error = response.message()+" "+response.code()
                    emptyArray.add(
                        Results(null, null, null, null, null, null, null, error)
                    )
                    giveMeRes(emptyArray)
                    println("Responseee unsuccessfull "+response.message()+response.code())
                }
            }

            override fun onFailure(call: Call<GetEndpointsModel>, t: Throwable) {
                val emptyArray = ArrayList<Results>()
                emptyArray.add(
                    Results(null, null, null, null, null, null, null, t.toString())
                )
                giveMeRes(emptyArray)
                println("Responseee error 1 "+t)
            }
        })
    }

    fun testCDNList(myEndpoints: List<UrlModel>, giveMeRes:( ArrayList<Results>)-> ArrayList<Results>){

        val result: ObservableList<Results> = ObservableArrayList<Results>()
        result.addOnListChangedCallback(object: ObservableList.OnListChangedCallback<ObservableList<Results>>(){
            override fun onChanged(sender: ObservableList<Results>?) {}

            override fun onItemRangeChanged(sender: ObservableList<Results>?, positionStart: Int, itemCount: Int) {}

            override fun onItemRangeInserted(sender: ObservableList<Results>?, positionStart: Int, itemCount: Int) {
                if(positionStart == myEndpoints.size-1){
                    val finalRes = ArrayList(sender)
                    sendResultsBack(finalRes)
                    giveMeRes(finalRes)
                }
            }

            override fun onItemRangeMoved(sender: ObservableList<Results>?, fromPosition: Int, toPosition: Int, itemCount: Int) {}

            override fun onItemRangeRemoved(sender: ObservableList<Results>?, positionStart: Int, itemCount: Int) {}
        })

        myEndpoints.forEach{res ->
            var retrofit = Retrofit.Builder()
                .baseUrl(res.url+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(retrofitBuilder.okHttpClient)
                .build()

            var apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

            val call: Call<UrlModel> = apiInterface.getUrl()
            call.enqueue(object : Callback<UrlModel> {
                override fun onResponse(
                    call: Call<UrlModel>,
                    response: Response<UrlModel>
                ) {
                    if(response.isSuccessful){
                        println("res sent: "+ response.raw().sentRequestAtMillis + "res rec: "+ response.raw().receivedResponseAtMillis)
                        val speedResult = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis)
                        val scoreResult = calculateScore(speedResult + res.price)
                        println("Result: "+ scoreResult)
                        result.add(Results(scoreResult,speedResult,res.id,res.name,res.weight,res.price,res.url))
                    } else {
                        result.add(Results(null, null,res.id,res.name,res.weight,res.price,res.url, errors = response.code().toString()))
                        println("Responseee unsuccessfull")
                    }
                }

                override fun onFailure(call: Call<UrlModel>, t: Throwable) {
                    result.add(Results(null, null,res.id,res.name,res.weight,res.price,res.url, errors = t.toString()))
                    println("Responseee error 2"+t)
                }
            })
        }
    }

    private fun calculateScore(score: Long): Long{
        return when (score) {
            in 1..300 -> 10
            in 300..500 -> 9
            in 500..700 -> 8
            in 700..900 -> 7
            in 900..1100 -> 6
            in 1100..1400 -> 5
            in 1400..1700 -> 4
            in 1700..2100 -> 3
            in 2100..2600 -> 2
            else -> { 1 }
        }
    }

    private fun sendResultsBack(rez: List<Results>){

        rez.forEach{
        analytics.track("CDNSpeedTest", buildJsonObject {
            put("CDN Name", it.name)
            put("CDN ID", it.id)
            put("CDN URL", it.url)
            put("CDN Speed", it.time)
            put("CDN Weight", it.weight)
            put("CDN Price", it.price)
        })}

        var retrofit = Retrofit.Builder()
            .baseUrl("$resultsUrl/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(retrofitBuilder.okHttpClient)
            .build()

        var apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

        val call: Call<List<Results>> = apiInterface.sendResults(rez)
        call.enqueue(object : Callback<List<Results>> {
            override fun onResponse(
                call: Call<List<Results>>,
                response: Response<List<Results>>) {
                if(response.isSuccessful){
                    println("Responseee isSuccessful")
                } else {
                    println("Responseee unsuccessfull")
                }
            }

            override fun onFailure(call: Call<List<Results>>, t: Throwable) {
                println("Responseee error 3 "+t)
            }
        })
    }
}
