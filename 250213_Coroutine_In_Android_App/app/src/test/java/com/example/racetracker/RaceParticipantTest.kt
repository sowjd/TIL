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
package com.example.racetracker

import com.example.racetracker.ui.RaceParticipant
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RaceParticipantTest {
    private val raceParticipant = RaceParticipant(
        name = "Test",
        maxProgress = 100,
        progressDelayMillis = 500L,
        initialProgress = 0,
        progressIncrement = 1
    )

    // 레이스 시작 후 진행률 테스트
    @Test
    fun raceParticipant_RaceStarted_ProgressUpdated() = runTest {
        val expectedProgress = 1
        launch { raceParticipant.run() } // 새로운 코루틴에서 실행 (비동기 실행) => why?
        // raceParticipant.run()에 delay()가 있는데 테스트가 delay() 만큼 기다려야하기 때문, advanceTimeBy()도 실행 안됨
        advanceTimeBy(raceParticipant.progressDelayMillis) // progressDelayMillis 시간 만큼 시간을 앞으로 이동 (기다림 없음)
        runCurrent() // 현재 대기중인 코루틴을 즉시 실행, advanceTimeBy()만으로 실행이 보장되지 않을 수도 있기 때문에 추가함.
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    // 레이스 완료 후 진행률 테스트
    @Test
    fun raceParticipant_RaceFinished_ProgressUpdated() = runTest {
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(100, raceParticipant.currentProgress)
    }
}
