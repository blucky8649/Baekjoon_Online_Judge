package 삼성SW역량테스트.`23290번_마법사상어와복제`

import java.util.*

const val FISH = 0
const val SMELL = 1
//←, ↖, ↑, ↗, →, ↘, ↓, ↙
val dr = listOf(0, -1, -1, -1, 0, 1, 1, 1) // 물고기의 이동 방향
val dc = listOf(-1, -1, 0, 1, 1, 1, 0, -1)
val _dr = listOf(-1, 0, 1, 0) // 상어의 이동 방향
val _dc = listOf(0, -1, 0, 1)
val water = Array(2) { Array(4) { IntArray(4) } }
var fish: Queue<Fish> = LinkedList()
lateinit var shark: Shark
fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    for (i in 0 until N) {
        val (r, c, dir) = readln().split(" ").map { it.toInt() - 1 }
        water[FISH][r][c] += 1
        fish.offer(Fish(r, c, dir))
    }
    val (sharkR, sharkC) = readln().split(" ").map { it.toInt() - 1 }
    shark = Shark(sharkR, sharkC)
    for (t in 1..M) {
        // 1. 상어가 모든 물고기에게 복제 마법을 시전한다.
        val cloneFish: Queue<Fish> = LinkedList()
        fish.forEach {
            cloneFish.add(it.copy())
        }
        // 2. 모든 물고기가 한 칸 이동한다.
        moveFish()
        // 3. 상어가 연속해서 3칸 이동한다.
        moveShark(t)
        // 4. 두 번 전 연습에서 생긴 물고기의 냄새가 격자에서 사라진다.
        removeSmell(t)
        // 5. 복제 마법이 완료된다.
        cloneSpell(cloneFish)

    }
    println(fish.size)
}
// 복제 마법 완료
fun cloneSpell(cloneFish: Queue<Fish>) {
    while (cloneFish.isNotEmpty()) {
        val curFish = cloneFish.poll()
        fish.add(curFish)
        water[FISH][curFish.r][curFish.c] += 1
    }
}
// 냄새를 지운다.
fun removeSmell(t: Int) {
    for (i in water[SMELL].indices) {
        for (j in water[SMELL][0].indices) {
            // 만약 현재 시도 횟수랑 2 이상 차이가 난다면 제거 대상이다.
            if (water[SMELL][i][j] + 2 <= t) {
                water[SMELL][i][j] = 0
            }
        }
    }
}
fun moveShark(t: Int) {
    max = 0
    maxRoute = emptyList()
    val route = Stack<Int>()
    permutation(0, route)
    maxRoute.forEach { r ->
        val nr = shark.r + _dr[r]
        val nc = shark.c + _dc[r]
        if (water[FISH][nr][nc] > 0) {
            // 냄새 남기기
            water[SMELL][nr][nc] = t
            // 먹어 치우기
            water[FISH][nr][nc] = 0
            // 물고기 개수 갱신
            fish = LinkedList(fish.filter {
                !(it.r == nr && it.c == nc)
            })
        }
        shark.also {
            it.r = nr
            it.c = nc
        }
    }

}
var max = 0
var maxRoute = List(3) {0}
fun calcFishCount(route: List<Int>): Int {
    var count = 0
    val pos = shark.copy()
    val clone = Array(4) {IntArray(4)}

    for (i in clone.indices) {
        clone[i] = water[FISH][i].clone()
    }

    route.forEach { r ->
        val nr = pos.r + _dr[r]
        val nc = pos.c + _dc[r]
        if (inRange(nr, nc)) {
            count += clone[nr][nc]
            clone[nr][nc] = 0
            pos.also {
                it.r = nr
                it.c = nc
            }
        } else {
            return -1
        }
    }
    return count
}
fun permutation(depth: Int, route: Stack<Int>) {
    if (depth == 3) {

        val count = calcFishCount(route.toList())
        if ((maxRoute.isNotEmpty() && count <= max) || count == -1) return
        max = count
        maxRoute = route.toList()
        return
    }

    for (i in 0 until 4) {
        route.push(i)
        permutation(depth + 1, route)
        route.pop()
    }
}
fun moveFish() {
    val size = fish.size
    for (i in 0 until size) {
        var curFish = fish.poll()

        for (i in 1..8) {
            val nr = curFish.r + dr[curFish.dir]
            val nc = curFish.c + dc[curFish.dir]
            // 격자 안, 냄새, 상어 피한다.
            if (inRange(nr, nc) && water[SMELL][nr][nc] == 0 && !(nr == shark.r && nc == shark.c)) {
                water[FISH][curFish.r][curFish.c] =
                    if (water[FISH][curFish.r][curFish.c] == 0) 0
                    else water[FISH][curFish.r][curFish.c] - 1
                water[FISH][nr][nc]++
                curFish.also {
                    it.r = nr
                    it.c = nc
                }
                break
            } else {
                curFish.dir = if (curFish.dir == 0) 7 else curFish.dir - 1
            }
        }
        fish.offer(curFish)
    }
}
fun inRange(r: Int, c: Int): Boolean = r in 0..3 && c in 0..3
data class Shark(var r: Int, var c: Int)
data class Fish(var r: Int, var c: Int, var dir: Int)