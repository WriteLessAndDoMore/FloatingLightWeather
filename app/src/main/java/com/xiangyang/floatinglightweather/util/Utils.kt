package com.xiangyang.floatinglightweather.util

import android.content.Intent
import android.widget.Toast
import com.xiangyang.floatinglightweather.base.WeatherApplication.Companion.context
import com.xiangyang.floatinglightweather.constant.GDConstant

fun <T> T.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, toString(), duration).show()
}

inline fun <reified T> startActivity(block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java).apply {
        block()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}