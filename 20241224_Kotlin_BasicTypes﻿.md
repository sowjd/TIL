https://kotlinlang.org/docs/basic-types.html
> 하나씩 차근차근!!

# Basic types
## 1. Numbers
### Integer types

| Type	 |Size (bits)| Min value| Max value|
|--------|-----------|----------|--------- |
| `Byte`	 | 8         |-128      |127       |
| `Short`	 | 16        |-32768    |32767     |
| `Int`	 | 32        |-2,147,483,648 (-2<sup>31</sup>)| 2,147,483,647 (2<sup>31</sup> - 1)|
| `Long`	 | 64        |-9,223,372,036,854,775,808 (-2<sup>63</sup>)|9,223,372,036,854,775,807 (2<sup>63</sup> - 1)|

Compiler는 기본적으로 `Int`로 추론한다.  
만약 3,000,000,000과 같이 `Int` 범위 밖의 숫자라면 `Long`으로 추론한다.  
명시적으로 타입을 지정하고 싶다면
```kotlin
val oneLong = 1L // Long
val oneByte: Byte = 1
```

### Floating-point types
| Type	 |Size (bits)|Significant bits|Exponent bits|Decimal digits|
|--------|-----------|--------------- |-------------|--------------|
| `Float`	 | 32        |24              |8            |6-7            |
| `Double` | 64        |53              |11           |15-16          |  

Compiler는 기본적으로 `Double`로 추론한다.  
`Float`으로 지정하고 싶다면 `F` 또는 `f`를 붙인다.

### Numbers representation on the JVM
JVM에서 number는 primitive type(`int`, `double` 등)으로 저장된다.  
`Int?`나 제네렉 사용으로 nullable일 경우 Java의 `Integer`로 박싱(boxing)된다.  
> 박싱이 되면 새로운 인스턴스가 생성되기 때문에 같은 객체 equal check에서 fail 난다..!!

```kotlin
val a: Int = 100
val boxedA: Int? = a
val anotherBoxedA: Int? = a

val b: Int = 10000
val boxedB: Int? = b
val anotherBoxedB: Int? = b

println(boxedA === anotherBoxedA) // true
println(boxedB === anotherBoxedB) // false
println(boxedB == anotherBoxedB) // true
```

> All nullable references to a are actually the same object because of the memory optimization that JVM applies to Integers between -128 and 127. It doesn't apply to the b references, so they are different objects.  
>
> 위 예제 코드에서 `a`의 값이 100으로, -128 ~ 127 사이의 정수라 JVM이 메모리 최적화를 했기 때문에 같은 객체라고 하는데...  
JVM에서 primitive type은 stack 영역에 저장되고, reference type은 값은 heap 영역에 저장하고 stack 영역에는 heap의 메모리 주소값만 참조하고 있다고 알고 있는데.. a와 b 둘 다 reference type이 아닌가 싶은데.. 왜 int의 값 크기에 따라 저렇게 동작을 하는걸까... 저것이 최적화의 힘인가..  
>
> 우선 정답인지는 모르겠지만 참고할 만한 stackoverflow 링크다.  
https://stackoverflow.com/questions/45139381/kotlin-boxed-int-are-not-the-same


### Explicit number conversions
context에서 타입 추론이 안 되는 경우는 명시적으로 형 변환을 해줘야 한다.  
작은 타입을 큰 타입에 할당할 경우를 명시적으로 형 변환을 해줘야 한다. (`toInt()`, `toLong()`, `toDouble()` 등)

```kotlin
val l = 1L + 3 // 타입 추론 가능, Long + Int => Long

val b: Byte = 1
// val i: Int = b // ERROR
val i1: Int = b.toInt()
```

### Bitwise operations
`Int`나 `Long` 타입일 때 사용 가능하다.  
infix form으로 호출 가능하다.  
* `shl(bits)` – signed shift left
* `shr(bits)` – signed shift right
* `ushr(bits)` – unsigned shift right
* `and(bits)` – bitwise **AND**
* `or(bits)` – bitwise **OR**
* `xor(bits)` – bitwise **XOR**
* `inv()` – bitwise inversion

### Floating-point numbers comparison
> `x in a..b` `x !in a..b` 간단하니 좋다!

## 2. Unsigned integer types
| Type     | Size (bits) | Min value | Max value                                       |
|----------|-------------|-----------|-------------------------------------------------|
| `UByte`  | 8           | 0         | 255                                             |
| `UShort` | 16          | 0         | 65,535                                          |
| `UInt`   | 32          | 0         | 4,294,967,295 (2<sup>32</sup> - 1)              |
| `ULong`  | 64          | 0         | 18,446,744,073,709,551,615 (2<sup>64</sup> - 1) |

Compiler는 기본적으로 `UInt`로 추론한다.  
`UInt` 범위 밖의 숫자라면 `ULong`으로 추론한다.  
literals 값에 `U` 또는 `u`를 붙인다.
> 양수인 `Int`값을 `UInt`로 변경할 땐 문제가 없지만 `UInt` -> `Int`는 조심하자!!

## 3. Booleans
`true` 또는 `false`를 저장한다. 8 bits 크기를 차지한다.
```kotlin
val boolNull: Boolean? = null // null 저장 가능
```

## 4. Characters
16-bit Unicode character를 저장한다.  
문자당 2 bytes 크기를 차지한다.  
```kotlin
val char1: Char = '1'
val int1 = char1.digitToInt()
```

## 5. Strings
```kotlin
val str = "abcd 123"
println(str[1]) // b
val multilineStr = """
    Hello
    World
    """
val multilineStr2 = """
    |Hello
    |World
    """.trimMargin()
```

### String templates
`$`를 출력하고 싶다면 `\$`을 사용한다.  
Multiline에서 출력하고 싶다면 `${'$'}`를 사용한다.  
```kotlin
val letters = listOf("a","b","c","d","e")
println("$letters") // [a, b, c, d, e]
```

### String formatting
`String.format()`은 **Kotlin/JVM**에서만 사용할 수 있다.  
```kotlin
val integerNumber = String.format("%07d", 31416)
println(integerNumber) // 0031416
println(String.format("%+.4f", 3.141592)) // +3.1416
println(String.format("%S %s", "hello", "world")) // HELLO world
```
> Format string을 변수에 assign할 수 있다.  
> String 출력할 때 `%S`로 하면 대문자로 출력되는게 신기하다!

## 6. Arrays
primitive value로만 구성될 array라면 boxing overhead를 줄이기 위해 primitive-type arrays를 쓸 수 있다.  
이 이외는 object-type array이다.  

### Create arrays
1. functions, such as `arrayOf()`, `arrayOfNulls()` or `emptyArray()`.
2. the `Array` constructor.

```kotlin
val simpleArray = arrayOf(1, 2, 3)
val nullArray: Array<Int?> = arrayOfNulls(3)
var exampleArray = emptyArray<String>()
var exampleArray2: Array<String> = emptyArray()

val initArray = Array<Int>(3) { 0 }
val asc = Array(5) { i -> (i * i).toString() } // ["0", "1", "4", "9", "16"]
```

#### Nested arrays
```kotlin
val twoDArray = Array(2) { Array<Int>(2) { 0 } } // [[0, 0], [0, 0]]
println(twoDArray.contentDeepToString())

val threeDArray = Array(3) { Array(3) { Array<Int>(3) { 0 } } }
println(threeDArray.contentDeepToString()) // [
    [[0, 0, 0], [0, 0, 0], [0, 0, 0]], 
    [[0, 0, 0], [0, 0, 0], [0, 0, 0]],
    [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
    ]
```

#### Pass variable number of arguments to a function
```kotlin
fun main() {
    val lettersArray = arrayOf("c", "d")
    printAllStrings("a", "b", *lettersArray) // abcd
}

fun printAllStrings(vararg strings: String) {
    for (string in strings) {
        print(string)
    }
}
```
> 멋지긴 한데 개인적으로 parameter가 몇 개 들어올지 모르는 상황은 진짜 특이한 케이스 아니고선 안쓸 것 같다..

#### Compare arrays
`.contentEquals()` `.contentDeepEquals()`

### Primitive-type arrays
| Primitive-type array | Equivalent in Java |
|----------------------|--------------------|
| `BooleanArray` | `boolean[]`|
| `ByteArray` | `byte[]`|
| `CharArray` | `char[]`|
| `DoubleArray` | `double[]`|
| `FloatArray` | `float[]`|
| `IntArray` | `int[]`|
| `LongArray` | `long[]`|
| `ShortArray` | `short[]`|

`Array` class와 상속관계는 없지만 동일한 함수와 속성(property)를 가지고 있다.
```kotlin
val exampleArray = IntArray(5)
println(exampleArray.joinToString()) // 0, 0, 0, 0, 0
```
convert primitive-type arrays to object-type arrays: `.toTypedArray()`  
convert object-type arrays to primitive-type arrays: `.toBooleanArray()` `.toIntArray()` `.toDoubleArray()` ...  

