![썸네이루_삼성-001 (5)](https://user-images.githubusercontent.com/83625797/154782892-b5d14404-198e-42a5-b48c-de5cb35a7e01.png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/17143)

## 풀이
이 문제 역시 문제를 잘 읽고, 단계별로 차분히 풀어나가시면 어렵지 않게 푸실 수 있습니다.

이문제의 제한 시간은 **1초**로 되어있습니다.
제한 시간이 1초라는건 주먹구구식으로 풀면 무조건 시간초과가 나도록 설계했다는 것이 됩니다.

따라서 이 문제를 푸실 땐 **미리** 어느 자료구조를 사용하여 효율성을 높일것인가가 중요합니다.

저는 이 문제를 풀 때 `2차원 배열`과 `해시맵` 자료구조를 사용하였습니다.
>`2차원 배열` : 같은 열에 있는 상어를 효율 적으로 잡을 수 있도록 상어를 담은 2차원 배열을 선언하였습니다.
>`해시맵` : 낚시왕이 물고기를 잡은 이후 상어의 움직임을 빠르게 변경하기 위해 사용하였습니다. `ArrayList`도 고려해보았으나, 데이터 삭제시 **N만큼의 시간을 소모**하기 때문에 비효율적이라 판단하였습니다.

이 문제에서는 다행히 어떤 방향으로 문제를 풀어나갈지 정해줍니다.
```
1. 낚시왕이 오른쪽으로 한 칸 이동한다.
2. 낚시왕이 있는 열에 있는 상어 중에서 땅과 제일 가까운 상어를 잡는다. 상어를 잡으면 격자판에서 잡은 상어가 사라진다.
3. 상어가 이동한다.

```
이 순서대로 풀어보도록 하겠습니다.

### step 1. 낚시왕이 오른쪽으로 한 칸 이동한다.
낚시왕이 한칸 씩 이동하면서 물고기를 잡도록 **시뮬레이션** 해주셔야 합니다. 다음 코드와 같이 반복문을 사용하시면 됩니다.
```kotlin
var manX = 0
var weightCatched = 0
while (manX + 1 in water[0].indices) {
    // 1. 낚시왕이 오른쪽으로 한 칸 이동한다.
    manX++
    // 2. 낚시왕이 있는 열에 있는 상어 중에서 땅과 제일 가까운 상어를 잡는다.
    weightCatched += getFish(manX)
    // 3. 상어가 이동한다.
    positionShark()
}
```

### step 2. 낚시왕이 있는 열에 있는 상어 중에서 땅과 제일 가까운 상어를 잡는다.

이부분은 낚시왕의 X좌표와 같은 열에 있는 상어를 탐색하여 주시면 됩니다.
```kotlin
fun getFish(manX: Int): Int {
    for (i in water.indices) {

        if (water[i][manX].z != EMPTY) {
            val sharkNum = water[i][manX].z
            val weight = water[i][manX].z
            water[i][manX] = Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) // 해당 위치를 비운다.
            sharks.remove(sharkNum)
            return weight
        }
    }
    // 낚시왕이 있는 열에 물고기가 없으면 0 반환
    return 0
}
```

### step 3. 상어가 이동한다.
이 문제의 하이라이트입니다. 상어 정보가 담긴 `해시맵`을 탐색하여 상어를 이동시켜 새로운 2차원 배열에 배치시킵니다. 만약 그 2차원 배열에 다른 상어가 있다면 크기가 큰 상어가 잡아먹습니다.
아래 코드는 **상어를 어항에 재배치**하는 코드입니다.

```kotlin
fun positionShark() {
    val sClone = HashMap<Int, Shark>(sharks)
    // 빈 수조 생성: 상어를 재배치 하기 위함
    val tmp = Array(water.size) { Array(water[0].size) { Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) } }
    sClone.forEach { (num, shark) ->
        val targetShark = moveShark(shark.s, shark)
        // 만약 수조가 비어있다면 상어를 놓고, 만약 어떤 상어가 자리잡고있다면 싸워야 한다.

        if (tmp[targetShark.r][targetShark.c].z == EMPTY) {
            tmp[targetShark.r][targetShark.c] = targetShark

        } else {

            if (tmp[targetShark.r][targetShark.c].z < targetShark.z) {
                val weakShark = tmp[targetShark.r][targetShark.c]
                tmp[targetShark.r][targetShark.c] = targetShark
                sharks.remove(weakShark.z)
            } else {
                sharks.remove(targetShark.z)
            }
        }
    }
    water = tmp // 수조 동기화
}
```
해시맵을 복사하여 사용한 이유는, 원본을 사용하여 상어데이터를 실시간으로 제거할 경우 오류가 나기 때문입니다.

다음은 **상어 이동** 메서드입니다. 한 칸씩 이동하는 방법도 있으나, 시간 효율을 위해 `재귀`를 사용하였습니다.

```kotlin
fun moveShark(remain: Int, shark: Shark): Shark {
    /**
     * How does a shark move?
     * ..방향에 따라 나눠야된다. (상 하 우 좌)
     * ..만약, 상어가 격자 밖으로 벗어나려고 하면 반대 방향으로 가게 유도해준다. (Using recursion)
     */

    when (shark.d) {
        1 -> {
            if (shark.r - remain < 1) {
                val moveDist = shark.r - 1 // 한쪽 끝으로 가는 이동거리
                shark.also {
                    it.r = 1 // 일단 한쪽 끝으로 이동
                    it.d = 2 // 방향 전환
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.r -= remain
            }
        }

        2 -> {
            if (shark.r + remain > R) {
                val moveDist = R - shark.r
                shark.also {
                    it.r = R
                    it.d = 1
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.r += remain
            }
        }

        3 -> {
            if (shark.c + remain > C) {
                val moveDist = C - shark.c
                shark.also {
                    it.c = C
                    it.d = 4
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.c += remain
            }
        }

        4 -> {
            if (shark.c - remain < 1) {
                val moveDist = shark.c - 1
                shark.also {
                    it.c = 1
                    it.d = 3
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.c -= remain
            }
        }
    }
    return shark
}
```

## Source Code
```kotlin
package 삼성SW역량테스트.`17143번_낚시왕`

private lateinit var water: Array<Array<Shark>>
private lateinit var input: List<Int>
private lateinit var sharks: HashMap<Int, Shark>
private var R = 0
private var C = 0
const val EMPTY = -1

fun main() {
    input = readln().split(" ").map { it.toInt() }
    R = input[0]
    C = input[1]
    water = Array(R + 1) { Array<Shark>(C + 1) { Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) } }
    sharks = HashMap<Int, Shark>()

    repeat(input[2]) {
        val (r, c, s, d, z) = readln().split(" ").map { it.toInt() }
        val shark = Shark(r, c, s, d, z)
        water[r][c] = shark
        sharks[z] = shark
    }
    var manX = 0
    var weightCatched = 0
    while (manX + 1 in water[0].indices) {
        // 1. 낚시왕이 오른쪽으로 한 칸 이동한다.
        manX++
        // 2. 낚시왕이 있는 열에 있는 상어 중에서 땅과 제일 가까운 상어를 잡는다.
        weightCatched += getFish(manX)
        // 3. 상어가 이동한다.
        positionShark()
    }
    println(weightCatched)
}

fun positionShark() {
    val sClone = HashMap<Int, Shark>(sharks)
    // 빈 수조 생성: 상어를 재배치 하기 위함
    val tmp = Array(water.size) { Array(water[0].size) { Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) } }
    sClone.forEach { (num, shark) ->
        val targetShark = moveShark(shark.s, shark)
        // 만약 수조가 비어있다면 상어를 놓고, 만약 어떤 상어가 자리잡고있다면 싸워야 한다.

        if (tmp[targetShark.r][targetShark.c].z == EMPTY) {
            tmp[targetShark.r][targetShark.c] = targetShark

        } else {

            if (tmp[targetShark.r][targetShark.c].z < targetShark.z) {
                val weakShark = tmp[targetShark.r][targetShark.c]
                tmp[targetShark.r][targetShark.c] = targetShark
                sharks.remove(weakShark.z)
            } else {
                sharks.remove(targetShark.z)
            }
        }
    }
    water = tmp // 수조 동기화
}

/**
 * @param remain: 상어의 남은 이동거리
 */
fun moveShark(remain: Int, shark: Shark): Shark {
    /**
     * How does a shark move?
     * ..방향에 따라 나눠야된다. (상 하 우 좌)
     * ..만약, 상어가 격자 밖으로 벗어나려고 하면 반대 방향으로 가게 유도해준다. (Using recursion)
     */

    when (shark.d) {
        1 -> {
            if (shark.r - remain < 1) {
                val moveDist = shark.r - 1 // 한쪽 끝으로 가는 이동거리
                shark.also {
                    it.r = 1 // 일단 한쪽 끝으로 이동
                    it.d = 2 // 방향 전환
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.r -= remain
            }
        }

        2 -> {
            if (shark.r + remain > R) {
                val moveDist = R - shark.r
                shark.also {
                    it.r = R
                    it.d = 1
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.r += remain
            }
        }

        3 -> {
            if (shark.c + remain > C) {
                val moveDist = C - shark.c
                shark.also {
                    it.c = C
                    it.d = 4
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.c += remain
            }
        }

        4 -> {
            if (shark.c - remain < 1) {
                val moveDist = shark.c - 1
                shark.also {
                    it.c = 1
                    it.d = 3
                }
                moveShark(remain - moveDist, shark)
            } else {
                shark.c -= remain
            }
        }
    }
    return shark
}

fun getFish(manX: Int): Int {
    for (i in water.indices) {

        if (water[i][manX].z != EMPTY) {
            val sharkNum = water[i][manX].z
            val weight = water[i][manX].z
            water[i][manX] = Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) // 해당 위치를 비운다.
            sharks.remove(sharkNum)
            return weight
        }
    }
    // 낚시왕이 있는 열에 물고기가 없으면 0 반환
    return 0
}

data class Shark(var r: Int, var c: Int, var s: Int, var d: Int, var z: Int)
```
