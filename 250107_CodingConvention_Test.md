# 스타일 가이드라인
## 코틀린
클래스: upper camel case, 명사 또는 명사구  
함수: 소문자로 시작하는 camel case, no underscores, 동사 또는 동사구  
https://kotlinlang.org/docs/coding-conventions.html#property-names

## 컴포즈
컴포저블 함수: upper camel case, 명사(명사를 설명하는 형용사가 앞에 붙을 수 있다)  
[API Guidelines for Jetpack Compose](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md)  
[API Guidelines for @Composable components in Jetpack Compose](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md)
> 여러 기술 블로그를 읽어보니 첫 번째 링크를 중점적으로 파악하면 되는 듯하다!

# 테스트
[테스트 작성 코드랩](https://developer.android.com/codelabs/basic-android-kotlin-compose-write-automated-tests?hl=ko&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-2-pathway-3%3Fhl%3Dko%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-write-automated-tests#0)  
> 테스트 코드 작성이 처음이라면 이 코드랩이 기본 방법을 익히기 좋은 자료라고 생각한다.

[[DroidKnights 2024] 김수현 - Compose UI 컴포넌트 설계와 테스트](https://youtu.be/_MJKDp9PBaA?si=JW47SRW0qPH94bZt)  
> 이 발표에서 얻은 인사이트는 
> 1. 이미 선언형 UI 프레임워크로 많은 정보가 있는 React 진영에서 어떤 설계와 테스트를 하고 있는지 참고하기
> 2. [Android Code Search](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/androidInstrumentedTest/kotlin/androidx/compose/material3/AppBarTest.kt)에서 test 코드 참고하기

> 개인적으로 2번이 좋은 공부 자료가 될 것 같다. 생각지도 못한 방법이었는데 발표자의 아이디어가 완전 👍🏻

# 기타 - 공부 순서
코틀린, 컴포즈가 처음이라면 아래 사이트에서 입문부터 순서대로 공부해 나가면 좋을 것 같다.  
https://developer.android.com/courses?hl=ko
