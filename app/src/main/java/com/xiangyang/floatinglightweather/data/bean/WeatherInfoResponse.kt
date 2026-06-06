package com.xiangyang.floatinglightweather.data.bean

import com.google.gson.annotations.SerializedName

data class WeatherInfoNowResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: String,
    @SerializedName("info") val info: String,
    @SerializedName("infocode") val infoCode: String,
    @SerializedName("lives") val lives: List<WeatherInfoLives>
)
data class WeatherInfoLives(
    @SerializedName("province") val province: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("adcode") val adCode: String = "",
    @SerializedName("weather") val weather: String = "",
    @SerializedName("temperature") val temperature: Int = 0,
    @SerializedName("winddirection") val windDirection: String = "",
    @SerializedName("windpower") val windpower: String = "",
    @SerializedName("humidity") val humidity: Int = 0,
    @SerializedName("reporttime") val reportTime: String = "",
    @SerializedName("temperature_float") val temperatureFloat: Double = 0.0,
    @SerializedName("humidity_float") val humidityFloat: Double = 0.0,
)

data class WeatherInfoForecastsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: String,
    @SerializedName("info") val info: String,
    @SerializedName("infocode") val infoCode: String,
    @SerializedName("forecasts") val forecasts: WeatherInfoForecasts
)

data class WeatherInfoForecasts(
    @SerializedName("city") val city: String,
    @SerializedName("adcode") val adCode: String,
    @SerializedName("province") val province: String,
    @SerializedName("reporttime") val reportTime: String,
    @SerializedName("casts") val casts: List<WeatherInfoForecastsInfo>
)

data class WeatherInfoForecastsInfo(
    @SerializedName("date") val date: String,
    @SerializedName("week") val week: Int,
    @SerializedName("dayweather") val dayWeather: String,
    @SerializedName("nightweather") val nightWeather: String,
    @SerializedName("daytemp") val dayTemp: Int,
    @SerializedName("nighttemp") val nightTemp: Int,
    @SerializedName("daywind") val dayWind: String,
    @SerializedName("nightwind") val nightWind: String,
    @SerializedName("daypower") val dayPower: Int,
    @SerializedName("nightpower") val nightPower: Int,
    @SerializedName("daytemp_float") val dayTempFloat: Double,
    @SerializedName("nighttemp_float") val nightTempFloat: Double,
)
