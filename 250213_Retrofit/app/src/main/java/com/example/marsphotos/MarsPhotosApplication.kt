package com.example.marsphotos

import android.app.Application
import com.example.marsphotos.data.AppContainer
import com.example.marsphotos.data.DefaultAppContainer

// Application 클래스의 서브클래스를 만들어 컨테이너 참조를 저장한다.
class MarsPhotosApplication : Application() {
    // lateinit: 초기화 없이 선언, 나중에 값을 할당
    // container는 onCreate() 호출 중에 초기화 되므로 lateinit을 사용함
    // 뷰 초기화, DI(의존성 주입, Dagger나 Hilt 등)에 자주 사용된다
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}