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

package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
// Data 클래스
// 컴파일러는 toString(), copy(), equals()와 같은 비교, 출력, 복사를 위한 유틸리티를 자동으로 생성
@Entity(tableName = "items") // Table 이름을 items로 설정함
data class Item(
    @PrimaryKey(autoGenerate = true) // 자동생성, 값을 할당하지 않아도 됨
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)
