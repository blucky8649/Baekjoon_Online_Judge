![백쥰](https://media.vlpt.us/images/blucky8649/post/a3ca609c-2eae-4ced-a438-ceb191810db2/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(5).png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/22942)

## 문제
원 이동하기 2 문제를 만들고 만든 데이터가 문제의 조건에 맞는지 확인하는 코드를 작성해야한다.

해당 문제의 데이터는 아래 조건들을 만족해야한다.

모든 원의 중심 좌표는 $x$축 위에 존재해야 한다.
 $N$개의 원 중 임의의 두 원을 선택했을 때, 교점이 존재하지 않아야 한다. 즉, 하나의 원이 다른 원 안에 존재하거나 외부에 존재한다.
데이터 형식은 원의 개수 $N$이랑 각 원의 중심 $x$좌표, 원의 반지름 $r$만 주어진다. 따라서, 2번 조건을 만족하는지만 확인하면 된다.

주어진 데이터가 해당 조건을 만족하는지 확인해보자.

## 입력
첫 번째 줄에는 원의 개수 $N$이 주어진다.

두 번째 줄부터 $N+1$번째 줄까지 원의 중심 $x$좌표, 원의 반지름 $r$이 공백으로 구분되어 주어진다.

## 출력
데이터가 조건에 맞는다면 YES, 조건에 만족하지 않는다면 NO를 출력한다.

## 제한
> -  $2 ≤ N ≤ 200,000$ 
-  $-1,000,000 ≤ x ≤ 1,000,000$ 
-  $1 ≤ r ≤ 10,000$ 
-  $x, r$은 정수

## 풀이
시간 제한이 1이며, N의 범위가 20만입니다.
모든 원을 서로 비교한다면 **O(N^2)**의 시간복잡도가 형성되므로 **시간 초과**가 날 것 입니다.
그렇기 때문에 **O(N)**의 시간복잡도 안에 문제를 해결하는 것이 포인트입니다.

혹시 프로그래머스의 [올바른 괄호](https://programmers.co.kr/learn/courses/30/lessons/12909) 문제를 푸신 적이 있으신가요?

이 문제도 괄호 문제와 상당히 비슷한 문제입니다.
원의 양쪽 끝 x좌표를 여는 괄호, 닫는 괄호라고 생각을 하고 계산을 하는것입니다.
각각의 원들은 서로 다른 괄호들 `(), {}, [] 등등..` 이라고 생각을 하면 됩니다.
만약, **"(" 괄호가 닫히기도 전에 다음 요소로 "}"괄호가 나온다면..** 완전한 괄호가 아니겠죠?ㅎㅎ

```kotlin
data class Circle(val x : Int, val isOpen : Boolean, val num : Int)
```
문제를 풀기위해 위와 같은 data class를 선언해주었습니다.

`x` : 원의 x좌표
`isOpen` : 좌표의 시작점인지 끝점인지 확인하는 좌표 (true면 괄호가 열려있는 것이고, false면 닫혀있는 것입니다.)
`num` : 원의 고유 번호 (원 1번의 괄호를 열었으면 원 1번의 괄호로 닫아야 합니다.)


다음은 상세 풀이 과정입니다.
> 1. 한 배열 안에 괄호가 열리는 원의 좌표, 괄호가 닫히는 원의 좌표를 다 넣어줍니다. (x 좌표를 기준으로 오름차순 정렬)
2. 만약 stack이 비어있으면 배열의 다음 요소를 넣어줍니다.
3. 만약 stack의 peek과 다음 요소의 원의 번호(num)가 같다면 완전한 괄호로 닫힌 것이므로 stack에서 pop 해줍니다.
4. 만약 다음 요소가 다른 원이고, 닫히는 괄호라면? NO 출력하고 rerutn 합니다.
5. 1~4 과정을 전부 통과하면 YES를 출력합니다.

코드에 위의 과정을 주석으로 입혀 설명하면 다음과 같습니다.
```kotlin
// 괄호 여닫는 코드
for (next in arr) {
    // 2. stack이 비어있으면 배열의 다음 요소를 넣어줍니다.
    if (stack.isEmpty()) {
        stack.push(next) ; continue
    }
	
    val cur = stack.lastElement()
    // 3. 만약 stack의 peek과 다음 요소의 원의 번호(num)가 같다면 완전한 괄호로 닫힌 것이므로 stack에서 pop 해줍니다.
    if (next.num == cur.num) {
        stack.pop() ; continue
    }
    // 4. 만약 다음 요소가 다른 원이고, 닫히는 괄호라면? NO 출력하고 rerutn 합니다.
    if (cur.isOpen && !next.isOpen) {
        println("NO")
        return
    }
    stack.push(next)
}
```

## Source Code
```kotlin
package 수학.`22942번_데이터_체커`

import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    val n = readLine().toInt()
    val pq = PriorityQueue<Circle>()
    val arr = Array(n * 2) {Circle(0, false, 0)}

    var idx = 0;
    for(i in 0 until n) {
        val (x, r) = readLine().split(" ").map { it.toInt() }
        arr[idx++] = Circle(x - r, true, i)
        arr[idx++] = Circle(x + r, false, i)
    }
    arr.sortBy { it.x } // x축을 기준으로 정렬

    var circle = pq.poll()
    val stack = Stack<Circle>()

    for (next in arr) {
        if (stack.isEmpty()) {
            stack.push(next) ; continue
        }

        val cur = stack.lastElement()
        if (next.num == cur.num) {
            stack.pop() ; continue
        }

        if (cur.isOpen && !next.isOpen) {
            println("NO")
            return
        }
        stack.push(next)
    }
    println("YES")
}
data class Circle(val x : Int, val isOpen : Boolean, val num : Int)
```
