package com.xiangyang.floatinglightweather.data.repository

import androidx.lifecycle.LiveData
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.bean.District
import com.xiangyang.floatinglightweather.data.network.WeatherNetWork
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 获取行政地区信息
 */
object DistrictRepository {
    /**
     * 根据关键字，提取下一级的子城市或区县列表
     * @param keyword 搜索词（如 "大连市" 或 "辽宁省"）
     * @return 干净的子区域列表，如果失败则返回空列表
     */
    suspend fun getDistrictInfo(keyword: String): List<District> {
        //使用 withContext 强制将这个函数里的所有代码调度到 IO 线程执行
        // 确保哪怕上层调用者写错了，这个耗时操作也绝对 100% 不会卡死主线程
        return withContext(Dispatchers.IO) {
            val districtResponse = WeatherNetWork.getDistrictInfo(keyword)
            val districts = districtResponse?.districts

            if (!districts.isNullOrEmpty()) {
                val resultList = mutableListOf<District>()
                districts.forEach { parent ->
                    resultList.add(parent.copy(subDistricts = emptyList()))
                    parent.subDistricts.forEach { sub ->
                        resultList.add(sub.copy(subDistricts = emptyList()))
                    }
                }
                val suffixRegex = "([省市区县])".toRegex()
                val cleanKeyword = keyword.replace(suffixRegex,"")
                val isExactParentMatch = districts.any {
                    it.name.replace(suffixRegex,"") == cleanKeyword
                }
                // 用户输入和获取到的数据一致，就会把他相关的下级输出
                LogUtil.d("isExactParentMatch:$isExactParentMatch")
                // 统一过滤去重
                // 保留省市区级别，根据 adCode 进行去重 distinctBy 会保留第一次遇到的元素，过滤掉后面重复的
                resultList.filter {
                    it.level == GDConstant.ResultParameters.Level.PROVINCE
                            || it.level == GDConstant.ResultParameters.Level.CITY
                            || it.level == GDConstant.ResultParameters.Level.DISTRICT
                }.filter {
                    if (isExactParentMatch) {
                        // 精准查询
                        true
                    } else {
                        // 模糊查询
                        it.name.startsWith(keyword)
                    }
                }.distinctBy { it.adCode }
            } else emptyList()
        }
    }
}