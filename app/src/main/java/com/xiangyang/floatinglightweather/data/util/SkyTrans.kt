package com.xiangyang.floatinglightweather.data.util

import com.xiangyang.floatinglightweather.R


/**
 * 天气图标与背景转换工具类
 */
class SkyTrans private constructor(val icon: Int, val bg: Int) {

    companion object {
        // 提供一个默认兜底的晴天配置
        private val defaultSky = SkyTrans(R.drawable.ic_clear_day, R.drawable.bg_clear_day)

        /**
         * 核心转换函数：根据高德返回的 weather 字符串，模糊匹配到对应的图标和背景
         */
        fun getSkyRes(weatherInfo: String?): SkyTrans {
            if (weatherInfo.isNullOrBlank()) return defaultSky

            return when {
                // 1. 晴天、少云系列
                weatherInfo == "晴" -> SkyTrans(R.drawable.ic_clear_day, R.drawable.bg_clear_day)
                weatherInfo == "少云" -> SkyTrans(
                    R.drawable.ic_partly_cloud_night,
                    R.drawable.bg_partly_cloudy_night
                )

                weatherInfo == "多云" || weatherInfo == "晴间多云" -> SkyTrans(
                    R.drawable.ic_partly_cloud_day,
                    R.drawable.bg_partly_cloudy_day
                )

                weatherInfo == "阴" -> SkyTrans(R.drawable.ic_cloudy, R.drawable.bg_cloudy)

                // 2. ✨ 风系列：只要包含了“风”或者“平静”，就都算作风，显示相同的图
                weatherInfo.contains("风") || weatherInfo == "平静" -> {
                    SkyTrans(R.drawable.ic_cloudy, R.drawable.bg_wind)
                }

                // 3. 雷阵雨系列
                weatherInfo.contains("雷阵雨") -> {
                    if (weatherInfo.contains("冰雹")) {
                        SkyTrans(R.drawable.ic_sleet, R.drawable.bg_snow)
                    } else {
                        SkyTrans(R.drawable.ic_thunder_shower, R.drawable.bg_rain)
                    }
                }

                // 4. 雨系列（包含各种暴雨、阵雨、XX雨-XX雨的区间等）
                weatherInfo.contains("雨") -> {
                    when {
                        weatherInfo.contains("小雨") || weatherInfo.contains("毛毛雨") || weatherInfo.contains(
                            "细雨"
                        ) -> {
                            SkyTrans(R.drawable.ic_light_rain, R.drawable.bg_rain)
                        }

                        weatherInfo.contains("中雨") -> SkyTrans(
                            R.drawable.ic_moderate_rain,
                            R.drawable.bg_rain
                        )

                        weatherInfo.contains("大雨") || weatherInfo.contains("阵雨") -> SkyTrans(
                            R.drawable.ic_heavy_rain,
                            R.drawable.bg_rain
                        )

                        weatherInfo.contains("暴雨") -> SkyTrans(
                            R.drawable.ic_storm_rain,
                            R.drawable.bg_rain
                        )

                        else -> SkyTrans(R.drawable.ic_light_rain, R.drawable.bg_rain) // 其他归为小雨
                    }
                }

                // 5. 雪系列（雨夹雪、大雪、XX雪-XX雪区间等）
                weatherInfo.contains("雪") -> {
                    when {
                        weatherInfo.contains("小雪") -> SkyTrans(
                            R.drawable.ic_light_snow,
                            R.drawable.bg_snow
                        )

                        weatherInfo.contains("中雪") -> SkyTrans(
                            R.drawable.ic_moderate_snow,
                            R.drawable.bg_snow
                        )

                        weatherInfo.contains("大雪") || weatherInfo.contains("暴雪") -> SkyTrans(
                            R.drawable.ic_heavy_snow,
                            R.drawable.bg_snow
                        )

                        else -> SkyTrans(R.drawable.ic_light_snow, R.drawable.bg_snow)
                    }
                }

                // 6. 霾、沙尘暴系列
                weatherInfo.contains("霾") -> SkyTrans(
                    R.drawable.ic_fog,
                    R.drawable.bg_fog
                ) // 霾可以借用雾的图，或定制
                weatherInfo.contains("沙") || weatherInfo.contains("尘") -> SkyTrans(
                    R.drawable.ic_fog,
                    R.drawable.bg_fog
                )

                // 7. 雾系列
                weatherInfo.contains("雾") || weatherInfo == "FOG" -> SkyTrans(
                    R.drawable.ic_fog,
                    R.drawable.bg_fog
                )

                // 8. 实在无法识别的生僻词，默认给晴天兜底
                else -> defaultSky
            }
        }
    }
}
