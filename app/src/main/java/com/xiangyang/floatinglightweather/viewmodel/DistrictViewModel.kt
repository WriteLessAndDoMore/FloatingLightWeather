package com.xiangyang.floatinglightweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiangyang.floatinglightweather.data.bean.District
import com.xiangyang.floatinglightweather.data.repository.DistrictRepository
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class DistrictViewModel : ViewModel() {

    // 定义内部私有的 MutableLiveData，用来在协程里写入网络结果
    private val _subDistrictsResult = MutableLiveData<List<District>>()
    val districtList = mutableListOf<District>()

    // 暴露给view层只读的liveData
    val subDistrictResult: LiveData<List<District>> = _subDistrictsResult
    private var searchJob: Job? = null

    /**
     * 根据搜索框内容通知行政信息
     * @param keyword 用户输入的城市/省份名称
     */
    fun searchLocation(keyword: String) {
        try {
            // 如果用户清空搜索框，直接不请求网络
            if (keyword.isBlank()) {
                _subDistrictsResult.value = emptyList()
                return
            }
            // 避免用户打字太快导致上一次还没有请求完，导致直接发出了下一次请求
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                // 🌟 【防抖逻辑】：让协程在原地先睡 300 毫秒。
                // 如果用户连续输入时，输入300ms内协程会被上面的 cancel() 杀死，
                // 这样网络请求根本不会发射出去，极大地保护了服务器和车机流量。
                delay(300)
                // 调用repository层挂起函数获取信息
                val districts = DistrictRepository.getDistrictInfo(keyword)
                // 将数据给到LiveData
                _subDistrictsResult.value = districts

            }
        } catch (e: CancellationException) {
            // 当协程被 searchJob?.cancel() 强行杀死时，会触发这个异常
            LogUtil.d("旧的搜索任务 [$keyword] 已被完美掐死，不影响最新的搜索结果")
        } catch (e: Exception) {
            LogUtil.e("搜索过程中发生错误${e.printStackTrace()}")
            _subDistrictsResult.value = emptyList()
        }

    }

}