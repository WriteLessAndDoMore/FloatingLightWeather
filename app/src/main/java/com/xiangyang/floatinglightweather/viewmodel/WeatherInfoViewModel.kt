package com.xiangyang.floatinglightweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsInfo
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoLives
import com.xiangyang.floatinglightweather.data.repository.WeatherInfoRepository
import kotlinx.coroutines.launch

class WeatherInfoViewModel: ViewModel() {
    private val _weatherInfoLivesResult = MutableLiveData<List<WeatherInfoLives>>()
    private val _weatherInfoForecastsResult = MutableLiveData<List<WeatherInfoForecastsInfo>>()
    val weatherInfoLivesResult: LiveData<List<WeatherInfoLives>> = _weatherInfoLivesResult
    val weatherInfoForecastsResult: LiveData<List<WeatherInfoForecastsInfo>> = _weatherInfoForecastsResult

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
}