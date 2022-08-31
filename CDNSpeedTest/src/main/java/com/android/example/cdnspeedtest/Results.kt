package com.android.example.cdnspeedtest

class Results (
//    var time: Long, id: Int, name: String, weight: Int, price: Int, url: String,
//        ): UrlModel(id, name, weight, price, url)
//
    var time: Long,
    var id: Int,
    var name: String,
    var weight: Int,
    var price: Int,
    var url: String
) {
    override fun toString(): String {
        return "Results(time=$time, id=$id, name='$name', weight=$weight, price=$price, url='$url')"
    }
}