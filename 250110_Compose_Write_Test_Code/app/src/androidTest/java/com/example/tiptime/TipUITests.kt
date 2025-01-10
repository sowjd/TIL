/*
[ Instrumentation Test ]
1. 안드로이드 스튜디오 왼쪽 상단 [Android] 탭을 [Project]로 변경
2. [src] 우클릭 > [New] > [Directory] > [androidTest/java]
3. [androidTest/java] 우클릭 > New > Package > 테스트 하려는 함수의 package 입력
4. 3에서 만든 package 우클릭 > [New] > [Kotlin Class/File]
5. 4에서 만든 클래스에서 테스트 코드 작성
    @get:Rule
    val composeTestRule = createComposeRule()
 */
package com.example.tiptime

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITests {
    // UI(Compose) test는 MainActivity.kt onCreate()에서 호출하는 방식과 비슷해야한다.
    // 그래서 ComposeContentTestRule(여기서는 composeTestRule 변수)를 만들어 준다.
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            TipTimeTheme {
                TipTimeLayout()
            }
        }
        composeTestRule
            .onNodeWithText("Bill Amount") // Bill Amount라는 Text를 가진 Node(UI element)를 찾는다
            .performTextInput("10") // 그 Node에 10이라는 Text를 전달한다
        composeTestRule
            .onNodeWithText("Tip Percentage")
            .performTextInput("20")

        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        composeTestRule
            .onNodeWithText("Tip Amount: $expectedTip")
            .assertExists("No node with this text was found.") // "Tip Amount: $expectedTip"인 Node가 있는지 찾는다
    }
}