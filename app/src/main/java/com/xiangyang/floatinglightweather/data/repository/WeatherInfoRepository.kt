package com.xiangyang.floatinglightweather.data.repository

import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsInfo
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoLives
import com.xiangyang.floatinglightweather.data.network.WeatherNetWork
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherInfoRepository {
    /**
     * 获取当前天气信息
     * @param city 城市编码 输入城市的 adcode
     * @return 天气信息
     */
    suspend fun getWeatherNowInfo(city: String): List<WeatherInfoLives> {
        return withContext(Dispatchers.IO) {
            LogUtil.d("getWeatherNowInfo cityCode: $city")
            val weatherInfoNowResponse = WeatherNetWork.getWeatherNowInfo(city)
            val lives = weatherInfoNowResponse?.lives
            if (!lives.isNullOrEmpty()) {
                val resultList = mutableListOf<WeatherInfoLives>()
                resultList.addAll(lives)
                resultList
            } else emptyList()

        }
    }

    /**
     * 获取未来天气信息
     * @param city 城市编码 输入城市的 adcode
     * @return 未来天气信息
     */
    suspend fun getWeatherForecastsInfo(city: String): List<WeatherInfoForecastsInfo> {
        return withContext(Dispatchers.IO) {
            val weatherInfoForecastsResponse = WeatherNetWork.getWeatherForecastsInfo(city)

            val weatherCasts = weatherInfoForecastsResponse?.forecasts?.casts
            if (!weatherCasts.isNullOrEmpty()) {
                val resultList = mutableListOf<WeatherInfoForecastsInfo>()
                resultList.addAll(weatherCasts)
                resultList
            } else {
                emptyList()
            }
        }
    }
}