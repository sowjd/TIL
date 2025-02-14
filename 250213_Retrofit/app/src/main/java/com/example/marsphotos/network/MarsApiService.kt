package com.example.marsphotos.network

import retrofit2.http.GET

interface MarsApiService {
    @GET("photos") // GET method, 엔드포인트는 photos
    suspend fun getPhotos(): List<MarsPhoto> // 호출되면 BASE_URL에 /photos를 엔드포인트로 사용한다.
}