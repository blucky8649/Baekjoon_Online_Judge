
![썸네이루-001](https://user-images.githubusercontent.com/83625797/153982888-393a18eb-2b52-42f6-ba10-de244af4e155.png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/13460)

## 풀이

이 문제도 문제를 잘 읽고 단계별로 코드를 작성하시면 푸실 수 있습니다.  

만은..! 예외처리를 빡세게 해주셔야 합니다.

### 참고 사항
이 문제는 일반적인 2차원 배열에서의 BFS의 개념을 담고 있습니다. 그러나 빨간공, 파란공의 위치에 대해서는 수많은 경우의 수가 존재합니다.  

따라서, 노드의 방문을 체크하는 isVisited 배열을 **4차원 배열로 선언**하여 두 공의 위치에 대한 모든 경우의 수를 따져주셔야 합니다.

### Data class
이 문제를 쉽고 직관적으로 풀기위해서는 R공의 위치, B공의 위치, 총 이동횟수를 담은 Data class를 생성해주셔야 합니다.

```kotlin
data class Pos(var x: Int, var y: Int)
data class Marble(
    val R: Pos,
    val B: Pos,
    val cnt: Int
)
```

### 공을 벽까지 보내야 한다.
이 문제는 다른 BFS문제에서 1칸 씩 이동하는 것과 달리, 한 번 이동으로 벽 까지 보내야 합니다.
따라서, 빨간 구슬, 파란 구슬 모두 벽 까지 보내는 것이 중요합니다.

```kotlin
fun moveMarble(x: Int, y: Int, dir: Int): Pos {
    isHole = false
    val pos = Pos(x, y)
    while (board[pos.y + dy[dir]][pos.x + dx[dir]] != '#') {
        pos.x += dx[dir]
        pos.y += dy[dir]

        if (board[pos.y][pos.x] == 'O') {
            isHole = true

            return pos
        }
    }
    return pos
}
```

### BFS 수행
이제 두 구슬을 상하좌우로 이동시켜 정답을 도출해야합니다.
앞서 설명 드린 구슬 이동 메서드를 통해 두 구슬을 벽까지 보내고, 만약 두 구슬의 포지션이 겹친다면 **원래의 포지션을 참고하여 구슬 위치를 재조정**해야 합니다.

```kotlin
fun rollingInTheHole(posR: Pos, posB: Pos): Int {
    val isVisited = Array(R) { Array(C) { Array(R) { BooleanArray(C) } } }
    val q: Queue<Marble> = LinkedList<Marble>()
    q.offer(Marble(posR, posB, 0))
    isVisited[posR.y][posR.x][posB.y][posB.x] = true

    while (!q.isEmpty()) {
        val cur = q.poll()
        // 만약 이동 횟수가 10 이상이면 게임 종료.
        if (cur.cnt == 10) return -1
        for (i in 0 until 4) {
            /** 파란 구슬 이동 **/
            var nextB = moveMarble(cur.B.x, cur.B.y, i)
            if (isHole) continue
            // 파란 구슬이 구멍에 들어가면 게임은 끝나지만, 뒤에 다른 경우도 봐야하기 때문에 continue.
            /** 빨간 구슬 이동 **/
            var nextR = moveMarble(cur.R.x, cur.R.y, i)
            if (isHole) return cur.cnt + 1
            /** 방향에 따른 구슬 위치 조정 **/
            if (nextR == nextB) {
                when(i) {
                    0 -> {
                        if (cur.R.y > cur.B.y) {
                            nextR.y++
                        } else {
                            nextB.y++
                        }
                    }
                    1 -> {
                        if (cur.R.x < cur.B.x) {
                            nextR.x--
                        } else {
                            nextB.x--
                        }
                    }
                    2 -> {
                        if (cur.R.y < cur.B.y) {
                            nextR.y--
                        } else {
                            nextB.y--
                        }
                    }
                    3 -> {
                        if (cur.R.x > cur.B.x) {
                            nextR.x++
                        } else {
                            nextB.x++
                        }
                    }
                }
            }
            if (isVisited[nextR.y][nextR.x][nextB.y][nextB.x]) continue
            isVisited[nextR.y][nextR.x][nextB.y][nextB.x] = true
            q.offer(Marble(nextR, nextB, cur.cnt + 1))
        }
    }
    return -1
}
```

## Source Code
```kotlin
package 삼성SW역량테스트.`13460번_구슬탈출2`

import java.util.*

private lateinit var board: Array<CharArray>
private val dy = arrayOf(-1, 0, 1, 0)
private val dx = arrayOf(0, 1, 0, -1)
private var isHole = false

var R = 0
var C = 0
fun main() {
    val (r, c) = readln().split(" ").map { it.toInt() }
    R = r ; C = c

    var posR = Pos(0, 0)
    var posB = Pos(0, 0)
    board = Array(R) { CharArray(C) }
    for (i in 0 until R) {
        val part = readln()
        for (j in 0 until C) {
            board[i][j] = part[j]
            if (board[i][j] == 'R') {
                posR = Pos(j, i)
                board[i][j] = '.'
            }
            if (board[i][j] == 'B') {
                posB = Pos(j, i)
                board[i][j] = '.'
            }
        }
    }
    println(rollingInTheHole(posR, posB))
}
fun rollingInTheHole(posR: Pos, posB: Pos): Int {
    val isVisited = Array(R) { Array(C) { Array(R) { BooleanArray(C) } } }
    val q: Queue<Marble> = LinkedList<Marble>()
    q.offer(Marble(posR, posB, 0))
    isVisited[posR.y][posR.x][posB.y][posB.x] = true

    while (!q.isEmpty()) {
        val cur = q.poll()
        if (cur.cnt == 10) return -1
        for (i in 0 until 4) {
            /** 파란 구슬 이동 **/
            var nextB = moveMarble(cur.B.x, cur.B.y, i)
            if (isHole) continue
            // 파란 구슬이 구멍에 들어가면 게임은 끝나지만, 뒤에 다른 경우도 봐야하기 때문에 continue.
            /** 빨간 구슬 이동 **/
            var nextR = moveMarble(cur.R.x, cur.R.y, i)
            if (isHole) return cur.cnt + 1
            /** 방향에 따른 구슬 위치 조정 **/
            if (nextR == nextB) {
                when(i) {
                    0 -> {
                        if (cur.R.y > cur.B.y) {
                            nextR.y++
                        } else {
                            nextB.y++
                        }
                    }
                    1 -> {
                        if (cur.R.x < cur.B.x) {
                            nextR.x--
                        } else {
                            nextB.x--
                        }
                    }
                    2 -> {
                        if (cur.R.y < cur.B.y) {
                            nextR.y--
                        } else {
                            nextB.y--
                        }
                    }
                    3 -> {
                        if (cur.R.x > cur.B.x) {
                            nextR.x++
                        } else {
                            nextB.x++
                        }
                    }
                }
            }
            if (isVisited[nextR.y][nextR.x][nextB.y][nextB.x]) continue
            isVisited[nextR.y][nextR.x][nextB.y][nextB.x] = true
            q.offer(Marble(nextR, nextB, cur.cnt + 1))
        }
    }
    return -1
}
fun moveMarble(x: Int, y: Int, dir: Int): Pos {
    isHole = false
    val pos = Pos(x, y)
    while (board[pos.y + dy[dir]][pos.x + dx[dir]] != '#') {
        pos.x += dx[dir]
        pos.y += dy[dir]

        if (board[pos.y][pos.x] == 'O') {
            isHole = true

            return pos
        }
    }
    return pos
}
data class Pos(var x: Int, var y: Int)
data class Marble(
    val R: Pos,
    val B: Pos,
    val cnt: Int
)
```
