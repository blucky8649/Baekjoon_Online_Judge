package 수학.`22942번_데이터_체커`

import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    val n = readLine().toInt()
    val pq = PriorityQueue<Circle>()
    val arr = Array(n * 2) {Circle(0, false, 0)}

    var idx = 0;
    for(i in 0 until n) {
        val (x, r) = readLine().split(" ").map { it.toInt() }
        arr[idx++] = Circle(x - r, true, i)
        arr[idx++] = Circle(x + r, false, i)
    }
    arr.sortBy { it.x } // x축을 기준으로 정렬

    var circle = pq.poll()
    val stack = Stack<Circle>()

    for (next in arr) {
        if (stack.isEmpty()) {
            stack.push(next) ; continue
        }

        val cur = stack.lastElement()
        if (next.num == cur.num) {
            stack.pop() ; continue
        }

        if (cur.isOpen && !next.isOpen) {
            println("NO")
            return
        }
        stack.push(next)
    }
    println("YES")
}
data class Circle(val x : Int, val isOpen : Boolean, val num : Int)