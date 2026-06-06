package com.xiangyang.floatinglightweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsInfo
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoLives
import com.xiangyang.floatinglightweather.data.repository.WeatherInfoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherInfoViewModel: ViewModel() {
    val forecastsInfoList = mutableListOf<WeatherInfoForecastsInfo>()
    private val _weatherInfoLivesResult = MutableLiveData<List<WeatherInfoLives>>()
    val weatherInfoLivesResult: LiveData<List<WeatherInfoLives>> = _weatherInfoLivesResult
    private val _weatherInfoForecastsResult = MutableLiveData<List<WeatherInfoForecastsInfo>>()
    val weatherInfoForecastsResult: LiveData<List<WeatherInfoForecastsInfo>> = _weatherInfoForecastsResult
    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun getWeatherInfoLives(cityCode: String) {
        viewModelScope.launch {
            val weatherInfoLives = WeatherInfoRepository.getWeatherNowInfo(cityCode)
            _weatherInfoLivesResult.value = weatherInfoLives
        }
    }

    fun getWeatherInfoForecasts(cityCode: String) {
        viewModelScope.launch {
            val weatherInfoForecasts = WeatherInfoRepository.getWeatherForecastsInfo(cityCode)
            _weatherInfoForecastsResult.value = weatherInfoForecasts
        }
    }

    fun getWeatherAllInfo(cityCode: String) {
        // 开始刷新
        _isRefreshing.value = true
        viewModelScope.launch {
            val livesDeferred = async { WeatherInfoRepository.getWeatherNowInfo(cityCode) }
            val forecastsDeferred = async { WeatherInfoRepository.getWeatherForecastsInfo(cityCode) }
            // 等待两者都完成后统一赋值
            _weatherInfoLivesResult.value = livesDeferred.await()
            _weatherInfoForecastsResult.value = forecastsDeferred.await()
            // 数据全部加载完毕，通知前端关闭动画
            _isRefreshing.value = false
        }
    }
}