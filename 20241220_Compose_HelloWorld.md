## Android Studio 최신버전(Ladybug) 설치
> 회사에서 기존 프로젝트 개발할 때 안스 버전이 달라서 빌드 오류가 발생했었다.  
> 지금까지 Chipmunk 버전을 사용했는데 낯설기도 하고 익숙하기도 한 Ladybug..!!

### Logcat
* 로그 쿼리 `key:value`로 검색
* key 예시: tag, package, process, message, level, age
  > tag, process, message를 많이 쓸 듯!
* 정규식 작성 가능
참고 https://developer.android.com/studio/debug/logcat?hl=ko

## Jetpack Compose
Android’s recommended modern toolkit for **building native UI**

### Composable Function
Jetpack Compose를 사용하여 UI를 생성하는 데 사용되는 함수이다.
* `@Composable` annotation으로 표시한다 (Kotlin compiler에게 알리는 역할)
* 함수 이름은 대문자로 표기한다.
* 아무것도 반환할 수 없다.

## Android Compose New Project 생성
> `build.gradle`, `settings.gradle`이 `build.gradle.kt`, `settings.gradle.kt`으로 생성되네...? 이건 뭐지..?

https://developer.android.com/build/migrate-to-kotlin-dsl를 읽어보면 Android Gradle plugin 4.0에서 Gradle build configuration에 Kotlin 사용을 지원한다고 한다.  
Kotlin이 Groovy보다 더 readable하고 더 나은 compile-time checking과 IDE support를 제공한다고 하는데, 빌드 측면에서는 Kotlin이 Groovy보다 조금 느린 경향이 있다고 한다.  

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { // composable function을 사용하여 레이아웃을 정의한다
            ComposeDemoTheme { // ComposeDemoTheme은 composable function이고, ui.theme/Theme.kt에 선언되어 있다
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    log.info("Jenny, innerPadding: " + innerPadding)
                }
            }
        }
    }
}

// Composable function은 setContent() 또는 다른 composable function에서 호출할 수 있다.
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// @Preview annotation: 미리보기 함수
// showBackground = true라는 매개변수를 사용하고 있는데 미리보기에 배경을 추가하라는 뜻이다
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting("Android")
    }
}
```
