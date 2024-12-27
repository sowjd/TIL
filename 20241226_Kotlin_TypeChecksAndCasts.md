## is and !is operators
```kotlin
if (obj is String) print(obj.length)
if (obj !is String) print("Not a String") // Same as !(obj is String)
```

## Smart casts
Compiler가 자동으로 타입을 캐스팅해 주는 것을 말한다.

### 예시 1) Control flow
`if` `when` `while`문에서 동작한다.
```kotlin
when (x) { // 컴파일러는
    is Int -> print(x + 1) // x가 Int임을 알고
    is String -> print(x.length + 1) // x가 String임을 알고
    is IntArray -> print(x.sum()) // x가 IntArray임을 안다.
    // x는 컴파일러에 의해 각각 Int, String, IntArray로 smart casting 되었다.
}
```

### 예시 2) Logical operators
`&&` 또는 `||` 연산자의 왼쪽 부분이 type check이면 오른쪽 부분에서 smart casting이 동작한다.
```kotlin
if (x !is String || x.length == 0) return // x !is String이 false면, x.length에서 x는 String으로 smart casting 되었다.

if (x is String && x.length > 0) { // x is String이 true면, x.length에서 x는 String으로 smart casting 된다.
    print(x.length) // 여기도 마찬가지!
}
```

### 예시 3) Inline functions
```kotlin
interface Processor {
    fun process()
}

inline fun inlineAction(f: () -> Unit) = f()

fun nextProcessor(): Processor? = null

fun runProcessor(): Processor? {
    var processor: Processor? = null
    inlineAction {
        // The compiler knows that processor is a local variable and inlineAction()
        // is an inline function, so references to processor can't be leaked.
        // Therefore, it's safe to smart-cast processor.

        // If processor isn't null, processor is smart-cast
        if (processor != null) {
            // The compiler knows that processor isn't null, so no safe call
            // is needed
            processor.process()
        }

        processor = nextProcessor()
    }

    return processor
}
```
> TODO 무슨 말인지 이해가 잘 안되는데 function 공부하고 나서 다시 보자!


### 예시 4) Exception handling
Smart cast 정보는 `catch`와 `finally` 블록으로 전달된다.
```kotlin
fun testString() {
    var stringInput: String? = null
    // stringInput is smart-cast to String type
    stringInput = ""
    try {
        // The compiler knows that stringInput isn't null
        println(stringInput.length) // 0

        // The compiler rejects previous smart cast information for 
        // stringInput. Now stringInput has the String? type.
        stringInput = null

        // Trigger an exception
        if (2 > 1) throw Exception()
        stringInput = ""
    } catch (exception: Exception) {
        // The compiler knows stringInput can be null
        // so stringInput stays nullable.
        println(stringInput?.length) // null
    }
}
```

### Smart cast prerequisites
<table style="none">
    <tr>
        <td>
            <code>val</code> local variables
        </td>
        <td>
            Always, except <a href="delegated-properties.md">local delegated properties</a>.
        </td>
    </tr>
    <tr>
        <td>
            <code>val</code> properties
        </td>
        <td>
            If the property is <code>private</code>, <code>internal</code>, or if the check is performed in the same <a href="visibility-modifiers.md#modules">module</a> where the property is declared. Smart casts can't be used on <code>open</code> properties or properties that have custom getters.
        </td>
    </tr>
    <tr>
        <td>
            <code>var</code> local variables
        </td>
        <td>
            If the variable is not modified between the check and its usage, is not captured in a lambda that modifies it, and is not a local delegated property.
        </td>
    </tr>
    <tr>
        <td>
            <code>var</code> properties
        </td>
        <td>
            Never, because the variable can be modified at any time by other code.
        </td>
    </tr>
</table>

> TODO delegated property에 대한 개념을 모르니까 무슨 말인지 모르겠다..!!

## "Unsafe" cast operator
Cast가 불가능할 경우 exception을 발생시키는데 이 때문에 "unsafe"라고 한다.  
`as` 연산자를 사용한다.

```kotlin
val x: String = y as String // y가 null이면 null은 String 타입으로 casting 될 수 없으므로 exception 발생
val x: String? = y as String? // y가 null이면 null은 String? 타입으로 casting 될 수 있으므로 exception 발생 x
```

## "Safe" (nullable) cast operator
Safe cast란 cast 실패 시 exception을 발생시키지 않고 null을 리턴한다.  
`as?` 연산자를 사용한다.  
```kotlin
val x: String? = y as? String
```
