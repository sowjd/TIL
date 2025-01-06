# Modifier
Compose 내장 객체로, composable에 적용될 수 있는 설정을 저장한다.  
Border, padding, background, size, event handler, gesture 등 다양한 프로퍼티를 설정할 수 있다.  
> 나중에 앱 만들 때 씨름하게 될 부분이 아닐까.. 한다...;;

## 순서
`Modifier`의 각 함수는 이전 함수에서 반환한 Modifier를 변경한다.  
따라서 함수의 call 순서가 중요하다.  
```kotlin
@Composable
fun ArtistCard(/*...*/) {
    val padding = 16.dp
    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(padding)
            .fillMaxWidth()
    ) {
        // rest of the implementation
    }
}
```
위 코드에서 clickable이 padding보다 먼저 호출되었기 떄문에 padding 영역까지 클릭할 수 있다.  

## 크기
기본적으로 compose에서 제공되는 레이아웃은 하위 요소를 wrapping 한다.  
상위 래이아웃의 크기에 제한받지 않으려면 `requiredSize`를 사용한다.  

> Modifier는 이론보단 나중에 view를 직접 만들면서 부딪혀보는 게 나을 것 같다.
