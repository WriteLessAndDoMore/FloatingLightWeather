package com.xiangyang.utillibrary

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

typealias PermissionCallBack = (Boolean, List<String>) -> Unit

class InvisibleFragment : Fragment() {
    private var callback: PermissionCallBack? = null

    // 注册现代化的权限请求启动器
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result: Map<String, Boolean> ->
        // 在回调中处理结果
        result.forEach { (key, value) ->
            Log.i("DXY", "registerForActivityResult key:$key, value:$value")
        }

        // 过滤拿到未授权的权限，拿到这些权限名，打包到List中
        val deniedList = result.filterValues { !it }.keys.toList()
        val allGranted = deniedList.isEmpty()
        callback?.invoke(allGranted, deniedList)
    }

    fun requestNow(cb: PermissionCallBack, vararg permission: String) {
        callback = cb
        // 检查是否有未授权的权限，如果全过了直接回调，没过才发起请求
        val context = context
        if (context == null) {
            cb.invoke(false, permission.toList())
            return
        }

        val needToRequest = permission.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needToRequest.isEmpty()) {
            cb.invoke(true, emptyList())
        } else {
            permissionLauncher.launch(
                needToRequest.toTypedArray()
            )
        }
    }
}