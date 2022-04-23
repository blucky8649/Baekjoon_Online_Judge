[문제 풀러 가기!](https://www.acmicpc.net/problem/13458)

## 풀이
~~이번 상반기에도 이렇게 나왔으면 하는 문제 유형 1순위입니다.~~

문제에 명시되어있는대로 구현하시면 되나, 최대 강의실, 강의실 별 학생수가 **1,000,000**이기 때문에 출력값은 최대 **백만*백만**이 나올 수 있습니다.

이 점 유의하여 자료형을 고려해보시기 바랍니다.

```kotlin
package 삼성SW역량테스트.`13458번_시험감독`

fun main() {
    val n = readln().toInt()
    val list = readln().split(" ").map { it.toInt() }.toMutableList()
    val (B, C) = readln().split(" ").map { it.toInt() }

    var answer: Long = n.toLong()
    list
        .filter { it - B > 0 }
        .forEach {
            answer += if ((it - B) % C == 0) (it - B) / C else (it - B) / C + 1
        }
    println(answer)
}

```
