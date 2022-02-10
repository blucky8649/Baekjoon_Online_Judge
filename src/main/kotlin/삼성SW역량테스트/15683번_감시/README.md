![](https://media.vlpt.us/images/blucky8649/post/327a9b60-fc05-4753-9dee-c52fd892654e/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(2).png)
[문제 링크](https://www.acmicpc.net/problem/15683)

## 풀이
모든 경우를 완전탐색하여 푸는 문제입니다.
제한 시간은 1초라고 되어있지만 배열의 최대 범위가 8이기에 **완전 탐색**으로 풀어야겠다고 생각했습니다.

참고로 삼성 기출 문제는 큰 틀을 그려놓고 단계적으로 하나씩 구현해나가면서 푸는 것이 중요합니다.
푸는 방법을 간단하게 설명하자면,
>1. cctv를 끝까지 비추는 move 메서드를 작성합니다.
>2. 모든 방향에 대한 경우의 수를 탐색하여 최솟값을 검출하는 백트래킹 메서드를 작성합니다.


본격 적으로 하나하나 짚어가며 문제를 해결해봅시다.

### step 1. CCTV의 타입별로 비출 수 있는 모든 방향을 나타낸다.

```kotlin
// 북, 동, 남, 서 방향에 따른 x, y축의 증감값
val dy = arrayOf(-1, 0, 1, 0) 
val dx = arrayOf(0, 1, 0, -1)

// CCTV 타입별로 비추는 모든 방향을 나타낸다.
val camera = arrayOf(
    arrayOf(arrayOf(0)),
    arrayOf(arrayOf(0), arrayOf(1), arrayOf(2), arrayOf(3)),                            // 1번 CCTV
    arrayOf(arrayOf(0, 2), arrayOf(1, 3)),                                              // 2번 CCTV
    arrayOf(arrayOf(0, 1), arrayOf(1, 2), arrayOf(2, 3), arrayOf(3, 0)),                // 3번 CCTV
    arrayOf(arrayOf(1, 0, 3), arrayOf(1, 2, 3), arrayOf(0, 2, 1), arrayOf(0, 2, 3)),    // 4번 CCTV
    arrayOf(arrayOf(0, 1, 2, 3))                                                        // 5번 CCTV
)
```
위와 같이 모든 방향에 대한 경우를 **3차원 배열**로 나타내주어야 합니다. 배열 안의 값은 그 위에 선언한 dy, dx 방향에 따른 인덱스를 넣어둡니다.

### step 2. 입력 값 받기.

```kotlin
/* 중략... */
var max = 0
private lateinit var q: Queue<CCTV> // CCTV 담는 Queue
private lateinit var map: Array<IntArray> // 전체 지도를 담는 2차원 배열

fun main() {
	val (R, C) = readln().split(" ").map { it.toInt() }
    max = R * C // 총 맵의 크기

    q = LinkedList<CCTV>()
    map = Array(R) { IntArray(C) }
    for (i in 0 until R) {
        val st = StringTokenizer(readln())
        for (j in 0 until C) {
            map[i][j] = st.nextToken().toInt()
            if (map[i][j] != 0) {
            	// 만약 입력 값이 CCTV라면, Queue에 담는다.
                if (map[i][j] != 6) {
                    q.offer(CCTV(j, i, map[i][j]))
                }
                max-- // 사각지대가 될 수 없으면 max값을 감소시켜 갱신한다.
            }
        }
    }
}
/* 중략... */
data class CCTV(var x: Int, var y: Int, val type: Int) // cctv 정보를 담는 data class
```

### step 3. cctv를 비추는 메서드 작성
`cur`의 좌표를 지속적으로 갱신하면서 지정한 방향으로 계속 비춰 나가야합니다. 단, 벽이나 맵을 벗어나는 경우 반드시 `break`를 해주어야 합니다.
```kotlin

/**
 * @parem x, y: 중심 좌표
 * @param dir: 비출 방향
 */
fun move(x: Int, y: Int, dir: Int): Int {
    var cnt = 0 // 총 비춘 면적을 cnt 하는 변수입니다.
    var cur = CCTV(x, y, dir)
    map[y][x] = -1 //이미 방문한 경로
    while (true) {
        cur.x = cur.x + dx[dir]
        cur.y = cur.y + dy[dir]
        // 비추는 곳이 벽에 닿거나, 범위를 이탈했을 시 루프를 빠져나와야 한다.
        if (!(cur.x in 0 until map[0].size && cur.y in 0 until map.size
                    && map[cur.y][cur.x] != 6)
        ) return cnt
		
        // 이미 비추지 않은 빈공간은 새롭게 비추면서 cnt 값을 증가시킨다.
        if (map[cur.y][cur.x] != 0) continue
        cnt++
        map[cur.y][cur.x] = -1

    }
}
```

### step 4. 완전 탐색 : 재귀를 활용한 백트래킹 이용
이전에 선언했었던 camera 3차원 배열을 이용하여 모든 CCTV에 대하여 가능한 모든 방향을 비춰보기 위하여 백트래킹을 사용하였습니다.

>백트래킹은 미래로 이동하여 **특정 경우에 대한 상황을 미리 시뮬레이션** 해보고 시뮬레이션 하기 전 상태로로 돌아온 뒤, **또 다른 경우도 시뮬레이션 해보는 알고리즘**입니다.

```kotlin
// 백트래킹을 이용하여 모든 경우의 수를 따져 최솟값을 추려내야함
/** @param range: 사각지대의 개수 **/
fun permutation(range: Int) {
	// 만약 모든 CCTV가 가동 되었다면 사각지대가 몇개나 되는지 비교해본다.
    if (q.isEmpty()) {
        ans = min(ans, range)
        return
    }

    val cur = q.poll()
    // 배열 복사 : 시뮬레이션을 수행하고 원상복귀하기 위함입니다.
    val clone = Array(map.size) { IntArray(map[0].size) }
    for (i in map.indices) {
        clone[i] = map[i].clone()
    }
    for (i in 0 until camera[cur.type].size) {
        var cnt = 0 // 카메라가 총 비춘 범위를 cnt 하는 변수
        for (j in 0 until camera[cur.type][i].size) {
            val dir = camera[cur.type][i][j]
            cnt += move(cur.x, cur.y, dir)
        }
        permutation(range - cnt) // 시뮬레이션 on
        
        // 백트래킹: 시뮬레이션이 끝났으면, 다음 경우를 시뮬레이션 하기 위해 원상복귀 시킨다.
        for (a in map.indices) {
            map[a] = clone[a].clone()
        }
    }
    q.offer(cur)
}
```

### final step. 정답 출력하기
```kotlin
fun main() {
	/* 중략... */
	permutation(max)
	println(ans)
}
/* 중략... */
```
마지막으로 시뮬레이션을 돌려주는 메서드를 실행시키고 정답을 출력하시면 됩니다.


## Source Code
```kotlin
package 삼성SW역량테스트.`15683번_감시`

import java.util.*
import kotlin.math.min

// CCTV 타입별로 비추는 모든 방향을 나타낸다.
val camera = arrayOf(
    arrayOf(arrayOf(0)),
    arrayOf(arrayOf(0), arrayOf(1), arrayOf(2), arrayOf(3)),                            // 1번 CCTV
    arrayOf(arrayOf(0, 2), arrayOf(1, 3)),                                              // 2번 CCTV
    arrayOf(arrayOf(0, 1), arrayOf(1, 2), arrayOf(2, 3), arrayOf(3, 0)),                // 3번 CCTV
    arrayOf(arrayOf(1, 0, 3), arrayOf(1, 2, 3), arrayOf(0, 2, 1), arrayOf(0, 2, 3)),    // 4번 CCTV
    arrayOf(arrayOf(0, 1, 2, 3))                                                        // 5번 CCTV
)
val dy = arrayOf(-1, 0, 1, 0) // 북, 동, 남, 서 방향에 대한 정보
val dx = arrayOf(0, 1, 0, -1)
var max = 0
var ans = Integer.MAX_VALUE
private lateinit var q: Queue<CCTV>
private lateinit var map: Array<IntArray>
fun main() {
    val (R, C) = readln().split(" ").map { it.toInt() }
    max = R * C

    q = LinkedList<CCTV>()
    map = Array(R) { IntArray(C) }
    for (i in 0 until R) {
        val st = StringTokenizer(readln())
        for (j in 0 until C) {
            map[i][j] = st.nextToken().toInt()
            if (map[i][j] != 0) {
                if (map[i][j] != 6) {
                    q.offer(CCTV(j, i, map[i][j]))
                }
                max--
            }
        }
    }
    permutation(max)
    println(ans)

}
// 백트래킹을 이용하여 모든 경우의 수를 따져 최솟값을 추려내야함
/** @param range: 카메라가 비춘 총 범위 **/
fun permutation(range: Int) {
    if (q.isEmpty()) {
        ans = min(ans, range)
        return
    }
    val cur = q.poll()
    // 배열 복사
    val clone = Array(map.size) { IntArray(map[0].size) }
    for (i in map.indices) {
        clone[i] = map[i].clone()
    }
    for (i in 0 until camera[cur.type].size) {
        var cnt = 0 // 카메라가 총 비춘 범위를 cnt 하는 변수
        for (j in 0 until camera[cur.type][i].size) {
            val dir = camera[cur.type][i][j]
            cnt += move(cur.x, cur.y, dir)
        }
        permutation(range - cnt)
        // 백트래킹: 앞서 작업했던 내용을 원래대로 복구한다.
        for (a in map.indices) {
            map[a] = clone[a].clone()
        }
    }
    q.offer(cur)
}


fun move(x: Int, y: Int, dir: Int): Int {
    var cnt = 0
    var cur = CCTV(x, y, dir)
    map[y][x] = -1 //이미 방문한 경로
    while (true) {
        cur.x = cur.x + dx[dir]
        cur.y = cur.y + dy[dir]
        // 비추는 곳이 벽에 닿거나, 범위를 이탈했을 시 루프를 빠져나와야 한다.
        if (!(cur.x in 0 until map[0].size && cur.y in 0 until map.size
                    && map[cur.y][cur.x] != 6)
        ) return cnt

        if (map[cur.y][cur.x] != 0) continue
        cnt++
        map[cur.y][cur.x] = -1

    }
}

data class CCTV(var x: Int, var y: Int, val type: Int)
```