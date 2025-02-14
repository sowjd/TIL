package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

/*
Data Layer: https://developer.android.com/topic/architecture/data-layer
- 앱의 비즈니스 로직과 앱 데이터 소싱 및 저장을 담당한다.
- 데이터 레이어는 단방향 데이터 흐름 패턴을 사용하여 UI 레이어에 데이터를 노출한다.
- 데이터는 네트워크 요청, 로컬 데이터베이스, 기기의 파일 등 여러 소스에서 가져올 수 있다.

- 앱에 데이터 소스가 두 개 이상 있을 수도 있다.
(예: ⓵기기의 로컬DB에서 데이터를 먼저 가져오고 ②앱이 실행되는 동안 네트워크를 통해 최신 데이터 가져오기)

- 데이터 레이어는 하나 이상의 저장소로 구성된다.
- 앱에 사용되는 데이터 소스 유형별로 저장소가 있는 방식을 권장한다.

- 저장소 이름 규칙: 데이터 유형 + 저장소
 */

interface MarsPhotosRepository { // 데이터 소스를 추상화하는 저장소 클래스
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {
    override suspend fun getMarsPhotos(): List<MarsPhoto> = marsApiService.getPhotos()
    // 위 코드는 표현식 함수(Expression Body Function)인데 Java에 없는 문법이다.
    // (Java에서 람다 표현식으로 비슷하게 작성 가능하긴 하다)
    // 풀어서 쓴다면 이래와 같다.
    //    override suspend fun getMarsPhotos(): List<MarsPhoto> {
    //        return marsApiService.getPhotos()
    //    }
    // 함수 바디가 단일 표현식(한 줄)일 때만 가능하다.
}