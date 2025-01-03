## [CompositionLocal](https://developer.android.com/develop/ui/compose/compositionlocal)
Compose는 data가 UI tree를 따라 아래로 흘러간다.  
그러나 이는 색상이나 스타일과 같이 자주 쓰이거나 널리 쓰이는 데이터에 대해서는 매번 자식 컴포저블로 전달 전달 해야 해서 번거롭다.  
이때 유용하게 쓰이는 것이 `CompositionLocal`이다.  

`CompositionLocal`은 자식 트리에만 데이터가 전달되도록 제한한다.  

### 사용법
1. 값 할당:  `provides`
2. 값 접근: `current` (상위 요소가 제공한 가장 가까운 값)

### 예시
```kotlin
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeDemoTheme {
        Composable1()
    }
}

val LocalColor = staticCompositionLocalOf { Color.Gray }

@Composable
fun Composable1() {
    Column {
        Text("Composable1", modifier = Modifier.background(LocalColor.current))
        Composable2()
        CompositionLocalProvider(LocalColor provides Color.Green) {
            Composable3()
        }
    }
}

@Composable
fun Composable2() {
    Text("Composable2", modifier = Modifier.background(LocalColor.current))
}

@Composable
fun Composable3() {
    Text("Composable3", modifier = Modifier.background(LocalColor.current))
    Composable4()
    CompositionLocalProvider(LocalColor provides Color.Yellow) {
        Composable5()
    }
}

@Composable
fun Composable4() {
    Text("Composable4", modifier = Modifier.background(LocalColor.current))
}

@Composable
fun Composable5() {
    Text("Composable5", modifier = Modifier.background(LocalColor.current))
}
```

<table>
   <tr>
       <td>트리 구조</td>
       <td>Preview 결과</td>
   </tr>
   <tr>
<td>
<img width="200" alt="Untitled" src="https://github.com/user-attachments/assets/1927a3ae-83d1-4431-b238-aabe3e6bdff9" />
</td>
<td>
<img width="160" alt="Screenshot" src="https://github.com/user-attachments/assets/7e90a7cc-b7e6-4421-9ba8-8985cf15a18a" />
</td>
</tr>
</table>

### 그렇다면 항상 `CompositionLocal`를 쓰는 것이 좋을까?
<table>
   <tr>
       <td>장점</td>
       <td>단점</td>
   </tr>
   <tr>
<td>
1) 자식 컴포저블로 전달, 전달, ... 하지 않아도 된다.
</td>
<td>
1) 컴포저블의 동작을 추론하기 어렵다.<br>
2) 문제가 발생할 때 앱을 디버깅하는 것이 어려울 수 있다.<br>
(트리를 다 탐색해서 <code>current</code> 값이 어디서 제공되었는지 확인해야 한다.)
</td>
</tr>
</table>

> 공식문서에서는 과도하게 사용하지 말 것은 권장한다. 대안으로는  
> 1) 자식 컴포저블에게 전달, 전달, ... 할 때 필요한 정보만 넘겨주거나 (명시적 매개변수 전달)
> 2) 자식 컴포저블이 수행했던 로직 처리를 부모 컴포저블에서 정의해서 핸들러를 넘겨준다. (컨트롤 역전)  

### 언제 `CompositionLocal`를 쓰는 것이 좋을까?
1. 명시적인 기본값이 있을 때
2. 일부 하위 요소가 아닌 모든 하위 요소에서 사용할 수 있을 때
