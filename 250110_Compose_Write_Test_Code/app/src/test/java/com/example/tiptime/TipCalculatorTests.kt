/*
[ Local Test ]
1. 안드로이드 스튜디오 왼쪽 상단 [Android] 탭을 [Project]로 변경
2. [src] 우클릭 > [New] > [Directory] > [test/java]
3. [test/java] 우클릭 > New > Package > 테스트 하려는 함수의 package 입력
4. 3에서 만든 package 우클릭 > [New] > [Kotlin Class/File]
5. 4에서 만든 클래스에서 테스트 코드 작성
    - import org.junit.Assert.*
    - import org.junit.Test
 */

package com.example.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

class TipCalculatorTests {
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}