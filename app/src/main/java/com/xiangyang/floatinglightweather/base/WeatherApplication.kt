package com.xiangyang.floatinglightweather.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherApplication: Application() {


    companion object {
        // 获取全局context
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}