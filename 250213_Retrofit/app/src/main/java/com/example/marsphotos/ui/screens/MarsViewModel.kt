/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel(
    // Constructor parameter 추가
    // ViewModelProvider.Factory를 사용하여 application container에서 값을 가져온다.
    private val marsPhotosRepository: MarsPhotosRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
        // viewModelScope: ViewModel에 정의된 기본 코루틴 스코프
        // ViewModel이 삭제되면 자동으로 취소된다.
        viewModelScope.launch {
            try {
                marsUiState = MarsUiState.Success(marsPhotosRepository.getMarsPhotos())
            } catch (e: IOException) {
//            Log.d("Jenny", "Fail to get photos. error: " + e.message)
                marsUiState = MarsUiState.Error
            }
        }
    }

    companion object { // companion? -> static이다 로 외워버려!
        // Factory: 변수명
        // ViewModelProvider.Factory: 타입
        // viewModelFactory {}: lifecycle-viewmodel-compose 라이브러리에서 제공하는 ViewModel Factory 생성 함수
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { // ViewModel을 어떻게 생성할지 정의
                // this[APPLICATION_KEY]: 현재 ViewModelStoreOwner에서 Application 객체를 가져옴
                // as MarsPhotosApplication: 가져온 Application 객체를 MarsPhotosApplication 타입으로 변환
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                // application.container: onCreate()에서 미리 생성된 Dependency Container
                val marsPhotosRepository = application.container.marsPhotosRepository
                // marsPhotosRepository를 constructor의 argument로 넘겨줌
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
        }
    }
}
