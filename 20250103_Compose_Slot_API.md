# Slot API
런타임에 동적으로 컴포저블을 삽입하는 API(Application Programming Interface)이다.  
함수 인자로 composable function을 선언하여 구현한다.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                SlotDemo(
                    { slotComposable() }
                )
            }
        }
    }
}

@Composable
fun SlotDemo(slot : @Composable () -> Unit) {
    Column {
        Text("start")
        slot()
        Text("end")
    }
}

@Composable
fun slotComposable() {
    Button(onClick = {}) { Text("Hello") }
}
```

## 여러 개의 slot 선언하기
```kotlin
@Composable
fun SlotDemo(slot1 : @Composable () -> Unit,
             slot2 : @Composable () -> Unit,
             slot3 : @Composable () -> Unit) {
                // ...
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                SlotDemo(
                    { slotComposable1() },
                    { slotComposable2() },
                    { slotComposable3() }
                )
            }
        }
    }
}
```
> Slot API는 view 만들 때 유용하게 쓰일 것 같기도..?!
