https://developer.android.com/develop/ui/compose/lifecycle

# Compose 상태
상태: 시간에 따라 변경될 수 있는 값  
상태 변수가 일반 변수와 다른 점  
1. 새로운 값을 할당할 때 이전 할당값을 기억하고 있어야 한다.  
2. 상태가 바뀌면 컴포저블 함수 계층 트리 전체에 영향을 미친다 (재구성)  

## 재구성 (Recomposition)
부모 컴포저블의 상태값 변화가 모든 자식 컴포저블에 반영되며, 해당 상태가 전달되는 것을 의미한다.  
(한 컴포저블 함수에서 다른 함수로 전달된 데이터는 대부분 부모 함수에서 상태로서 선언된다.)  
- 재구성은 컴포저블 함수의 계층 안에서 상태값이 변경될 때 일어난다.  
(Compose will avoid recomposing them if their inputs haven't changed.)  
- 컴포즈는 재구성의 오버헤드를 줄이기 위해 해당 상태값을 읽는 함수들만 재구성하는 `지능적 재구성(Intelligent recomposition)`을 사용한다.  

## 상태 선언
[`MutableState`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/MutableState?hl=en) 객체로 감싸서 선언한다.  
`MutableState`는 `옵저버블 타입(Observable type)`으로 참조되는 컴포즈 클래스이다.  
```kotlin
// 1.
var textState = remember { mutableStateOf("") }
val onTextChange = { text : String -> textState.value = text }
TextField(value = textState.value, onValueChange = onTextChange)

// 2. 위임(Delegation) 키워드 by 사용 - 주로 사용하는 방법
var textState by remember { mutableStateOf("") }
val onTextChange = { text : String -> textState = text }
TextField(value = textState, onValueChange = onTextChange)

// 3. setter 사용 - Kotlin의 구조 분해 선언(destructuring declaration) 문법
// mutableStateOf("")가 반환하는 것은 MutableState<String> 객체인데 이 객체는
//  1) value: 상태 값 자체 (textValue에 해당)
//  2) value를 변경할 수 있는 setter (setText에 해당)
// 이 두 가지를 제공한다.
// 따라서 var (textValue, setText)는 MutableState<String>를 두 개의 변수 1) 과 2)로 분해한다.
var (textValue, setText) = remember { mutableStateOf("") }
val onTextChange = { text : String -> setText(text) }
TextField(value = textValue, onValueChange = onTextChange)
```

## 단방향 데이터 흐름 (Unidirectional data flow)
저장된 상태가 자식 컴포저블 함수에 의해 변경되어서는 안 된다는 개념이다.  
자식 컴포저블 함수에 이벤트 핸들러를 선언하고 자식 컴포저블에 해당 이벤트 핸들러를 전달하여 호출하도록 한다.  
```kotlin
@Composable
fun ParentComposableFunction() {
    var switchState by remember { mutableStateOf(true) }
    val onSwitchChange = { value : Boolean -> switchState = value }

    ChildComposableFunction(switchState = switchState, onSwitchChange = onSwitchChange)
}

@Composable
fun ChildComposableFunction(switchState : Boolean, onSwitchChange: (Boolean) -> Unit) {
    Column {
        Switch(
            checked = switchState,
            onCheckedChange = onSwitchChange
        )
    }
}
```

## 상태 호이스팅(State Hoisting)
상태를 자식 컴포저블에서 이를 호출한 부모 컴포저블로 들어 올리는 것을 말한다.  
```kotlin
@Composable
fun ComposableFunction() {
    var switchState by remember { mutableStateOf(true) }
    val onSwitchChange = { value : Boolean -> switchState = value }

    Column {
        Switch(
            checked = switchState,
            onCheckedChange = onSwitchChange
        )
    }
}
```
이 코드를 단방향 데이터 흐름의 예제 코드로 바꾸는 것이 상태 호이스팅의 예시이다.  
이렇게 바꾸게 된다면  
1. 자식 컴포저블을 비상태 컴포저블로 만들 수 있기 때문에 재사용성이 높아진다.
2. 상태는 다른 하위 컴포저블에도 전달할 수 있다.


## 환경설정을 변경했을 때 상태 저장하기
`remember` 키워드를 통해 상태값을 저장할 수 있지만, 환경설정이 변경될 때는 액티비티 형태를 바꾸는 것이기 때문에 액티비티 전체가 삭제되고 다시 생성된다.  
> 환경설정 변경이란? 기기 방향 변경, 시스템 폰트 변경 등  

이때 `rememberSaveable` 키워드를 사용하면 재구성뿐만 아니라 환경설정 변경 시에도 상태값을 유지할 수 있다.  
