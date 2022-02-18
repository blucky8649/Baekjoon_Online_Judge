package 삼성SW역량테스트.`17142번_연구소3`

import java.util.*
import kotlin.math.*

private lateinit var rab: Array<IntArray>
private lateinit var isSelected: BooleanArray
private val dy = arrayOf(-1, 0, 1, 0)
private val dx = arrayOf(0, 1, 0, -1)
private val virusPosList = ArrayList<Virus>()
private val virusSelected = Stack<Virus>()
private var sizeWithoutVirus = 0
private var answer = Integer.MAX_VALUE

fun main() {
    val (n, m) = readln().split(" ").map { it.toInt() }
    sizeWithoutVirus = n * n
    rab = Array(n) { IntArray(n) }
    for (i in 0 until n) {
        val st = StringTokenizer(readln())
        for (j in 0 until n) {
            rab[i][j] = st.nextToken().toInt()
            if (rab[i][j] == 2) {
                virusPosList.add(Virus(j, i, 0))
                rab[i][j] = -9
                sizeWithoutVirus--
            } else if (rab[i][j] == 1) {
                rab[i][j] = -1 // 벽
                sizeWithoutVirus--
            }
        }
    }
    if (sizeWithoutVirus == 0) {
        println(0)
        return
    }
    // 1. 바이러스가 될 수 있는 모든 자리에서 m개의 자리를 뽑는 조합을 구한다.
    isSelected = BooleanArray(virusPosList.size)
    Permutation(0, 0, m)

    println(if (answer == Int.MAX_VALUE) -1 else answer)
}

fun Permutation(start: Int, depth: Int, m: Int) {
    if (depth == m) {
        // 2. BFS를 통해 바이러스를 흩뿌려준다.
        spreadVirus(sizeWithoutVirus)
        return
    }

    for (i in start until virusPosList.size) {
        virusSelected.push(virusPosList[i])
        Permutation(i + 1, depth + 1, m)
        virusSelected.pop()
    }
}

fun spreadVirus(sizeWithoutVirus: Int) {
    var size = sizeWithoutVirus
    val isVisited = Array(rab.size) { BooleanArray(rab.size) }
    var time = 0 // 최대 걸린 시간
    val q: Queue<Virus> = LinkedList<Virus>()

    for (v in virusSelected) {
        q.offer(v)
        isVisited[v.y][v.x] = true
    }

    while (q.isNotEmpty()) {
        val cur = q.poll()
        time = max(time, cur.cnt)

        for (i in dy.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (inRange(nx, ny) && !isVisited[ny][nx] && rab[ny][nx] != -1) {

                if (rab[ny][nx] == 0) {
                    size--
                }
                if (size == 0) {
                    answer = min(answer, cur.cnt + 1)
                    return
                }
                isVisited[ny][nx] = true
                q.offer(Virus(nx, ny, cur.cnt + 1))
            }
        }
    }
}

fun inRange(x: Int, y: Int) = (x in rab.indices && y in rab.indices)
data class Virus(val x: Int, val y: Int, val cnt: Int)