package com.xiangyang.floatinglightweather.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiangyang.floatinglightweather.data.repository.ReGeoCodeRepository
import com.xiangyang.floatinglightweather.data.util.SystemLocationHelper
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.launch

class ReGeoCodeViewModel: ViewModel() {
    private val _adCodeStatusLiveData = MutableLiveData<AdCodeStatus>()
    val adCodeStatusLiveData: LiveData<AdCodeStatus> = _adCodeStatusLiveData

    fun loadWeatherByLocation(context: Context) {
        viewModelScope.launch {
            val location = SystemLocationHelper.getCurrentLocation(context)
            if (location == null) {
                // 定位失败
                LogUtil.e("loadWeatherByLocation: $location")
                _adCodeStatusLiveData.value = AdCodeStatus.LocationFail
                return@launch
            }
            val locationString = "${location.first},${location.second}"
            try {
                val reGeoCode = ReGeoCodeRepository.getReGeoCode(locationString)
                val adCode = reGeoCode?.addressComponent?.adCode
                if (!adCode.isNullOrBlank()) {
                    _adCodeStatusLiveData.value = AdCodeStatus.ResponseSuccess(adCode)
                } else {
                    _adCodeStatusLiveData.value = AdCodeStatus.ResponseFail
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _adCodeStatusLiveData.value = AdCodeStatus.ResponseFail
            }
        }
    }

}
sealed class AdCodeStatus {
    object LocationFail: AdCodeStatus()
    object ResponseFail: AdCodeStatus()
    data class ResponseSuccess(val adCode: String): AdCodeStatus()
}