package com.example.marsphotos.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// https://developer.android.com/kotlin/coroutines/test?hl=ko#testdispatchers
class TestDispatcherRule(
    // TestDispatcher: 테스트 목적으로 사용하는 CoroutineDispatcher
    // 테스트 중에 새 코루틴을 만드는 경우 TestDispatcher를 사용해야 한다.

    // 사용할 수 있는 TestDispatcher에는
    // UnconfinedTestDispatcher: Task가 특정 순서로 실행되지 않도록 지정
    // 코루틴이 자동으로 처리되므로 간단한 테스트에 적합
    // 복잡한 테스트에서는 StandardTestDispatcher를 사용
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) { // Task가 실행되기 전에 실행됨
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}