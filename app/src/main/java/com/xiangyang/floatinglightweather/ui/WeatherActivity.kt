package com.xiangyang.floatinglightweather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiangyang.floatinglightweather.R
import com.xiangyang.floatinglightweather.adapter.WeatherForecastsAdapter
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.util.SkyTrans
import com.xiangyang.floatinglightweather.databinding.ActivityWeatherBinding
import com.xiangyang.floatinglightweather.util.LogUtil
import com.xiangyang.floatinglightweather.viewmodel.WeatherInfoViewModel

class WeatherActivity : AppCompatActivity() {
    private val weatherInfoViewModel: WeatherInfoViewModel by viewModels()
    lateinit var binding: ActivityWeatherBinding
    private lateinit var adapter: WeatherForecastsAdapter
    var currentAdCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
        // 获取并保存搜索内容
        currentAdCode = intent.getStringExtra(GDConstant.GeneralConstant.IntentKey.CITY_AD_CODE)
        // 首次进入页面执行请求
        refreshAndGetWeatherAllInfo()

        binding.sflFreshWeather.setColorSchemeResources(R.color.teal_200)
        binding.sflFreshWeather.setOnRefreshListener {
            refreshAndGetWeatherAllInfo()
        }
        binding.icNowWeather.btHome.setOnClickListener {
            binding.dlWeather.openDrawer(GravityCompat.START)
        }
        binding.dlWeather.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(p0: View, p1: Float) {}
            override fun onDrawerOpened(p0: View) {}
            override fun onDrawerStateChanged(p0: Int) {}
            override fun onDrawerClosed(view : View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
        initObserve()

    }

    // 封装统一的刷新/请求数据方法
    private fun refreshAndGetWeatherAllInfo() {
        if (!currentAdCode.isNullOrBlank()) {
            weatherInfoViewModel.getWeatherAllInfo(currentAdCode!!)
        } else {
            LogUtil.e("错误：未获取到有效的城市代码，无法请求/刷新天气！")
        }
    }

    fun refreshAndGetWeatherAllInfo(adCode: String?) {
        if (!adCode.isNullOrBlank()) {
            weatherInfoViewModel.getWeatherAllInfo(adCode)
        } else {
            LogUtil.e("错误：未获取到有效的城市代码，无法请求/刷新天气！")
        }
    }

    private fun initRecycleView() {
        binding.icForecastWeather.rvForecast.layoutManager = LinearLayoutManager(this)
        adapter = WeatherForecastsAdapter(weatherInfoViewModel.forecastsInfoList)
        binding.icForecastWeather.rvForecast.adapter = adapter
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initObserve() {
        weatherInfoViewModel.weatherInfoLivesResult.observe(this) { infos ->
            infos.forEach { info ->
                binding.icNowWeather.tvNowPlace.text = info.city
                binding.icNowWeather.tvNowTemp.text =
                    "${info.temperature}${GDConstant.GeneralConstant.TEMPERATURE_MARK}"
                binding.icNowWeather.tvNowSituation.text = info.weather
                binding.icNowWeather.tvAirHumidityValue.text =
                    "${info.humidity}${GDConstant.GeneralConstant.PERCENT_SIGN}"
                val skyTrans = SkyTrans.getSkyRes(info.weather)
                binding.icNowWeather.clNowBg.setBackgroundResource(skyTrans.bg)

            }
        }
        weatherInfoViewModel.weatherInfoForecastsResult.observe(this) { infos ->
            weatherInfoViewModel.forecastsInfoList.clear()
            weatherInfoViewModel.forecastsInfoList.addAll(infos)
            adapter.notifyDataSetChanged()
        }

        weatherInfoViewModel.isRefreshing.observe(this) { isRefresh ->
            binding.sflFreshWeather.isRefreshing = isRefresh
        }
    }
}