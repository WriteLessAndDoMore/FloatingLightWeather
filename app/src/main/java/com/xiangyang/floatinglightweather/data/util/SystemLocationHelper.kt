package com.xiangyang.floatinglightweather.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.xiangyang.floatinglightweather.util.LogUtil
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 *  系统定位工具类
 */
object SystemLocationHelper {
    /**
     * 获取当前位置，挂起函数，告诉协程编译器这个函数里有等gps耗时操作，执行它时，当前线程可以干其他事情，等有结果了在继续走
     * @param context 接受上下文
     * @return 返回一个包含双精度浮点型经纬度的键值对，或者返回空
     */
    @SuppressLint("MissingPermission") // 告诉编译器我知道需要用户同意定位权限，在使用时已经申请
    suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? {
        // 开始执行一个可取消的协程转换闭包，把控制权交给continuation
        // 意思就是协程将控制权交给原生定位检查代码，什么时候拿到定位在通知协程，继续往下执行
        return suspendCancellableCoroutine { continuation ->
            // 获取定位服务
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            if (locationManager == null) {
                LogUtil.e("getCurrentLocation: locationManager is null")
                // 如果为null，说明没有获取到manager接下来也不用执行了，唤醒函数返回null
                continuation.resume(null) { _, _, _ -> }
                // 跳出当前协程Lambda闭包
                return@suspendCancellableCoroutine
            }
            // 选择GPS还是网络基站
            val provider = when {
                // 如果用户打开了GPS，那就用卫星定位，室外极准
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                // 如果用户没开GPS，但是网络定位开着，靠连着的WIFI或者基站定位，室内可用
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                // 如果都没开，那就什么都没有返回null
                else -> null
            }
            if (provider == null) {
                LogUtil.e("getCurrentLocation: 系统定位未开启(GPS/Network)")
                // 恢复协程返回 null，并紧急跳出闭包。
                continuation.resume(null) { _, _, _ -> }
                return@suspendCancellableCoroutine
            }
            // 真正开启GPS搜星要好几秒，为了让用户体验急速刷新，加个拦截，避免每次都搜星
            // 获取上一次已知的历史位置缓存。
            val locationLast = locationManager.getLastKnownLocation(provider)
            // 如果locationLast不是null并且上次位置距离当前时间未超过30s，说明用户未离开，可以直接使用
            if (locationLast != null && (System.currentTimeMillis() - locationLast.time) < 1000 * 30) {
                //恢复协程，把缓存的经纬度返回回去。
                continuation.resume(
                    Pair(
                        locationLast.longitude,
                        locationLast.latitude
                    )
                ) { _, _, _ -> }
                return@suspendCancellableCoroutine
            }
            // 如果执行到这，说明没有历史定位，需要我们获取
            // 创建一个传统的原生定位监听器匿名内部类对象。
            val locationListener = object : LocationListener {
                // 当位置发生改变时的回调方法
                override fun onLocationChanged(location: Location) {
                    // 判断协程还是否活跃，检查调用改方法的页面是否还存在，页面没关说明还活着
                    if (continuation.isActive) {
                        // 安全恢复协程，返回最新计算出来的经纬度。
                        continuation.resume(
                            Pair(
                                location.longitude,
                                location.latitude
                            )
                        ) { _, _, _ -> }
                    }
                    locationManager.removeUpdates(this)
                }
            }
            try {
                //请求位置更新，传入提供者、时间间隔、距离间隔、监听器
                locationManager.requestLocationUpdates(provider, 0L, 0f, locationListener)
            } catch (e: Exception) {
                LogUtil.e("getCurrentLocation 原生定位触发异常: ${e.message}")
                // 如果协程活着，返回 null。
                if (continuation.isActive) continuation.resume(null) { _, _, _ -> }
            }

            // 注册协程取消时的监听回调。如果用户退出了改页面，此时view model销毁，外面的协程就被cancel，一旦协程死了就会执行invokeOnCancellation
            continuation.invokeOnCancellation {
                // 移除监听器，协程都死了，直接取消获取定位，防止内存泄漏
                locationManager.removeUpdates(locationListener)
            }
        }
    }
}