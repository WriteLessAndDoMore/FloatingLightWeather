package com.xiangyang.utillibrary

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine

object PermissionX {
    private const val TAG = "InvisibleFragment"
    fun request(
        activity: FragmentActivity,
        vararg permission: String,
        callBack: PermissionCallBack
    ) {
        val fragmentManager = activity.supportFragmentManager
        var invisibleFragment = fragmentManager.findFragmentByTag(TAG) as? InvisibleFragment
        if (invisibleFragment == null) {
            invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG)
                .commitNowAllowingStateLoss()
        }
        invisibleFragment.requestNow(callBack, *permission)
    }

    suspend fun FragmentActivity.requestPermissionsAsync(vararg permission: String): Pair<Boolean, List<String>> {
        return suspendCancellableCoroutine { continuation ->
            // 协程将控制权交给callback，什么时候执行完结果在通知协程继续执行
            request(this, *permission) { allGranted, deniedList ->
                if (continuation.isActive) {
                    continuation.resume(Pair(allGranted, deniedList)) { _, _, _ -> }
                }
            }
        }
    }
}