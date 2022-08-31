package com.android.example.cdnspeedtest

import android.util.Log
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class CDN {
    val retrofitBuilder = RetrofitBuilder()
    var resultsUrl:String = ""

    fun getCdnList(){
        val call: Call<GetEndpointsModel> = retrofitBuilder.apiInterface.getEndpoints()
        call.enqueue(object : Callback<GetEndpointsModel> {
            override fun onResponse(
                call: Call<GetEndpointsModel>,
                response: Response<GetEndpointsModel>
            ) {
                if(response.isSuccessful){
                    println("Responseee body"+ response.body()?.servers)
                    val myEndpoints: List<UrlModel> = response.body()!!.servers
                    resultsUrl = response.body()!!.reportingURL;
                    testCDNList(myEndpoints)
                } else {
                    println("Responseee unsuccessfull")
                }
            }

            override fun onFailure(call: Call<GetEndpointsModel>, t: Throwable) {
                println("Responseee error 1 "+t)
            }
        })
    }
    fun testCDNList(myEndpoints: List<UrlModel>){
//        val requests = ArrayList<Call<*>>()

        var result = ArrayList<Results>()
//        var apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)
        var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            // .authenticator(AccessTokenAuthenticator())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(OkHttpProfilerInterceptor())
        //  .addNetworkInterceptor(OnlineInterceptor())
        Log.i("Hey","ovde")
        var okHttpClient: OkHttpClient = builder.build()
        myEndpoints.forEach{res ->

            var retrofit = Retrofit.Builder()
                .baseUrl(res.url+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

            var apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

            val call: Call<UrlModel> = apiInterface.getUrl()
            call.enqueue(object : Callback<UrlModel> {
                override fun onResponse(
                    call: Call<UrlModel>,
                    response: Response<UrlModel>
                ) {
                    if(response.isSuccessful){
                        println("res sent: "+ response.raw().sentRequestAtMillis + "res rec: "+ response.raw().receivedResponseAtMillis )
                        val rezultat = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis)
                        println("Result: "+ rezultat)
//                        testCDNList(response.body()!!)
                        result.add(Results(rezultat,res.id,res.name,res.weight,res.price,res.url))
                        if(myEndpoints.size == result.size){
                           sendResultsBack(result)
                            println("site rezulstati"+ result.toString())

                        }
                    } else {
                        println("Responseee unsuccessfull")
                    }
                }

                override fun onFailure(call: Call<UrlModel>, t: Throwable) {
                    println("Responseee error 2"+t)
                }
            })

        }
    }
    private fun sendResultsBack(rez: ArrayList<Results>){
    Log.i("Hey",resultsUrl)
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
                response: Response<List<Results>>
            ) {
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
