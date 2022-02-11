![](https://media.vlpt.us/images/blucky8649/post/50666659-21c3-4c66-b445-d08ed4f51048/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8_%EC%82%BC%EC%84%B1-001%20(1).png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/14502)

## 풀이

저번에 포스팅했던 감시 문제와 비슷하지만 좀 더 쉽다고 볼 수 있는 문제입니다.

삼성 기출을 풀때는 문제를 꼼꼼히 읽고, 제시하는 내용이 무엇인지 파악한 후, 세부 단계로 나눠서 차근차근 해결해 나가는 것이 중요합니다.

또한 시간 제한이 널널한 편이기 때문에 주먹구구식으로 푸는 것도 나쁘지 않습니다.

이 문제는 백트래킹을 활용해서 3개의 벽을 모든 경우의 수에 두고, BFS를 수행하여 바이러스를 퍼뜨린 뒤, 안전지대 범위를 계산하면 되는 문제입니다.

### 1. 백트래킹을 활용하여 모든 경우를 완전탐색 한다.
가로 세로의 범위가 (3 ≤ N, M ≤ 8) 이므로 `백트래킹`을 시도해볼 수 있습니다. 2차원 배열에서 벽 3개를 설치할 수 있는 경우를 모두 구해주는 것입니다. 한 가지 팁을 드리자면 벽을 설치하는 순서는 중요하지 않으므로 **중복 없는 조합**을 구해주셔야 합니다.

>(0, 1)에 위치에 벽을 설치한 후 (0, 2)위치에 벽을 설치 하는 것과, (0, 2)에 위치에 벽을 설치한 후 (0, 1)위치에 벽을 설치 하는 것은 아무 의미가 없이 다 똑같은 의미겠죠?

#### 중복을 허용한 조합을 사용한 경우
```kotlin
fun permutation(depth: Int) {
    if (depth == 3) {
        // 3. 벽 3개를 다 세웠다면, 이제 바이러스를 퍼뜨려서 안전지대의 개수를 구해보자.
        var countVirusRange = 0
        val q: Queue<Virus> = LinkedList<Virus>(virus)
        val check = Array(map.size) { BooleanArray(map[0].size) }
        while (!q.isEmpty()) {
            countVirusRange += spreadVirus(q.poll(), check)
        }
        answer = max(answer, max - countVirusRange)
        return
    }

    for (y in map.indices) {
        for (x in map[0].indices) {
            if (isVisited[y][x] || map[y][x] != 0) continue
            isVisited[y][x] = true // 중복 방지 방문처리
            map[y][x] = 1 // 벽 세우기
            max--
            permutation(depth + 1)
            isVisited[y][x] = false // 백트래킹: 원상복귀
            map[y][x] = 0
            max++
        }
    }
}
```
##### ![](https://images.velog.io/images/blucky8649/post/56fc080b-4cf9-40dd-a3e9-875fb6d3ea81/image.png)

중복을 허용한 조합을 사용하게 된다면 위 사진과 같이 **302212KB의 메모리, 932ms의 시간**을 잡아먹습니다.

그러면 중복 허용하지 않는 조합을 사용한다면 어떻게 될까요?
#### 중복을 허용하지 않는 조합을 사용하는 경우
```kotlin
fun permutation(depth: Int, startX: Int, startY: Int) {
    if (depth == 3) {
        // 3. 벽 3개를 다 세웠다면, 이제 바이러스를 퍼뜨려서 안전지대의 개수를 구해보자.
        var countVirusRange = 0
        val q: Queue<Virus> = LinkedList<Virus>(virus)
        val check = Array(map.size) { BooleanArray(map[0].size) }
        while (!q.isEmpty()) {
            countVirusRange += spreadVirus(q.poll(), check)
        }
        answer = max(answer, max - countVirusRange)
        return
    }

    for (y in startY until map.size) {
        var new_x = 0
        // 2차원 배열에서의 중복 없는 조합
        if (startY == y) {
           new_x = startX
        } else {
            new_x = 0
        }

        for (x in new_x until map[0].size) {
            if (isVisited[y][x] || map[y][x] != 0) continue
            isVisited[y][x] = true // 중복 방지 방문처리
            map[y][x] = 1 // 벽 세우기
            max--
            permutation(depth + 1, x + 1, y)
            isVisited[y][x] = false // 백트래킹: 원상복귀
            map[y][x] = 0
            max++
        }
    }
}
```

#### ![](https://images.velog.io/images/blucky8649/post/a99ff03e-b9c4-4c4a-93c0-f0c62d65f617/image.png)

차이가 느껴지시나요? 백트래킹을 이용해 완전탐색을 하실 때, 순서가 중요하지 않다면 위와 같이 가지치기를 하여 더 효율적인 코드를 작성하실 수 있습니다.

### 2. 바이러스 전파
1 단계에서 세운 3개의 벽을 포함시켜서 각각의 바이러스를 전파하는 단계입니다. 반환 값은 **바이러스 1개가 전파한 범위**가 되겠네요.
```kotlin
fun spreadVirus(virus: Virus, check: Array<BooleanArray>): Int {
    var countVirusRange = 0
    val q: Queue<Virus> = LinkedList<Virus>()
    q.offer(Virus(virus.x, virus.y))
    check[virus.y][virus.x] = true
    while (!q.isEmpty()) {
        val cur = q.poll()
        for (i in 0 until 4) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (nx < 0 || nx >= map[0].size || ny < 0 || ny >= map.size || check[ny][nx] || map[ny][nx] != 0) continue
            countVirusRange++
            check[ny][nx] = true
            q.offer(Virus(nx, ny))
        }
    }

    return countVirusRange
}

```

## Source Code
```kotlin
package 삼성SW역량테스트.`14052번_연구소`

import java.util.*
import kotlin.math.max

private lateinit var map: Array<IntArray>
private lateinit var isVisited: Array<BooleanArray>
private val virus: Queue<Virus> = LinkedList<Virus>()
private var dy = arrayOf(-1, 0, 1, 0) // 상하좌우 방향정보
private var dx = arrayOf(0, 1, 0, -1)
private var max = 0
private var answer = Integer.MIN_VALUE

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    map = Array(N) { IntArray(M) }
    isVisited = Array(N) { BooleanArray(M) }
    max = N * M // max = 맵의 전체 크기 - 벽 - 바이러스
    // 1. 입력 데이터 받기
    for (i in 0 until N) {
        val st = StringTokenizer(readln())
        for (j in 0 until M) {
            map[i][j] = st.nextToken().toInt()
            if (map[i][j] == 0) continue
            max--
            if (map[i][j] == 2) {
                virus.offer(Virus(j, i))
            }
        }
    }
    // 2. 벽 세우기: 백트래킹으로 모든 경우를 따져봐야 함.
    permutation(0, 0, 0)
    println(answer)
}

fun permutation(depth: Int, startX: Int, startY: Int) {
    if (depth == 3) {
        // 3. 벽 3개를 다 세웠다면, 이제 바이러스를 퍼뜨려서 안전지대의 개수를 구해보자.
        var countVirusRange = 0
        val q: Queue<Virus> = LinkedList<Virus>(virus)
        val check = Array(map.size) { BooleanArray(map[0].size) }
        while (!q.isEmpty()) {
            countVirusRange += spreadVirus(q.poll(), check)
        }
        answer = max(answer, max - countVirusRange)
        return
    }

    for (y in startY until map.size) {
        var new_x = 0
        // 2차원 배열에서의 중복 없는 조합
        if (startY == y) {
           new_x = startX
        } else {
            new_x = 0
        }

        for (x in new_x until map[0].size) {
            if (isVisited[y][x] || map[y][x] != 0) continue
            isVisited[y][x] = true // 중복 방지 방문처리
            map[y][x] = 1 // 벽 세우기
            max--
            permutation(depth + 1, x + 1, y)
            isVisited[y][x] = false // 백트래킹: 원상복귀
            map[y][x] = 0
            max++
        }
    }
}

fun spreadVirus(virus: Virus, check: Array<BooleanArray>): Int {
    var countVirusRange = 0
    val q: Queue<Virus> = LinkedList<Virus>()
    q.offer(Virus(virus.x, virus.y))
    check[virus.y][virus.x] = true
    while (!q.isEmpty()) {
        val cur = q.poll()
        for (i in 0 until 4) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (nx < 0 || nx >= map[0].size || ny < 0 || ny >= map.size || check[ny][nx] || map[ny][nx] != 0) continue
            countVirusRange++
            check[ny][nx] = true
            q.offer(Virus(nx, ny))
        }
    }

    return countVirusRange
}

data class Virus(val x: Int, val y: Int)
```