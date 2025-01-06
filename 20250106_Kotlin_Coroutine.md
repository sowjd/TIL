안드로이드는 애플리케이션이 시작될 때 런타임 시스템은 단일 thread를 생성한다.  
이 단일 thread가 흔히 말하는 main thread이다.  
Main thread에서 시간이 오래 걸리는 task를 수행하면 완료될 때까지 애플리케이션이 멈춘 것처럼 보인다.  
이때 안드로이드 운영체제는 유저에게 "응답하지 않습니다"는 메시지를 보여준다.  
이 메시지가 뜨는 것은 우리의 의도가 아니기 때문에 시간이 오래 걸리는 작업을 할 때 `코루틴(Coroutine)`을 사용한다.  

# 코루틴(Coroutine)
자신이 실행된 thread를 정지시키지 않으면서 비동기적으로 실행되는 **코드 블록**이다.  
코루틴 내부적으로는 매우 효율적인 다중 threading이 적용되어 있다.  
이번에 새로 나타난 개념이 아니라 1960년대부터 이미 존재했다.  

## 코루틴과 Thread
CPU는 많은 thread를 실행할 수 있지만 병렬적으로 실행될 수 있는 개수는 CPU 코어 개수이다.  
> 정말 아~~~주 짧은 딱! 그때!를 말하는 듯..! 그럼 그때는 CPU 코어 개수니까..!

일반적으로 CPU 코어 개수보다 많은 thread가 필요하면, 시스템은 thread scheduling을 통해 여러 코어가 thread들을 공유할 정책을 결정한다.  
그러나 코루틴은 이러한 오버헤드를 줄이기 위해 **활성화 상태의 thread pull을 유지하여 코루틴을 thread에 할당하는 방법을 관리**한다.  

1. 코루틴이 실행될 때마다 새로운 thread를 실행하고 코루틴이 종료될 때 해당 thread를 파기하지 않는다.  
2. 활성화된 코루틴이 중지되면 Kotlin 런타임이 해당 코루틴을 저장하고, 다른 코루틴이 재실행된다.
3. 재시작된 코루틴은 thread pull에 비어 있는 thread에서 완료 또는 중지될 때까지 실행된다.

이 방법은 제한된 숫자의 thread를 효과적으로 사용해서 task를 비동기적으로 처리하고,  
thread scheduling으로 인한 성능 저하를 일으키지 않으면서 멀티 테스킹을 수행할 수 있게 된다.  

> 책을 보며 공부 중이었는데, 코루틴 관련해서 설명이 좀 부족한 것 같아서 공식문서 ㄱㄱ

## [Coroutines basics](https://kotlinlang.org/docs/coroutines-basics.html)
### Your first coroutine
코루틴은 thread와 비슷한 면이 있지만 thread는 아니다.  
코루틴은 thread에서 실행되는 **코드 블록**이고, 한 thread에서 실행을 일시 중단하고 다른 thread에서 재개될 수 있다.  
```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}
// 결과
// Hello
// World!
```
`launch` coroutine builder이다.
> It launches a new coroutine concurrently with the rest of the code, which continues to work independently. That's why Hello has been printed first.  
라고 쓰여 있는데 [launch api 문서](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/launch.html)를 읽어보면  
current thread를 block 하지 않는다는 말인 것 같다.

`delay` coroutine을 특정 시간 동안 suspend 하는 함수이다.  
코루틴을 suspend 하는 동안 underlying thread를 block 하지 않는다. 즉, 다른 코루틴이 underlying thread를 사용할 수 있다.  

`runBlocking` coroutine builder이다.  
coroutine이 끝날 때까지 current thread를 block 한다.
> 그래서 잘 안 쓴다!!

#### Structured concurrency
코루틴은 structured concurrency 정책을 따른다.  
structured concurrency란 새로운 coroutine은 특정 `CoroutineScope`에서만 launch 될 수 있다는 것이다.  
`CoroutineScope`는 coroutine의 lifetime을 정한다.  
위 예제에서 `runBlocking`은 `println("World!")`가 끝날 때까지가 코루틴의 lifetime이다.  
실제 애플리케이션에는 많은 코루틴이 launch 되는데 structured concurrency 정책이  
1) 코루틴을 leak 되지 않게 하고
2) error가 적절하게 보고되고 lost 되지 않도록 보장한다.

### Extract function refactoring 
> Extract function refactoring이란 함수 안의 코드를 새로운 함수로 만드는 것을 말한다.  

위 예제에서 launch block을 `suspend` modifier를 사용해서 suspending function으로 만든다.  
suspending function은 코루틴 안에서 사용될 수 있다.  
또한 내부에 다른 suspending function을 사용하여(여기서는 `delay`) 코루틴의 실행을 suspend 할 수 있다.  
```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { doWorld() }
    println("Hello")
}

suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```

### An explicit job
`launch` coroutine builder는 `Job` 객체를 리턴한다.  
`Job` 객체는 coroutine을 관리할 수 있다. 예제를 보자!
```kotlin
val job = launch { // launch a new coroutine and keep a reference to its Job
    delay(1000L)
    println("World!")
}
println("Hello")
job.join() // wait until child coroutine completes
println("Done")

// 결과
// Hello
// World!
// Done
```

> 공식문서에 예제도 있고 다른 챕터도 많은데 하나씩 집중해서 공부해봐야겠다!!
