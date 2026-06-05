package com.xiangyang.floatinglightweather.data.bean

import com.google.gson.annotations.SerializedName

/**
 * 高德行政区划接口返回的根对象
 */
data class DistrictResponse(
    @SerializedName("status") val status: String,       // 1: 成功, 0: 失败
    @SerializedName("info") val info: String,           // 返回状态说明
    @SerializedName("infocode") val infoCode: String,   // 状态码
    @SerializedName("count") val count: String,         // 匹配的数量
    @SerializedName("districts") val districts: List<District>
)
/**
 * 行政区划核心节点（支持无限套娃的递归结构）
 */
data class District(
    @SerializedName("citycode") val cityCode: Any,      // 城市区号（注意：有些省份返回的是空数组 []，所以用 Any 防止崩溃）
    @SerializedName("adcode") val adCode: String,       // 行政区编码（如：210200）
    @SerializedName("name") val name: String,           // 行政区名称（如：大连市）
    @SerializedName("center") val center: String,       // 中心点经纬度（"经度,纬度"）
    @SerializedName("level") val level: String,         // 级别：province(省), city(市), district(区/县), street(街道)
    //核心：递归持有下一级的列表
    @SerializedName("districts") val subDistricts: List<District>
)
