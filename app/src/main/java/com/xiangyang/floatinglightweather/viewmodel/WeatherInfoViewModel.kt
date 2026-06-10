package com.xiangyang.floatinglightweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsInfo
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoLives
import com.xiangyang.floatinglightweather.data.repository.WeatherInfoRepository
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WeatherInfoViewModel: ViewModel() {
    val forecastsInfoList = mutableListOf<WeatherInfoForecastsInfo>()
    private val _weatherInfoLivesResult = MutableLiveData<List<WeatherInfoLives>>()
    val weatherInfoLivesResult: LiveData<List<WeatherInfoLives>> = _weatherInfoLivesResult
    private val _weatherInfoForecastsResult = MutableLiveData<List<WeatherInfoForecastsInfo>>()
    val weatherInfoForecastsResult: LiveData<List<WeatherInfoForecastsInfo>> =
        _weatherInfoForecastsResult
    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing
    private var refreshTimerJob: Job? = null
    private var lastRefreshTime: Long = 0
    private val refreshTimer: Long = 1000 * 60 * 15

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
    fun checkAndRefresh(adCode: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime >= refreshTimer) {
            getWeatherAllInfo(adCode)
        }
    }

    fun startMinuteTimer(adCode: String, timer: Long = refreshTimer) {
        // 清除之前的job，确保当前job的唯一性
        stopMinuteTimer()
        // 绑定 viewModelScope 随生命周期安全销毁
        refreshTimerJob = viewModelScope.launch {
            while (isActive) {
                // 由于第一次是生命周期刷新所以此时不需要刷新
                delay(timer)
                LogUtil.d("定时开始......$timer")
                // 挂起timer时间后获取天气信息
                if (adCode.isNotBlank()) {
                    getWeatherAllInfo(adCode)
                }
            }

        }
    }

    fun stopMinuteTimer() {
        if (refreshTimerJob?.isActive == true) {
            LogUtil.d("停止计时器")
            refreshTimerJob?.cancel()
        }
    }

    fun getWeatherAllInfo(cityCode: String) {
        // 开始刷新
        _isRefreshing.value = true
        viewModelScope.launch {
            val livesDeferred = async { WeatherInfoRepository.getWeatherNowInfo(cityCode) }
            val forecastsDeferred =
                async { WeatherInfoRepository.getWeatherForecastsInfo(cityCode) }
            // 等待两者都完成后统一赋值
            _weatherInfoLivesResult.value = livesDeferred.await()
            _weatherInfoForecastsResult.value = forecastsDeferred.await()
            // 数据全部加载完毕，通知前端关闭动画
            _isRefreshing.value = false
            // 上一次刷新的时间
            lastRefreshTime = System.currentTimeMillis()
        }
    }
}