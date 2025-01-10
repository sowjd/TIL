package com.example.unscramble.ui

// Model: UI State, data class로 구현

/*
파일명이 UiState라서 ViewModel에서 처리하는 UI의 상태와 헷갈린다..

Q. 어떤 데이터를 Model에 저장해야하고, 어떤 데이터를 ViewModel에 저장해야하는가?
A. 우선 지금까지의 내 결론은
 Model: 비지니스 로직과 관련된 데이터, DB에서 가져온 원본 데이터, API 호출 결과나 서버에서 가져오는 데이터
 ViewModel: UI와 관련된 상태 데이터, 모델 데이터를 화면에 표시하기 위해 가공한 데이터, 사용자 입력 데이터
 여전히 헷갈리긴 함..
 */
data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0, // UI에 표시가 됨 ∴ Model에 저장하는 데이터는 무조건 UI에 노출되지 않는다고 생각하면 안될듯..!
    val currentWordCount: Int = 1,
    val isGameOver: Boolean = false
)