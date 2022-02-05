package 다익스트라.`KCM Travel`

import java.util.*

const val INF = 1_000_000_000
fun main(args: Array<String>) = with(System.`in`.bufferedReader()) {
    val sb = StringBuilder()
    repeat(readLine().toInt()) {
        val (N, M, K) = readLine().split(" ").map { it.toInt() }
        val nodeList = Array(N + 1) { ArrayList<Node>() }
        val dst = Array(N + 1) { IntArray(M + 1) }
        repeat(K) {
            val (u, v, c, d) = readLine().split(" ").map { it.toInt() }
            nodeList[u].add(Node(v, d, c))
        }
        val answer = dijkstra(1, dst, M, N, nodeList)
        sb.append("$answer \n")
    }
    print(sb)
}
fun dijkstra(start: Int, dst: Array<IntArray>, M: Int, N: Int, nodeList: Array<ArrayList<Node>>): String {
    val q = PriorityQueue<Node>()
    for (element in dst) {
        Arrays.fill(element, INF)
    }
    q.offer(Node(start, 0, 0))
    dst[start][0] = 0

    while (!q.isEmpty()) {
        val cur: Node = q.poll()

        // 현재 노드가 LA라면 지금 현재 거리를 출력 (우선순위 큐를 사용했으므로 무조건 최소 거리가 된다.)
        if (cur.end == N) return cur.weight.toString()

        for (next in nodeList[cur.end]) {
            val nextCost = cur.cost + next.cost
            if (nextCost > M) continue // 돈을 허용치 초과할 시 continue
            // next 노드를 'nextCost' 비용으로 가는 현재의 거리가 더 짧다면 갱신을 한다.
            if (dst[next.end][nextCost] > cur.weight + next.weight) {
                dst[next.end][nextCost] = cur.weight + next.weight
                q.offer(Node(next.end, dst[next.end][nextCost], cur.cost + next.cost))
            }
        }
    }
    return "Poor KCM"
}
data class Node(val end: Int, val weight: Int, val cost: Int) : Comparable<Node> {
    override fun compareTo(other: Node): Int = weight - other.weight
}