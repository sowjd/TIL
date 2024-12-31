## Java와 Kotlin의 람다식 차이점
<table>
   <tr>
       <td>Java</td>
       <td>Kotlin</td>
   </tr>
   <tr>
<td>

```java
public interface Multiply {
    int multiply(int x);
}
public static void main(String[] args) {
    Multiply multiply2 = (x) -> x * 2;
    System.out.println(multiply2.multiply(3));

    // 람다식 바디가 블록{}으로 감싸져 있는데
    // 이때 리턴값이 있다면 반드시 return 문을 작성해야 한다.
    Multiply multiply3 = (x) -> {
        System.out.println("Hello");
        return x * 3;
    };
    System.out.println(multiply3.multiply(3));
}
```

</td>
<td>

```kotlin
fun interface Multiply {
    fun multiply(x: Int): Int
}

fun main() {
    val multiply2: Multiply = Multiply { x -> x * 2 }
    println(multiply2.multiply(3))

    // Kotlin의 람다식에는 return 문이 없다.
    // 람다의 마지막 줄로 선언하기만 하면 그 값을 반환한다.
    val multiply3: Multiply = Multiply { x ->
        println("Hello")
        x * 3
    }
    println(multiply3.multiply(3))
}
```

</td>
</tr>
</table>
