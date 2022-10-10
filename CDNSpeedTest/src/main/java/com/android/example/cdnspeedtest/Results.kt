package com.android.example.cdnspeedtest

class Results (
    var score: Long?,
    var time: Long?,
    var id: Int?,
    var name: String?,
    var weight: Int?,
    var price: Int?,
    var url: String?,
    var errors: String? = ""
) {
    override fun toString(): String {
        return "Results(score=$score, time=$time, id=$id, name=$name, weight=$weight, price=$price, url=$url, errors=$errors)"
    }
}