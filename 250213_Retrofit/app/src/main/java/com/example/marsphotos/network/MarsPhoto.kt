package com.example.marsphotos.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable // MarsPhoto 클래스를 직렬화 한다.
data class MarsPhoto(
    // 멤버 변수는 JSON 객체의 key 값에 맵핑된다.
    val id: String,
    @SerialName(value = "img_src") // JSON의 key인 img_src를 imgSrc와 맵핑한다.
    val imgSrc: String
)