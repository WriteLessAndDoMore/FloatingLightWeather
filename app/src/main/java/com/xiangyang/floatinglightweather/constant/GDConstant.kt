package com.xiangyang.floatinglightweather.constant

object GDConstant {
    object RequestParameters {
        // 高德Token请求服务权限标识
        const val TOKEN = "70c2303afb185c7af92781a7401e5518"

        // 子级行政区
        const val SUBDISTRICT = 1

        /**
         * 气象类型
         * 可选值：base/all
         * base:返回实况天气
         * all:返回预报天气
         */
        object Extensions {
            const val BASE = "base"
            const val ALL = "all"
        }

    }

    object ResultParameters {
        // 高德status
        const val STATUS_SUCCESS = "1"
        const val STATUS_FAIL = "0"

        // 高德info
        const val INFO = "OK"

        object Level {
            const val PROVINCE = "province"
            const val CITY = "city"
            const val DISTRICT = "district"
        }
    }

    object GeneralConstant {
        const val TEMPERATURE_MARK = "°"
        const val PERCENT_SIGN = "%"
        object IntentKey{
            const val CITY_AD_CODE = "city_ad_code"
        }
    }
}