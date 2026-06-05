package com.xiangyang.floatinglightweather.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    // 高德服务器根路径
    private const val GAO_DE_BASE_URL = "https://restapi.amap.com/"

    // 创建Retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(GAO_DE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 传入service的Class类型给到retrofit的create方法，然后它动态代理返回service的实例
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    // 利用kotlin中泛型实体化
    inline fun <reified T> create(): T = create(T::class.java)


}