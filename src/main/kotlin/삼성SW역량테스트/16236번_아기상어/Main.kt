package 삼성SW역량테스트.`16236번_아기상어`

import java.util.*

private lateinit var water: Array<IntArray>
private val dy = arrayOf(-1, 0, 1, 0) // 상하좌우 이동을 위한 배열
private val dx = arrayOf(0, 1, 0, -1)
fun main() {
    val n = readln().toInt()
    water = Array(n) { IntArray(n) }
    var babyShark = Shark(0, 0, 0, 0, 0)
    for (i in 0 until n) {
        val st = StringTokenizer(readln())
        for (j in 0 until n) {
            water[i][j] = st.nextToken().toInt()
            if (water[i][j] == 9) {
                babyShark = Shark(j, i, 0, 2, 0)
                water[i][j] = 0
            }
        }
    }

    var answer = 0
    while (true) {
        /** 1. 먹을 수 있는 가장 우선의 물고기를 정한다. **/
        val eatFirst = searchFish(babyShark)
        // 더 이상 먹을 물고기가 없으면 프로그램 종료.
        if (eatFirst.dst == -1) break

        /** 2. 물고기를 먹고 상어 위치 갱신 **/
        babyShark.also {
            it.x = eatFirst.dot.x
            it.y = eatFirst.dot.y
            if (it.exp + 1 == it.lev) {
                it.lev ++
                it.exp = 0
            } else {
                it.exp++
            }
            water[it.y][it.x] = 0
        }
        // 상어의 총 이동거리 갱신
        answer += eatFirst.dst
    }
    println(answer)
}
fun searchFish(babyShark: Shark): FishEatable{
    val isVisited = Array(water.size) { BooleanArray(water[0].size) }
    val eatable = PriorityQueue<FishEatable>()
    val q: Queue<Shark> = LinkedList<Shark>()

    isVisited[babyShark.y][babyShark.x] = true
    q.offer(babyShark)

    while(q.isNotEmpty()) {
        val cur = q.poll()
        if (water[cur.y][cur.x] in 1 until  cur.lev) {
            eatable.offer(FishEatable(cur.cnt, cur))
            continue
        }
        for (i in 0 until 4) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]

            if (nx in water.indices && ny in water.indices && !isVisited[ny][nx] && water[ny][nx] <= cur.lev) {
                isVisited[ny][nx] = true
                q.offer(Shark(nx, ny, cur.exp, cur.lev, cur.cnt + 1))
            }
        }
    }
    return if (eatable.isNotEmpty()) eatable.peek() else FishEatable(-1, babyShark)
}
data class Shark(var x: Int, var y: Int, var exp: Int, var lev: Int, var cnt: Int)
data class FishEatable(val dst: Int, val dot: Shark): Comparable<FishEatable> {
    override fun compareTo(other: FishEatable): Int {
        if (this.dst == other.dst) {
            if (this.dot.y == other.dot.y) {
                return this.dot.x - other.dot.x
            }
            return this.dot.y - other.dot.y
        }
        return this.dst - other.dst
    }
}

