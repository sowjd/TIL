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
## 4. Characters
## 5. Strings
## 6. Arrays
