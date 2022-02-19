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
        var shark = Shark(r, c, s, d, z)
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
    val tmp = Array(water.size) { Array<Shark>(water[0].size) { Shark(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY) } }
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
                var moveDist = shark.r - 1 // 한쪽 끝으로 가는 이동거리
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
                var moveDist = R - shark.r
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
                var moveDist = C - shark.c
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
                var moveDist = shark.c - 1
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