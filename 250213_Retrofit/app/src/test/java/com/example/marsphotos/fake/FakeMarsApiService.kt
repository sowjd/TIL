package com.example.marsphotos.fake

import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

// 저장소가 API 서비스에 종속되기 때문에
// FakeDataSource를 리던해줄 fake API 서비스를 만든다.
class FakeMarsApiService : MarsApiService {
    override suspend fun getPhotos(): List<MarsPhoto> {
        return FakeDataSource.photosList
    }
}