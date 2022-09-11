package com.android.example.cdnspeedtest

class Results (
    var time: Long?,
    var id: Int,
    var name: String,
    var weight: Int,
    var price: Int,
    var url: String,
    var errors: String=""
) {
    override fun toString(): String {
        return "Results(time=$time, id=$id, name='$name', weight=$weight, price=$price, url='$url')"
    }
}