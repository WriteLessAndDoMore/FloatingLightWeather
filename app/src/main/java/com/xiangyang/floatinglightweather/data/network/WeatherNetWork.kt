package com.xiangyang.floatinglightweather.data.network

import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.api.DistrictService
import com.xiangyang.floatinglightweather.data.api.ReGeoCodeService
import com.xiangyang.floatinglightweather.data.api.WeatherInfoService
import com.xiangyang.floatinglightweather.data.bean.DistrictResponse
import com.xiangyang.floatinglightweather.data.bean.ReGeoCodeResponse
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsResponse
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoNowResponse
import com.xiangyang.floatinglightweather.util.LogUtil

object WeatherNetWork {
    // 获取api中的网络接口
    private val districtService = ServiceCreator.create<DistrictService>()
    private val weatherInfoService = ServiceCreator.create<WeatherInfoService>()
    private val reGeoCodeService = ServiceCreator.create<ReGeoCodeService>()

    /**
     * 获取输入位置信息
     * @param keywords 位置
     * @return 位置信息
     */
    suspend fun getDistrictInfo(keywords: String): DistrictResponse? {
        return try {
            // suspend函数不需要接受callback回调，直接使用变量接受返回值
            val districtResponse = districtService.getDistrictInfo(keywords)
            // 校验
            when (districtResponse.status) {
                GDConstant.ResultParameters.STATUS_SUCCESS -> districtResponse
                else -> {
                    LogUtil.e("getDistrictInfo 高德接口返回失败原因：${districtResponse.info}")
                    null
                }
            }
        } catch (e: Exception) {
            // 捕获断网、超时、JSON解析失败等物理网络异常
            LogUtil.e("getDistrictInfo 网络请求发生异常：${e.printStackTrace()}")
            null
        }
    }

    /**
     * 获取当前天气信息
     * @param city 城市编码 输入城市的 adcode
     * @return 天气信息
     */
    suspend fun getWeatherNowInfo(city: String): WeatherInfoNowResponse? {
        return try {
            val weatherInfoNowResponse = weatherInfoService.getWeatherNowInfo(city)
            // 校验
            when (weatherInfoNowResponse.status) {
                GDConstant.ResultParameters.STATUS_SUCCESS -> weatherInfoNowResponse
                else -> {
                    LogUtil.e("getWeatherNowInfo 高德接口返回失败原因：${weatherInfoNowResponse.info}")
                    null
                }
            }
        } catch (e: Exception) {
            LogUtil.e("getWeatherNowInfo 网络请求发生异常：${e.printStackTrace()}")
            null
        }
    }

    /**
     * 获取未来天气信息
     * @param city 城市编码 输入城市的 adcode
     * @return 未来天气信息
     */
    suspend fun getWeatherForecastsInfo(city: String): WeatherInfoForecastsResponse? {
        return try {
            val weatherInfoForecastsResponse = weatherInfoService.getWeatherForecastsInfo(city)
            // 校验
            when (weatherInfoForecastsResponse.status) {
                GDConstant.ResultParameters.STATUS_SUCCESS -> weatherInfoForecastsResponse
                else -> {
                    LogUtil.e("getWeatherForecastsInfo 高德接口返回失败原因：${weatherInfoForecastsResponse.info}")
                    null
                }
            }
        } catch (e: Exception) {
            LogUtil.e("getWeatherForecastsInfo 网络请求发生异常：${e.message}")
            null
        }
    }

    /**
     * 根据经纬度获取adCode区域编码
     * @param location 经纬度
     * @return 主要获取区域编码
     */
    suspend fun getReGeoCode(location: String): ReGeoCodeResponse? {
        return try {
            val reGeoCodeResponse = reGeoCodeService.getReGeoCode(location)
            when (reGeoCodeResponse.status) {
                GDConstant.ResultParameters.STATUS_SUCCESS -> reGeoCodeResponse
                else -> {
                    LogUtil.e("getReGeoCode 高德接口返回失败原因：${reGeoCodeResponse.info}")
                    null
                }
            }
        } catch (e: Exception) {
            LogUtil.e("getReGeoCode 网络请求发生异常：${e.message}")
            null
        }
    }


}
