package com.xiangyang.floatinglightweather.data.api

import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.bean.ReGeoCodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReGeoCodeService {

    /**
     * 根据经纬度获取adCode区域编码
     * @param location 经纬度
     * @param key token
     * @return 主要获取区域编码
     */
    @GET("v3/geocode/regeo")
    suspend fun getReGeoCode(
        @Query("location") location: String,
        @Query("key") key: String = GDConstant.RequestParameters.TOKEN
    ): ReGeoCodeResponse
}