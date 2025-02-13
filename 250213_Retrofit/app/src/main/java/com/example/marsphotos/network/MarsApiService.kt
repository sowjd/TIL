package com.example.marsphotos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    @GET("photos") // GET method, 엔드포인트는 photos
    suspend fun getPhotos(): String // 호출되면 BASE_URL에 /photos를 엔드포인트로 사용한다.
}

object MarsApi { // 싱글톤(Singleton) 객체 선언 따라서 앱이 실행될 때 한 번만 생성된다.
    val retrofitService : MarsApiService by lazy { // lazy 블록: retrofitService가 처음 사용될 때 실행됨
        retrofit.create(MarsApiService::class.java)
    }
}