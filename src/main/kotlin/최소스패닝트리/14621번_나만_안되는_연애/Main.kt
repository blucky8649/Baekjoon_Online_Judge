package 최소스패닝트리.`14621번_나만_안되는_연애`

import java.util.*

private lateinit var parents : Array<Int>
fun main(args : Array<String>) = with(System.`in`.bufferedReader()) {
    val (N, M) = readLine().split(" ").map { it.toInt() }
    val gender =  readLine().split(" ")
    val pq = PriorityQueue<Node>()
    parents = Array(N + 1) {i -> i}

    for(i in 0 until M) {
        val (start, end, weight) = readLine().split(" ").map {it.toInt()}
        /** 서로 성별이 다른 사람끼리만 연결 **/
        if (gender[start - 1] == gender[end - 1]) continue
        pq.offer(Node(start, end, weight))
    }
    var cnt = 0
    var ans = 0
    while (!pq.isEmpty()) {
        val cur = pq.poll()
        val a = find(cur.start)
        val b = find(cur.end)

        if (a == b) continue
        union(a, b)
        ans += cur.weight
        if (++cnt == N - 1) break
    }

    println(if (cnt < N - 1) {-1} else {ans})

}
fun union(a : Int, b : Int) {
    val A = find(a)
    val B = find(b)

    if (A == B) return
    parents[B] = A
}
fun find(x : Int) : Int{
    if (parents[x] == x) return x
    parents[x] = find(parents[x])
    return parents[x]
}

data class Node(val start : Int, val end : Int, val weight : Int) : Comparable<Node> {
    override fun compareTo(o : Node) = weight - o.weight
}