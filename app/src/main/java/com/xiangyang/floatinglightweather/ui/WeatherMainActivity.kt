package com.xiangyang.floatinglightweather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.xiangyang.floatinglightweather.R
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.viewmodel.AdCodeStatus
import com.xiangyang.floatinglightweather.viewmodel.ReGeoCodeViewModel
import com.xiangyang.utillibrary.PermissionX.requestPermissionsAsync
import kotlinx.coroutines.launch

class WeatherMainActivity : AppCompatActivity() {
    private val reGeoCodeViewModel: ReGeoCodeViewModel by viewModels()

    // 注册现代权限申请回调（必须声明在类级别变量位置）
    // registerForActivityResult(...)：向系统注册一个“异步结果通道”。
    // ActivityResultContracts.RequestMultiplePermissions()：告诉系统，这个通道是专门用来“同时申请多个权限”的。
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 这是一个 Lambda 闭包（回调大括号）。只要用户在系统弹窗上点了“允许”或“拒绝”，
            // 系统就会立刻带着结果回来执行这个大括号里面的代码。 参数 permissions 是一个 Map<String, Boolean>，里面存着每个权限是否被同意。
            //从系统返回的 Map 结果中，取出精准定位（FINE）和粗略定位（COARSE）的授权结果。如果找不到该权限的状态，默认赋值为 false（未通过）。
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            if (fineGranted || coarseGranted) {
                // 只要用户至少同意了其中一个定位权限，再去底层获取GPS定位
                reGeoCodeViewModel.loadWeatherByLocation(this)
            } else {
                // 用户无情拒绝，直接弹个 Toast 提示他
                Toast.makeText(this, "由于您拒绝了定位权限，无法自动获取当前天气", Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_main)
        initObserve()
        // 核心时序改变：进来后不直接定位，而是先检查权限
        lifecycleScope.launch {
            val pair = requestPermissionsAsync(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (pair.first) {
                reGeoCodeViewModel.loadWeatherByLocation(this@WeatherMainActivity)
            } else {
                Toast.makeText(
                    this@WeatherMainActivity,
                    "由于您已经拒绝了定位权限，无法自动获取当前天气",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
//        checkAndRequestPermissions()
    }

    /**
     * 检查与请求权限的逻辑
     */
    private fun checkAndRequestPermissions() {
        // 向系统静默查询，用户“以前”有没有给过这个 App 定位权限。
        val hasFine = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (hasFine || hasCoarse) {
            // 之前给过并且是允许状态，直接底层获取GPS定位
            reGeoCodeViewModel.loadWeatherByLocation(this)
        } else {
            // 用户第一次打开或者之前没有授权，使用上面注册的“小助手”拉起权限弹窗
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        }
    }

    private fun initObserve() {
        reGeoCodeViewModel.adCodeStatusLiveData.observe(this) { status ->
            when (status) {
                is AdCodeStatus.LocationFail -> Toast.makeText(
                    this,
                    "定位服务不可用，请检查GPS或网络设置",
                    Toast.LENGTH_SHORT
                ).show()

                is AdCodeStatus.ResponseFail -> Toast.makeText(
                    this,
                    "数据请求失败",
                    Toast.LENGTH_SHORT
                ).show()

                is AdCodeStatus.ResponseSuccess -> {
                    val intent = Intent(this, WeatherActivity::class.java).apply {
                        putExtra(GDConstant.GeneralConstant.IntentKey.CITY_AD_CODE, status.adCode)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}