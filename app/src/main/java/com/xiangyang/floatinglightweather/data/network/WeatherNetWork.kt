package com.xiangyang.floatinglightweather.data.network

import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.api.DistrictService
import com.xiangyang.floatinglightweather.data.bean.DistrictResponse
import com.xiangyang.floatinglightweather.util.LogUtil

object WeatherNetWork {
    // 获取api中的网络接口
    private val districtService = ServiceCreator.create<DistrictService>()
    suspend fun getDistrictInfo(keywords: String): DistrictResponse? {
        return try {
            // suspend函数不需要接受callback回调，直接使用变量接受返回值
            val districtResponse = districtService.getDistrictInfo(keywords)
            // 校验
            when (districtResponse.status) {
                GDConstant.ResultParameters.STATUS_SUCCESS -> districtResponse
                else -> {
                    LogUtil.e("高德接口返回失败原因：${districtResponse.info}")
                    null
                }
            }


        } catch (e: Exception) {
            // 捕获断网、超时、JSON解析失败等物理网络异常
            LogUtil.e("网络请求发生异常：${e.printStackTrace()}")
            null
        }

    }

}
