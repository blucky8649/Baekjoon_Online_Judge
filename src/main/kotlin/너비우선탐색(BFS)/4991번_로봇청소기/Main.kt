package `너비우선탐색(BFS)`.`4991번_로봇청소기`

import java.lang.Integer.min
import java.util.*
import kotlin.collections.ArrayList

private lateinit var isVisited : Array<Boolean>
var answer = Integer.MAX_VALUE

fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    while (true) {
        answer = Integer.MAX_VALUE
        val(C, R) = readLine().split(" ").map { it.toInt() }
        if (R + C == 0) break
        val dust = Array<Dot>(11) {Dot(0, 0)}
        var dust_idx = 1
        val map : Array<Array<Char>> = Array(R){Array(C){' '}}
        var start : Dot = Dot(0, 0)
        for (i in 0 until R) {
            val str = readLine()
            for (j in 0 until C) {
                map[i][j] = str[j]
                if (map[i][j] == '*') dust[dust_idx++] = (Dot(j, i))
                else if (map[i][j] == 'o') dust[0] = Dot(j, i)
            }
        }
        val list = Array(dust_idx) {ArrayList<Node>()}
        /** BFS를 사용하여 모든 Dust로 가는 최단거리를 찾는다. **/

        for (i in 0 until dust_idx) {
            for (j in i + 1 until dust_idx) {
                val weight = BFS(R, C, dust[i], dust[j], map)
                if (weight== -1) continue
                list[i].add(Node(j, weight))
                list[j].add(Node(i, weight))
            }
        }
        isVisited = Array(dust_idx) {false}
        isVisited[0] = true
        Permutation(0, 0, list, dust_idx, 0)
        answer = if (answer == Integer.MAX_VALUE) { -1 } else {answer}
        println(answer)
    }
}

/**
 * @param cur : 현재 위치
 * @param depth : 백트래킹 깊이
 * @param list : 인접리스트
 * @param size : 먼지의 개수
 * @param sum : 총 쓸고나간 거리
 */
fun Permutation(cur : Int, depth : Int, list : Array<ArrayList<Node>>, size : Int, sum : Int) {
    if (depth == size - 1) {
        answer = min(answer, sum)
        return
    }
    for (next in list[cur] ) {
        if (isVisited[next.end]) continue
        isVisited[next.end] = true
        Permutation(next.end, depth + 1, list, size, sum + next.weight)
        isVisited[next.end] = false
    }
}
fun BFS(R : Int, C : Int, start : Dot, end : Dot, map : Array<Array<Char>>) : Int {
    val dy = arrayOf(-1, 0, 1, 0)
    val dx = arrayOf(0, 1, 0, -1)
    val isVisited = Array(R) {Array<Boolean> (C) {false} }
    val q : Queue<Dot> = LinkedList<Dot>()
    isVisited[start.y][start.x] = true
    q.offer(Dot(start.x, start.y, start.moved))

    while (!q.isEmpty()) {
        val cur = q.poll()

        if (cur.x == end.x && cur.y == end.y) return cur.moved
        for (i in 0 .. 3) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (nx in 0 until C && ny in 0 until R && !isVisited[ny][nx] && map[ny][nx] != 'x') {
                q.offer(Dot(nx, ny, cur.moved + 1))
                isVisited[ny][nx] = true
            }
        }
    }
    return -1
}
data class Dot (var x : Int, var y : Int){
    var moved : Int = 0
    constructor(x : Int, y : Int, _moved : Int) : this(x, y){
        moved = _moved
    }
}
data class Node(val end : Int, val weight : Int)
