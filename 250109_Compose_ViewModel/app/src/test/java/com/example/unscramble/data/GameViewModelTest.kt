/*
https://developer.android.com/codelabs/basic-android-kotlin-compose-test-viewmodel?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-4-pathway-1%3Fhl%3Dko%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-test-viewmodel#3
[ Unit Test ]
1. Success path(happy path tests)
 - 예외나 오류가 없는 흐름
 - 앱의 의도에 맞는 동작이기 때문에 테스트 시나리오 작성이 쉽다.
2. Error path
 - 앱의 오류 조건 or 잘못된 사용자의 입력에 대한 응답을 test
 - 가능한 오류 흐름을 모두 파악하는 것이 어렵다.
 ∴ 우선 처음에는 예상할 수 있는 오류 조건에 대해 나열하고, 그것들을 테스트하다 보면 다양한 시나리오가 발견될 것이다.
 테스트하면서 점점 개선해 나가는 것이 좋다.
3. Boundary case
 - 앱의 경계 조건에 테스트한다.
 - 지금 앱을 예로 들자면, 1) 앱이 로드될 때의 UI 상태 2) 사용자가 최대 단어 수를 play 한 후의 UI 상태를 확인한다.


 [ thingUnderTest_TriggerOfTest_ResultOfTest 형식 ]
 - 단위 테스트에서 흔히 사용되는 naming convention
 - 어떤 입력이 어떤 동작을 일으켰고, 그 결과가 무엇인지 직관적으로 알 수 있다.

 1) thingUnderTest(테스트 대상): 테스트하려는 주요 객체 또는 메소드
 2) TriggerOfTest(테스트를 유발하는 트리거): 동작, 조건 입력 등
 3) ResultOfTest는(테스트 결과): 검증할 상태로써 출력값, 상태 변화, 동작의 확인 등


 [ Test Coverage 확인 ]
 좌측 탭에서 현재 파일 우클릭 후 [Run ‘GameViewModelTest' with Coverage] 실행
 */
package com.example.unscramble.data

import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    // GameViewModel이 인스턴스로 만들어지고 init까지 실행된 상태
    // 각 @Test 메소드마다 GameViewModelTest가 인스턴스화 된다. (viewModel이 공유되지 않는다!)
    private val viewModel = GameViewModel()

    // 1. Success path(happy path tests)
    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset()  {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(20, currentGameUiState.score)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    // 2. Error path
    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        // Assert that score is unchanged
        assertEquals(0, currentGameUiState.score)
        // Assert that checkUserGuess() method updates isGuessedWordWrong correctly
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    // 3. Boundary case
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        // Assert that current word is scrambled.
        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)
        // Assert that current word count is set to 1.
        assertTrue(gameUiState.currentWordCount == 1)
        // Assert that initially the score is 0.
        assertTrue(gameUiState.score == 0)
        // Assert that the wrong word guessed is false.
        assertFalse(gameUiState.isGuessedWordWrong)
        // Assert that game is not over.
        assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            // Assert that after each correct answer, score is updated correctly.
            assertEquals(expectedScore, currentGameUiState.score)
        }

        // Assert that after all questions are answered, the current word count is up-to-date.
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        // Assert that after 10 questions are answered, the game is over.
        assertTrue(currentGameUiState.isGameOver)
    }
}