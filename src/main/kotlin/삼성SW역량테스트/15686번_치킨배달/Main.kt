package 삼성SW역량테스트.`15686번_치킨배달`

import java.util.Stack
import kotlin.math.abs
import kotlin.math.min

val chickenList = ArrayList<Dot>()
val houseList = ArrayList<Dot>()
val selectedChickenStoreList = Stack<Dot>()
var answer = Int.MAX_VALUE
fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }

    for (i in 0 until N) {
        val str = readln().split(" ").map { it.toInt() }
        for (j in 0 until N) {
            val obj = str[j]
            if (obj == 2) {
                chickenList.add(Dot(i, j))
            } else if (obj == 1) {
                houseList.add(Dot(i, j))
            }
        }
    }
    selectChickenStore(0, 0, M)
    println(answer)
}
fun selectChickenStore(start: Int, countSelected: Int, M: Int) {
    if (countSelected == M) {
        answer = min(calcChickenDst(), answer)
        return
    }

    for (i in start until chickenList.size) {
        selectedChickenStoreList.push(chickenList[i])
        selectChickenStore(start + 1, countSelected + 1, M)
        selectedChickenStoreList.pop()
    }
}
fun calcChickenDst(): Int {
    var sum = 0
    houseList.forEach { house ->
        var minDst = Int.MAX_VALUE
        selectedChickenStoreList.forEach { chicken ->
            val dst = abs(house.r - chicken.r) + abs(house.c - chicken.c)
            minDst = min(dst, minDst)
        }
        sum += minDst
        if (sum > answer) return Int.MAX_VALUE
    }
    return sum
}
data class Dot(val r: Int, val c: Int)