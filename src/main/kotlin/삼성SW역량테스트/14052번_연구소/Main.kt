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
    permutation(0)
    println(answer)
}

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