package `너비우선탐색(BFS)`.`22868번_산책(Small)`

import java.util.*
import kotlin.collections.ArrayList

private lateinit var isVisited : Array<Boolean>
fun main(args : Array<String>) = with(System.`in`.bufferedReader()){
    val (N, M) = readLine().split(" ").map { it.toInt() }
    val list = Array(N + 1) {ArrayList<Int>()}

    repeat(M) {
        val (start, end) = readLine().split(" ").map { it.toInt() }
        list[start].add(end)
        list[end].add(start)
    }
    // 번호가 빠른 노드부터 방문할 수 있도록 인접리스트를 정렬해준다.
    for (i in 1 .. N) {
        list[i].sort()
    }

    var answer = 0
    isVisited = Array(N + 1) {false}
    val (S, E) = readLine().split(" ").map { it.toInt() }

    // 1. S지점에서 E지점으로 가는 최단거리를 구한다.
    answer += BFS(S, E, list)

    // 2. E지점에서 S지점으로 가는 최단거리를 구한다.
    isVisited[S] = false
    answer += BFS(E, S, list)
    println(answer)
}

fun BFS(start : Int, end : Int, list : Array<ArrayList<Int>>) : Int {
    val q : Queue<Node> = LinkedList()
    isVisited[start] = true
    q.offer(Node(start, 0,"$start"))

    while (!q.isEmpty()) {
        val cur = q.poll()

        if (cur.node == end) {
            val routes = cur.route.split(" ").map { it.toInt() }
            // 도착했다면 지금까지 온 경로만 방문한걸로 isVisited 배열을 다시 수정해주어야 한다.
            isVisited = Array(list.size) {false}
            for (item in routes) {
                isVisited[item] = true
            }
            return cur.cnt
        }

        for (next in list[cur.node]) {
            if (isVisited[next]) continue
            isVisited[next] = true
            q.offer(Node(next, cur.cnt + 1, cur.route + " $next"))
        }
    }
    return -1
}
data class Node(val node : Int, val cnt : Int, val route : String) : Comparable<Node> {
    override fun compareTo(other: Node): Int = node - other.node
}