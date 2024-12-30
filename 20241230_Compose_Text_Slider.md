## [Text](https://developer.android.com/develop/ui/compose/text/style-text?hl=ko)
> fontSize에 sp 단위를 쓰는 것을 보았다.  
dp는 들어봤는데 sp가 뭘까..?  

`sp`(scalable pixels): 텍스트 크기를 정의할 때 사용하는 단위로, 기본적으로 dp와 같은 크기이지만 *사용자 설정 (안드로이드 환경설정에서 글꼴 크기 선택)* 에 따라 크기가 조정된다.  
`dp`(density-independent pixels): 공식 문서에서 virtual pixel unit이라고 표현하는데, 스크린 밀도가 달라도 같은 비율로 보이게 한다.  

출처: https://developer.android.com/training/multiscreen/screendensities?hl=ko  


## [Slider](https://developer.android.com/develop/ui/compose/components/slider)
<img width="219" alt="slider" src="https://github.com/user-attachments/assets/76403698-7669-4b1b-87fe-6143f70842ac" />

> 작은 요소 하나도 다 이름이 있는 걸 보면 UI도 큰 영역이란 것이 체감된다..!

## 예제 코드
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                DemoScreen()
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDemoScreen() {
    ComposeDemoTheme {
        DemoScreen()
    }
}

@Composable
fun DemoScreen() {
    // var sliderPosition = 20f 으로 하면 값 변경이 반영되지 않는다.
    // Slider 값이 변경되면 DemoScreen()에서 호출하는 모든 composable들이 재구성되는데
    // 이때 slider에서 변경된 값을 기억해야 한다고 설정하는 부분이다. 나중에 더 공부해 보자!!
    var sliderPosition by remember { mutableStateOf(20f) }

    val handlePositionChange = { position : Float -> sliderPosition = position }
    Column( // place items vertically on the screen
        // https://developer.android.com/develop/ui/compose/layouts/basics
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DemoText(message = "Hello World😆", fontSize = sliderPosition)
        Spacer(modifier = Modifier.height(100.dp))
        DemoSlider(sliderPosition = sliderPosition, onPositionChange = handlePositionChange)
        Text(text = sliderPosition.toInt().toString() + "sp")
    }
}

@Composable
fun DemoText(message: String, fontSize: Float) {
    Text(text = message, fontSize = fontSize.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun DemoSlider(sliderPosition: Float, onPositionChange: (Float) -> Unit) {
    Slider(
        modifier = Modifier.padding(10.dp),
        valueRange = 20f..40f, // The range of values that the slider can take
        value = sliderPosition, // The current value of the slider
        onValueChange = { onPositionChange(it) } // A lambda that gets called every time the value is changed
    )
}
```
