package com.example.marsphotos

import com.example.marsphotos.fake.FakeDataSource
import com.example.marsphotos.fake.FakeNetworkMarsPhotosRepository
import com.example.marsphotos.rules.TestDispatcherRule
import com.example.marsphotos.ui.screens.MarsUiState
import com.example.marsphotos.ui.screens.MarsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

// MarsViewModel을 보면 init 블록에서 getMarsPhotos()를 실행한다.
// getMarsPhotos() 바디를 보면 viewModelScope.launch()가 있는데
// 이는 Main dispatcher 아래에 새 코루틴을 실행한다.
// 그런데 Main dispatcher는 Main thread에서 코루틴을 실행하는데
// Unit test는 Android 기기나 에뮬레이터가 아닌 워크스테이션에서 실행된다.
// 따라서 Main thread가 없다.
// 그래서 TestDispatcher를 사용하여 다른 코루틴 디스패처를 사용하도록 설정해준다.
class MarsViewModelTest {
    @get:Rule // Rule을 본 test에 적용
    val testDispatcher = TestDispatcherRule()


    @Test
    fun marsViewModel_getMarsPhotos_verifyMarsUiStateSuccess() =
        runTest {
            val marsViewModel = MarsViewModel(
                marsPhotosRepository = FakeNetworkMarsPhotosRepository()
            )
            assertEquals(
                MarsUiState.Success("Success: ${FakeDataSource.photosList.size} Mars " +
                        "photos retrieved"),
                marsViewModel.marsUiState
            )
        }
}