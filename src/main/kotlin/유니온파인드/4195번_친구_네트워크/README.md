[![백쮼](https://media.vlpt.us/images/blucky8649/post/41953539-5a4b-4252-aed3-6b625b5e3b8f/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(6).png)](https://www.acmicpc.net/problem/4195)

## 문제
민혁이는 소셜 네트워크 사이트에서 친구를 만드는 것을 좋아하는 친구이다. 우표를 모으는 취미가 있듯이, 민혁이는 소셜 네트워크 사이트에서 친구를 모으는 것이 취미이다.

어떤 사이트의 친구 관계가 생긴 순서대로 주어졌을 때, 두 사람의 친구 네트워크에 몇 명이 있는지 구하는 프로그램을 작성하시오.

친구 네트워크란 친구 관계만으로 이동할 수 있는 사이를 말한다.

## 입력
첫째 줄에 테스트 케이스의 개수가 주어진다. 각 테스트 케이스의 첫째 줄에는 친구 관계의 수 F가 주어지며, 이 값은 100,000을 넘지 않는다. 다음 F개의 줄에는 친구 관계가 생긴 순서대로 주어진다. 친구 관계는 두 사용자의 아이디로 이루어져 있으며, 알파벳 대문자 또는 소문자로만 이루어진 길이 20 이하의 문자열이다.

## 출력
친구 관계가 생길 때마다, 두 사람의 친구 네트워크에 몇 명이 있는지 구하는 프로그램을 작성하시오.

## 풀이
유니온 파인드 응용 문제다.

부모노드의 정보를 나타내는 `parents` 배열과, 관계에 포함된 친구의 수의 정보를 나타내는 `weight`배열을 선언해주었다.
```kotlin
parents = Array(m * 2) {i -> i}
weight = Array(m * 2) {i -> 1}
```

또한 입력 값이 String 문자열로 되어있으므로 `map`을 이용하여 각각의 사람마다 고유의 index번호를 부여해주어야 한다.
```kotlin
val (start, end) = readLine().split(" ").map { it }
if (!map.containsKey(start)) {
    map[start] = index++
}
if (!map.containsKey(end)) {
    map[end] = index++
}
```

그 상태에서 일반적인 유니온 파인드 알고리즘 코드를 적으면 되는데 한가지 추가할 조건이 있다.

```kotlin
fun union(a : Int, b : Int)  : Int {
    // a와 b의 부모노드를 구함
    val A = find(a)
    val B = find(b)

    // 만약 a와 b의 부모가 같으면 서로 이미 이어져 있는 것이므로 기존값 출력
    if (A == B) { return weight[A] }
    // 부모가 다르다면 부모를 같게 이어주어야함
    parents[B] = A
    // B의 친구네트워크를 A에 흡수해줌
    weight[A] += weight[B]

    return weight[A]
}
```

앞서 선언했던 weight 배열이 있기에 B의 부모를 A로 고친다음 B에 속해 있는 친구들을 전부 A에 흡수 시켜주면 이 문제는 풀릴 것이다.


## Source Code
```kotlin
package 유니온파인드.`4195번_친구_네트워크`

import java.util.*
import kotlin.collections.HashMap

private lateinit var map : HashMap<String, Int>
private lateinit var parents : Array<Int>
private lateinit var weight : Array<Int>
fun main() = with(System.`in`.bufferedReader()){
    val t = readLine().toInt() // 테스트 케이스 개수

    val sb = StringBuilder()
    for (i in 0 until t) {
        val m = readLine().toInt() // 친구 관계의 수
        map = HashMap<String, Int>()

        parents = Array(m * 2) {i -> i}
        weight = Array(m * 2) {i -> 1}

        var index = 0
        repeat(m) {
            val (start, end) = readLine().split(" ").map { it }
            if (!map.containsKey(start)) {
                map[start] = index++
            }
            if (!map.containsKey(end)) {
                map[end] = index++
            }
            sb.append(union(map[start]!!, map[end]!!)).append('\n')
        }
    }
    print(sb.toString())
}
fun find(x: Int) : Int {
    if (parents[x] == x) {
        return x
    }
    parents[x] = find(parents[x])
    return parents[x]
}

fun union(a : Int, b : Int)  : Int {
    // a와 b의 부모노드를 구함
    val A = find(a)
    val B = find(b)

    // 만약 a와 b의 부모가 같으면 서로 이미 이어져 있는 것이므로 기존값 출력
    if (A == B) { return weight[A] }
    // 부모가 다르다면 부모를 같게 이어주어야함
    parents[B] = A
    // B의 친구네트워크를 A에 흡수해줌
    weight[A] += weight[B]

    return weight[A]
}
data class Node(val  start : String, val end : String, val weight : Int)
```
