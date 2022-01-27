package 위상정렬.`23059번_리그오브레게노`

import java.util.*
import kotlin.collections.*

fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    val list = HashMap<String, ArrayList<String>>()
    val indegree = HashMap<String, Int>()

    repeat(readLine().toInt()) {
        val (start, end) = readLine().split(" ")
        list.putIfAbsent(start, ArrayList<String>())
        list.putIfAbsent(end, ArrayList<String>())
        list[start]?.add(end)
        indegree.putIfAbsent(end, 0)
        indegree.putIfAbsent(start, 0)
        indegree[end] = indegree[end]?.plus(1) ?: 0

    }

    topological_sort(list, indegree)
}
fun topological_sort(list : HashMap<String,ArrayList<String>>, indegree : HashMap<String, Int>) {
    val q : Queue<Node> = LinkedList<Node>()
    val result = PriorityQueue<Node>()
    indegree.forEach { (k, v) ->
        if (v == 0) {
            q.offer(Node(k, 0))
        }
    }

    while (!q.isEmpty()) {
        val cur = q.poll()
        result.add(cur)
        for (item in list[cur.value]!!) {
            indegree[item] = indegree[item]?.minus(1) ?: 0
            if (indegree[item] == 0) {
                q.offer(Node(item, cur.priority + 1))
            }
        }
    }
    if (result.size != list.size) {
        println(-1)
        return
    }
    val sb = StringBuilder()
    while (!result.isEmpty()) {
        sb.append(result.poll().value + " ")
    }
    println(sb.toString())
}

data class Node(val value : String, val priority : Int) : Comparable<Node> {
    override fun compareTo(other: Node): Int {
        if (priority == other.priority) { value.compareTo(other.value) }
        return this.priority - other.priority
    }
}