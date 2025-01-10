package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ViewModel: UI State holders
class GameViewModel : ViewModel() {
    /*
    StateFlow
    - A SharedFlow that represents a read-only state with a single updatable data value
    that emits updates to the value to its collectors.

    SharedFlow
    - A hot Flow that shares emitted(내뿜다, 방출하다) values among all its collectors
    in a broadcast fashion, so that all collectors get all emitted values.
    - (방송 방식: 데이터를 전달하는 방법이나 방식이 방송처럼 모든 수집자에게 동시에 전달된다)

    MutableStateFlow
    - A mutable(변경가능한) StateFlow that provides a setter for value.

    https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=ko
    위 문서를 읽어보면 Flow API 안에 StateFlow, SharedFlow, MutableStateFlow가 있다.

    Flow (https://developer.android.com/kotlin/flow?hl=ko)
    - 데이터 스트림인데, 비동기적으로 생성하고 사용된다.
    - 코루틴을 기반으로 빌드 된다

    > 종합해보자면 main thread를 block 하지 않고, 코루틴에서 백그라운드로 작업하는데
    그 작업의 결과물을 ui에 표시하려면 ui는 state 기반이니까 flow에서 state를 가져오는 느낌이네
    */
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())

    /*
    asStateFlow()
    - make mutable(변경가능한) StateFlow -> read-only state
    - GameViewModel 안에서만 _uiState를 set(update)할 수 있고, 외부에는 uiState로 get(read)만 할 수 있게
     */
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    /*
    late init
    1. lateinit: 초기화 이후 값이 바뀔 수 있을 때 (∴ var 사용)
    2. by lazy: 초기화 이후 읽기 전용 값으로 사용할 때 (∴ val 사용)
    https://velog.io/@haero_kim/Kotlin-lateinit-vs-lazy-%EC%A0%95%ED%99%95%ED%9E%88-%EC%95%84%EC%84%B8%EC%9A%94
     */
    private lateinit var currentWord: String

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()

    /*
    mutableStateOf()는 변경 가능한 State 객체를 생성해서 리턴한다.
    이 반환된 State 인스턴스가 userGuess를 감싸게 되는데 (by 키워드로 delegation(위임) 했으므로)
    State는 getter, setter가 다 있다. 그래서 mutableStateOf()를 by로 사용할 때 backing property가 자동으로 생성된다.
     */
    var userGuess by mutableStateOf("")
        private set // 이게 참 볼때마다 적응이 안된닼ㅋㅋㅋㅋ

    init {
        resetGame()
    }

    // 랜덤으로 단어 하나 선택해서 중복 확인 하고,
    // 중복되지 않은 단어로 currentWord에 저장하고 currentWord를 섞은 String을 리턴한다.
    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    // 단어의 철자 순서를 섞는다
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    // 게임 시작, 다시 시작
    fun resetGame() {
        usedWords.clear()
        // _uiState 초기화 (pickRandomWordAndShuffle()를 사용하여 currentScrambledWord에 새 단어 선택)
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    // 사용자가 입력한 단어를 받는다.
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                // isGuessedWordWrong만 true로 설정하고 나머지 값은 그대로 copy
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }
}