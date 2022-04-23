[문제 풀러 가기!](https://www.acmicpc.net/problem/23290)

## 풀이
작년 하반기 오후반 기출 문제 1번입니다.
부끄럽게도 당시에는 3시간을 다써도 풀지 못하였는데요, 삼성 문제는 차근 차근 문제에 제시된 순서에 맞게 풀면 어렵지 않게 풀 수 있습니다.

### 시뮬레이션 로직
문제에서는 구현해나가야 할 시뮬레이션을 단계별로 제시해주고 있습니다. 다음 코드 스니핏과 같이 구현해야할 큰 틀을 설계해 보는 것도 괜찮은 방법입니다.
```kotlin
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
```

## 1. 상어가 복제 마법을 시전한다.
기존에는 다음코드와 같이 현재 유지중인 물고기 Queue를 복제하는 방식을 사용하였습니다.
```kotlin
val cloneFish: Queue<Fish> = LinkedList(fish)
```
이렇게 되면 문제점이 `cloneFish`와 `fish`에 있는 `Fish`클래스가 같은 주소값을 참조해버려 `fish`값의 변동 사항이 `cloneFish`에 영향을 끼치는 문제가 발생했습니다. 따라서 다음코드와 같이 객체를 새롭게 생성해주어 해결했습니다.
```kotlin
val cloneFish: Queue<Fish> = LinkedList()
fish.forEach {
    cloneFish.add(it.copy())
}
```

## 2. 모든 물고기가 한 칸 이동한다.
각각의 물고기를 **반시계**방향으로 회전시켜, 적절한 위치로 이동시켜야 합니다. 만약 8방향 모두 갈 곳이 없으면 제자리에 머물 수 있도록 구현해나가야 합니다.
```kotlin
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
```

## 3. 상어가 연속해서 3칸 이동한다.
상어는 상하좌우로 3칸 이동하는 경로 중에 **물고기를 가장 많이 먹으면서 사전순으로 앞서는 이동 경로**로 이동시켜야 합니다.

그렇기 때문에 **4가지 방향중에 임의의 3가지를 뽑는 순열**을 DFS를 통해 구해주고 최적의 경로를 갱신하는 로직으로 구현했습니다.

```kotlin
fun moveShark(t: Int) {
    max = 0
    maxRoute = emptyList()
    val route = Stack<Int>()
    // 순열을 구하여 최적의 경로를 갱신하자.
    permutation(0, route)
    // 갱신된 최적의 경로를 기반으로 상어를 이동시키자.
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

fun permutation(depth: Int, route: Stack<Int>) {
    if (depth == 3) {
		// 해당 경로로 이동할 시 물고기를 얼마나 잡아먹을 수 있는지 카운트
        val count = calcFishCount(route.toList())
        // 만약 이동 가능하고 물고기를 최대한 잡아먹을 수 있다면 갱신한다.
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
// 특정 경로를 사전에 탐색하여 물고기를 얼마나 잡을 수 있는지 계산한다.
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
```

## 4. 두 번 전 연습에서 생긴 냄새를 제거한다.
유지중인 냄새 배열에서 2번 전에 생긴 냄새를 골라 제거 하는 로직을 구현하였습니다.
```kotlin
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
```

## 마지막. 복제 마법 적용
앞서 복사했던 `cloneFish`를 `fish`에 삽입하면 됩니다.
```kotlin
// 복제 마법 완료
fun cloneSpell(cloneFish: Queue<Fish>) {
    while (cloneFish.isNotEmpty()) {
        val curFish = cloneFish.poll()
        fish.add(curFish)
        water[FISH][curFish.r][curFish.c] += 1
    }
}
```

## Source Code
전체 소스 코드도 첨부해 드립니다. 5월 1일 시험 화이팅입니다^^
```kotlin
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
```
