package 유니온파인드.`4195번_친구_네트워크`

import java.util.*
import kotlin.collections.HashMap

private lateinit var map : HashMap<String, Int>
private lateinit var parents : Array<Int>
private lateinit var weight : Array<Int>
fun main() = with(System.`in`.bufferedReader()){
    val t = readLine().toInt() // 테스트 케이스 개수

    val sb = StringBuilder()
    for (i in 0 until t) {
        val m = readLine().toInt() // 친구 관계의 수
        map = HashMap<String, Int>()

        parents = Array(m * 2) {i -> i}
        weight = Array(m * 2) {i -> 1}

        var index = 0
        repeat(m) {
            val (start, end) = readLine().split(" ").map { it }
            if (!map.containsKey(start)) {
                map[start] = index++
            }
            if (!map.containsKey(end)) {
                map[end] = index++
            }
            sb.append(union(map[start]!!, map[end]!!)).append('\n')
        }
    }
    print(sb.toString())
}
fun find(x: Int) : Int {
    if (parents[x] == x) {
        return x
    }
    parents[x] = find(parents[x])
    return parents[x]
}

fun union(a : Int, b : Int)  : Int {
    // a와 b의 부모노드를 구함
    val A = find(a)
    val B = find(b)

    // 만약 a와 b의 부모가 같으면 서로 이미 이어져 있는 것이므로 기존값 출력
    if (A == B) { return weight[A] }
    // 부모가 다르다면 부모를 같게 이어주어야함
    parents[B] = A
    // B의 친구네트워크를 A에 흡수해줌
    weight[A] += weight[B]

    return weight[A]
}
data class Node(val  start : String, val end : String, val weight : Int)