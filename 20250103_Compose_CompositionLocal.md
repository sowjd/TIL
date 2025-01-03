## [CompositionLocal](https://developer.android.com/develop/ui/compose/compositionlocal)
Compose는 data가 UI tree를 따라 아래로 흘러간다.  
그러나 이는 색상이나 스타일과 같이 자주 쓰이거나 널리 쓰이는 데이터에 대해서는 매번 자식 컴포저블로 전달 전달 해야 해서 번거롭다.  
이때 유용하게 쓰이는 것이 `CompositionLocal`이다.  

`CompositionLocal`은 자식 트리에만 데이터가 전달되도록 제한한다.  

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
