package com.android.example.kotlinlibaryapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.cdnspeedtest.CDN
import com.android.example.cdnspeedtest.Results


class MainActivity : AppCompatActivity() {

    var testButton: Button? = null
    var recyclerResults: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton = findViewById(R.id.testButton)
        recyclerResults = findViewById(R.id.rvResults)

        var cdn = CDN("oHlEsC7CD3dwV1DZBcZ9WzJx8wsjAZf7", this.applicationContext)

        fun giveMeRes(rez: ArrayList<Results>):  ArrayList<Results>{
           var resultsModelArrayList = ArrayList<ResultsModel>()
            for (result in rez) {
                System.out.println("Titles " + result.name)
                resultsModelArrayList!!.add(
                    ResultsModel(
                        result.id,
                        result.name,
                        result.time,
                        result.price,
                        result.weight,
                        result.url,
                        result.errors
                    )
                )
            }
            resultsModelArrayList.sortBy { it.cdnSpeed }
            val resultsAdapter = ResultsAdapter(applicationContext, resultsModelArrayList)
            val linearLayoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerResults!!.layoutManager = linearLayoutManager
            recyclerResults!!.adapter = resultsAdapter
            return rez
        }

        testButton!!.setOnClickListener {
            cdn.checkCdnSpeed(::giveMeRes)
        }
    }
}