package com.xiangyang.floatinglightweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var adapter: WeatherForecastsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
        val adCode = intent.getStringExtra(GDConstant.GeneralConstant.IntentKey.CITY_AD_CODE)
        setWeatherInfo(adCode)
        setWeatherInfoForecasts(adCode)
        initObserve()

    }

    private fun initRecycleView() {
        binding.icForecastWeather.rvForecast.layoutManager = LinearLayoutManager(this)
        adapter = WeatherForecastsAdapter(weatherInfoViewModel.forecastsInfoList)
        binding.icForecastWeather.rvForecast.adapter = adapter
    }

    private fun setWeatherInfo(adCode: String?) {
        if (!adCode.isNullOrBlank()) {
            weatherInfoViewModel.getWeatherInfoLives(adCode)
        } else {
            LogUtil.e("错误：未从上个页面接收到有效的城市代码！")
        }
    }

    private fun setWeatherInfoForecasts(adCode: String?) {
        if (!adCode.isNullOrBlank()) {
            weatherInfoViewModel.getWeatherInfoForecasts(adCode)
        } else {
            LogUtil.e("错误：未从上个页面接收到有效的城市代码！")
        }
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
    }
}