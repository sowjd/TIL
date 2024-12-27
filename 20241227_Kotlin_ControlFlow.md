# 1. Conditions and loops
## If expression
Kotlin에서 `if`는 expression이다. 즉, 값을 리턴한다는 뜻이다.  
그러므로 `condition ? then : else`와 같은 3항 연산자는 없다.  
`if` expression이 값을 리턴하는 경우, `else`는 꼭 있어야 한다.  
```kotlin
var max;
if (a > b) max = a
else max = b

// expression이기 때문에 가능!
val maxLimit = 1
val maxOrLimit = if (maxLimit > a) maxLimit else if (a > b) a else b
```

## When expressions and statements
Java의 `switch`문과 비슷하다.  
위에서부터 순차적으로 판단하고, 일치하는 조건을 만나면 이후 조건들은 판단하지 않는다.
> Java `switch`문에서는 `break`를 매번 써줘야 했는데 `break`가 내장(?)되어 있다고 볼 수 있다.

`else`는 Java `swtich`문의 `default`처럼 모든 case가 만족하지 않을 때 실행된다.  


### 1) 제목에서 알 수 있듯, expression과 statement 둘 다로도 사용할 수 있다.
<table>
   <tr>
       <td>Expression</td>
       <td>Statement</td>
   </tr>
   <tr>
<td>

```kotlin
// Returns a string assigned to the 
// text variable
val text = when (x) {
    1 -> "x == 1"
    2 -> "x == 2"
    else -> "x is neither 1 nor 2"
}
```

</td>
<td>

```kotlin
// Returns nothing but triggers a 
// print statement
when (x) {
    1 -> print("x == 1")
    2 -> print("x == 2")
    else -> print("x is neither 1 nor 2")
}
```

</td>
</tr>
</table>

### 2) subject 없이 사용할 수 있다.
단 subject가 없다면 모든 가능한 case를 cover 하던지, `else` branch를 사용해야 한다.  
그렇지 않으면 compile error가 발생한다.  
<table>
   <tr>
       <td>모든 case cover</td>
       <td>모든 case cover X</td>
   </tr>
   <tr>
<td>

```kotlin
enum class Bit {
    ZERO, ONE
}

val numericValue = when (getRandomBit()) {
    Bit.ZERO -> 0
    Bit.ONE -> 1
}
```

</td>
<td>

```kotlin
when {
    a > b -> "a is greater than b"
    a < b -> "a is less than b"
    else -> "a is equal to b"
}
```

</td>
</tr>
</table>

### 3) expression을 case로 설정할 수 있다.
```kotlin
when (x) {
    s.toInt() -> print("s encodes x")
    else -> print("s does not encode x")
}
```

### 4) 복수의 case는 콤마(,)로 설정할 수 있다.
```kotlin
when (x) {
    0, 1 -> print("x == 0 or x == 1")
    else -> print("otherwise")
}
```

### 5) in, !in, is 키워드를 쓸 수 있다. 

## For loops
```kotlin
for (i in 1..3) {
    print(i) // 123
}

for (i in 6 downTo 0 step 2) {
    print(i) // 6420
}

for (i in array.indices) print(array[i])

for ((index, value) in array.withIndex()) println("the element at $index is $value")
```

## While loops
`while`과 `do-while`문이 있다.

# 2. Returns and jumps
Java랑 차이점이 있다면 label이라는 개념이 있다.  

## Break and continue labels
`break` `continue`때 흐름을 원하는 곳으로 바꿀 수 있다. 예제를 보면 이해하기 쉽다.    
```kotlin
loop@ for (i in 1..5) {
    for (j in 1..5) {
        println(j)
        if (j == 2) break@loop // for (i in 1..5) 위치로 이동한다. 12가 총 5번 출력됨
    }
}
```

## Return to labels
`return`은 값을 반환할 수 없던 상황에서 값을 반환할 수 있다. 이것도 예제를 보자!  
```kotlin
fun main() {
    foo() // 1245 done with explicit label
    foo2() // 12
}

fun foo() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit // local return to the caller of the lambda - the forEach loop
        print(it)
    }
    println(" done with explicit label")
}


fun foo2() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return
        print(it)
    }
    println(" done with explicit label")
}
```
`return@a 1`는 @a에 1을 리턴한다는 의미이다.  
> `foo2` 처럼 람다식에서 유용하게 쓰일 것 같다!!
