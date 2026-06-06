package com.xiangyang.floatinglightweather.data.api

import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.constant.GDConstant.RequestParameters.Extensions
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsResponse
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoNowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInfoService {
    /**
     * 获取当前天气信息
     * @param city 城市编码 输入城市的 adcode
     * @return 天气信息
     *
     */
    @GET("v3/weather/weatherInfo")
    suspend fun getWeatherNowInfo(
        @Query("city") city: String,
        @Query("key") key: String = GDConstant.RequestParameters.TOKEN
    ): WeatherInfoNowResponse

    /**
     * 获取未来天气信息
     * @param city 城市编码 输入城市的 adcode
     * @param extensions 可选值：base/all base:返回实况天气 all:返回预报天气
     * @return 未来天气信息
     */
    @GET("v3/weather/weatherInfo")
    suspend fun getWeatherForecastsInfo(
        @Query("city") city: String,
        @Query("extensions") extensions: String = Extensions.ALL,
        @Query("key") key: String = GDConstant.RequestParameters.TOKEN
    ): WeatherInfoForecastsResponse
}