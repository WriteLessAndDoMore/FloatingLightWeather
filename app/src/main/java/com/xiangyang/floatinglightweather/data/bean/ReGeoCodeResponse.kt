package com.xiangyang.floatinglightweather.data.bean

import com.google.gson.annotations.SerializedName

data class ReGeoCodeResponse(
    @SerializedName("status") val status: String, // 1: 成功, 0: 失败
    @SerializedName("regeocode") val reGeoCode: ReGeoCode?,
    @SerializedName("info") val info: String

)
data class ReGeoCode(
    @SerializedName("addressComponent") val addressComponent: AddressComponent?
)
data class AddressComponent(
    @SerializedName("adcode") val adCode: String?
)
