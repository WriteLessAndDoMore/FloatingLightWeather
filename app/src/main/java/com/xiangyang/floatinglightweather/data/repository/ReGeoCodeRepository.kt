package com.xiangyang.floatinglightweather.data.repository

import android.location.Location
import com.xiangyang.floatinglightweather.data.bean.ReGeoCode
import com.xiangyang.floatinglightweather.data.network.WeatherNetWork
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ReGeoCodeRepository {
    /**
     * 根据经纬度获取adCode区域编码
     * @param location 经纬度
     * @return 主要获取区域编码
     */
    suspend fun getReGeoCode(location: String): ReGeoCode? {
        return withContext(Dispatchers.IO) {
            LogUtil.d("getReGeoCode cityCode: $location")
            val reGeoCodeResponse = WeatherNetWork.getReGeoCode(location)
            reGeoCodeResponse?.reGeoCode
        }
    }
}