package com.xiangyang.floatinglightweather.util

import android.util.Log

object LogUtil {
    const val VERBOSE: Int = 1
    const val DEBUG: Int = 2
    const val INFO: Int = 3
    const val WARN: Int = 4
    const val ERROR: Int = 5
    const val TAG = "FloatingLightWeather"
    private var level: Int = VERBOSE
    fun v(msg: String, tag: String = TAG) {
        if (level <= VERBOSE) Log.v(tag, msg)
    }
    fun d(msg: String, tag: String = TAG) {
        if (level <= DEBUG) Log.d(tag, msg)
    }
    fun i(msg: String, tag: String = TAG) {
        if (level <= INFO) Log.i(tag, msg)
    }
    fun w(msg: String, tag: String = TAG) {
        if (level <= WARN) Log.w(tag, msg)
    }
    fun e(msg: String, tag: String = TAG) {
        if (level <= ERROR) Log.e(tag, msg)
    }
}
