package com.android.example.kotlinlibaryapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.cdnspeedtest.CDN
import com.android.example.cdnspeedtest.Results


class MainActivity : AppCompatActivity() {

    var testButton: Button? = null
    var recyclerResults: RecyclerView? = null
    var noResults: TextView? = null
    var progressBar: ProgressBar? = null
    var key1: String = "oHlEsC7CD3dwV1DZBcZ9WzJx8wsjAZf7"
    var key2: String = "key1"
    var key3: String = "key"
    var endpoint: String = "get-endpoints/"
    var baseUrl: String = "http://192.168.1.105:3000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton = findViewById(R.id.testButton)
        recyclerResults = findViewById(R.id.rvResults)
        noResults = findViewById(R.id.noResults)
        progressBar = findViewById(R.id.progressBar)

        val cdn = CDN(key1, baseUrl, endpoint, this.applicationContext)

        fun giveMeRes(res: ArrayList<Results>):  ArrayList<Results>{

            if(res.size != 0 && res.isNotEmpty()) {
                val resultsModelArrayList = ArrayList<ResultsModel>()
                for (result in res) {
                    resultsModelArrayList.add(
                        ResultsModel(result.id, result.name, result.time, result.score, result.price, result.weight, result.url, result.errors)
                    )
                }
                resultsModelArrayList.sortBy { it.cdnSpeed }
                val resultsAdapter = ResultsAdapter(applicationContext, resultsModelArrayList)
                val linearLayoutManager = LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                progressBar!!.visibility = View.GONE
                recyclerResults!!.layoutManager = linearLayoutManager
                recyclerResults!!.adapter = resultsAdapter
            } else {
                progressBar!!.visibility = View.GONE
                noResults!!.visibility = View.VISIBLE
            }
            return res
        }

        testButton!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            cdn.checkCdnSpeed(::giveMeRes)
        }
    }
}