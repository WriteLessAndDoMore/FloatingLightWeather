package com.xiangyang.floatinglightweather.data.api

import com.xiangyang.floatinglightweather.base.WeatherApplication
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.bean.DistrictResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 获取城市相关接口
 */
interface DistrictService {
    @GET("v3/config/district")
    suspend fun getDistrictInfo(
        @Query("keywords") keywords: String,
        @Query("subdistrict") subdistrict: Int = GDConstant.RequestParameters.SUBDISTRICT,
        @Query("key") key: String = GDConstant.RequestParameters.TOKEN
    ): DistrictResponse

}