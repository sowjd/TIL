package com.example.marsphotos.fake

import com.example.marsphotos.data.NetworkMarsPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMarsRepositoryTest {
    @Test
    fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList() =
        runTest { // 코루틴 테스트 (getMarsPhotos()가 suspend 함수이기 때문)
            val repository = NetworkMarsPhotosRepository(
                // MarsApiService를 FakeMarsApiService로 설정하면
                // NetworkMarsPhotosRepository의 코드만 테스트 할 수 있다.
                // MarsApiService를 넣으면 테스트에서 오류가 났을 때
                // MarsApiService에서 오류가 발생한 것일수도 있기 때문!
                marsApiService = FakeMarsApiService()
            )
            assertEquals(FakeDataSource.photosList, repository.getMarsPhotos())
        }
}