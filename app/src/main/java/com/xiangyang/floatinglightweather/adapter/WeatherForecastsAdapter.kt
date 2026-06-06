package com.xiangyang.floatinglightweather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.bean.WeatherInfoForecastsInfo
import com.xiangyang.floatinglightweather.data.util.SkyTrans
import com.xiangyang.floatinglightweather.databinding.ItemForecastWeatherBinding

class WeatherForecastsAdapter(val forecastsInfoList: List<WeatherInfoForecastsInfo>) :
    RecyclerView.Adapter<WeatherForecastsAdapter.WeatherForecastsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WeatherForecastsViewHolder {
        val binding =
            ItemForecastWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherForecastsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        viewHolder: WeatherForecastsViewHolder, position: Int
    ) {
        val forecastsInfo = forecastsInfoList[position]
        viewHolder.binding.tvForecastData.text = forecastsInfo.date
        viewHolder.binding.ivForecastIcon.setImageResource(SkyTrans.getSkyRes(forecastsInfo.dayWeather).icon)
        viewHolder.binding.tvForecastInfo.text =
            "${forecastsInfo.dayWeather}~${forecastsInfo.nightWeather}"
        viewHolder.binding.tvForecastTemp.text =
            "${forecastsInfo.dayTemp}${GDConstant.GeneralConstant.TEMPERATURE_MARK}~${forecastsInfo.nightTemp}${GDConstant.GeneralConstant.TEMPERATURE_MARK}"
    }

    override fun getItemCount() = forecastsInfoList.size

    class WeatherForecastsViewHolder(val binding: ItemForecastWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)
}