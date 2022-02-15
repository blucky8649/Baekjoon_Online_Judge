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