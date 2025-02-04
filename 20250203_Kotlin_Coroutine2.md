> 약 한 달 동안 Figma로 작성된 UI look을 실제 안드로이드 앱의 view로 만드는 작업을 했다.  
> Column, Row, Box, Button, Image, Modify가 가장 많이 타이핑하지 않았나.. 싶다ㅋㅋㅋ  
> 앞으로 할 일은  
> 1. View에서 발생하는 이벤트에 대한 처리 로직을 넣고 (예: 버튼 클릭 이벤트)
> 2. API 통신으로 서버에서 데이터를 가져와서 출력해 주고
> 3. 서버에 업데이트가 필요한 데이터가 있다면 업데이트 요청하는 것   
> 4. 구현해 놓은 view에서 스크롤 가능한 컴포넌트들을 LazyColumn 및 LazyRow로 변경  
>
> 정도이지 않을까.. 한다.
---------
공부 자료  
https://developer.android.com/courses/pathways/android-basics-compose-unit-5-pathway-1?hl=ko#codelab-https://developer.android.com/codelabs/basic-android-kotlin-compose-coroutines-kotlin-playground

<table>
   <tr>
       <td>동기 코드</td>
       <td>비동기 코드</td>
   </tr>
   <tr>
<td>

```kotlin
import kotlinx.coroutines.*

fun main() {
    val time = measureTimeMillis { // 실행 시간 측정
        runBlocking {
            println("Weather forecast")
            printForecast()
            printTemperature()
        }
    }
    println("Execution time: ${time / 1000.0} seconds")
}
suspend fun printForecast() {
    delay(1000)
    println("Sunny")
}

suspend fun printTemperature() {
    delay(1000)
    println("30\u00b0C")
} 
```
Weather forecast 출력 -> 1초 기다림 -> Sunny 출력 -> 1초 기다림 -> 30°C 출력  
약 2.1초 소요

</td>
<td>

```kotlin
fun main() {
    val time = measureTimeMillis { // 실행 시간 측정
        runBlocking {
            println("Weather forecast")
            launch { printForecast() }
            launch { printTemperature() }
        }
    }
    println("Execution time: ${time / 1000.0} seconds")
}
suspend fun printForecast() {
    delay(1000)
    println("Sunny")
}

suspend fun printTemperature() {
    delay(1000)
    println("30\u00b0C")
} 
```
Weather forecast 출력 -> 1초 기다림 -> Sunny 출력
                     -> 1초 기다림 -> 30°C 출력  
약 1.1초 소요
</td>
</tr>
</table>


## async(): 코루틴 결과값이 필요할 때
```kotlin
suspend fun getForecast(): String {
    delay(1000)
    return "Sunny"
}

suspend fun getTemperature(): String {
    delay(1000)
    return "30\u00b0C"
}

fun main() {
    runBlocking {
        println("Weather forecast")
        val forecast: Deferred<String> = async {
            getForecast()
        }
        val temperature: Deferred<String> = async {
            getTemperature()
        }
        println("${forecast.await()} ${temperature.await()}")
    }
}
```
`async()`는 `Deferred` 객체를 리턴한다.  
`Deferred` 객체에서 `await()`를 호출하여 코루틴의 결과값을 얻을 수 있다.  

## 코루틴에서 오류 발생 했을 때
```kotlin
suspend fun getForecast(): String {
    delay(1000)
    return "Sunny"
}

suspend fun getTemperature(): String {
    delay(500)
    throw AssertionError("Temperature is invalid")
    return "30\u00b0C"
}

fun main() {
    runBlocking {
        println("Weather forecast") // 1
        val forecast: Deferred<String> = async { // 2
            getForecast()
        }
        val temperature: Deferred<String> = async { // 3
            getTemperature()
        }
        println("${forecast.await()} ${temperature.await()}") // 4
    }
}
```
코루틴은 상위-하위(parent-child) 관계가 있는데 getForecast()와 getTemperature()는 같은 상위 코루틴에 속한다.  
하위 코루틴 중 하나가 예외와 함께 실패한다면
 1. 상위로 전파됨 (상향 전파, propagating the error upwards)
 2. 상위 코루틴이 취소됨 ∴ 상위 코루틴의 모든 하위 코루틴이 취소됨(3, 4)
  이때 자기 자신도 하위 코루틴 중 하나이므로 취소됨
 3. 오류가 위로 전파됨
 4. 프로그램이 AssertionError로 종료됨

## 코루틴 취소
`Deferred` 객체에서 `cancel()`을 호출  
아래로 전파된다.  



## Job
`launch()`로 코루틴을 실행하면 `Job` 인스턴스가 리턴된다.(`async()`로 리턴된 `Deferred` 객체도 Job이다.)
Job을 사용하면 코루틴의 활성, 취소, 완료 등의 상태를 확인할 수 있다.  

## CoroutineScope
코루틴은 `CoroutineScope`로 실행되는데, 이는 코루틴의 수명주기와 연결된다.  
코루틴이 관리되지 않아서 발생하는 리소스 낭비를 방지한다.  

## CoroutineContext
코루틴이 실행될 컨텍스트에 관한 정보  
\* 괄호 안은 기본값
1) 이름 - 코루틴을 고유하게 식별하는 이름 (coroutine)
2) 작업 - 코루틴의 수명 주기를 제어함 (상위 작업 없음)
3) 디스패처 - 작업을 적절한 스레드에 전달함 (Dispatchers.Default)
4) 예외 핸들러 - 코루틴에서 실행되는 코드에서 발생하는 예외를 처리함 (예외 핸들러 없음)

## 디스패처 (Dispatcher)
코루틴은 디스패처를 사용하여 실행에 사용할 thread를 결정

1. `Dispatchers.Main`
- Main thread에서 코루틴을 실행한다.
- 주로 UI 업데이트 및 상호작용을 처리하고 빠른 작업을 실행하는 데 사용
2. `Dispatchers.IO`
- 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있다.
- 예를 들어 파일에서 읽거나 파일에 쓰고 네트워크 작업을 실행한다.
3. `Dispatchers.Default`
- Context에 디스패처가 지정되지 않은 상태에서 launch() 및 async()를 호출할 때 사용되는 기본 디스패처
- 계산이 많은 작업을 실행한다.

Main thread에서 시작된 코루틴의 일부 특정 작업을 다른 thread에서 실행하려면  
`withContext`를 사용하여 디스패처를 전환할 수 있다.