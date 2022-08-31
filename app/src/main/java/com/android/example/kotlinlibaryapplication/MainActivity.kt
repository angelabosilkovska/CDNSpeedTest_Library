package com.android.example.kotlinlibaryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.example.cdnspeedtest.CDN


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var cdn = CDN();
        cdn.getCdnList()
    }
}