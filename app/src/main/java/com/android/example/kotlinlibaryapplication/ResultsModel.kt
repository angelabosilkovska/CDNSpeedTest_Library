package com.android.example.kotlinlibaryapplication

class ResultsModel {
    var cdnId: Int? = null
    var cdnName: String? = null
    var cdnSpeed: Long? = null
    var cdnPrice: Int? = null
    var cdnWeight: Int? = null
    var cdnUrl: String? = null
    var error: String? = null

    constructor(
        cdnId: Int?,
        cdnName: String?,
        cdnSpeed: Long?,
        cdnPrice: Int?,
        cdnWeight: Int?,
        cdnUrl: String?,
        error: String?
    ) {
        this.cdnName = cdnName
        this.cdnSpeed = cdnSpeed
        this.cdnPrice = cdnPrice
        this.cdnId = cdnId
        this.cdnWeight = cdnWeight
        this.cdnUrl = cdnUrl
        this.error = error
    }
}