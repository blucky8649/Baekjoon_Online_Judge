package 삼성SW역량테스트.`16235번_나무재테크`

import java.util.*

lateinit var nut: Array<IntArray>
lateinit var ground: Array<IntArray>
var trees = PriorityQueue<Tree>()
val dead: Queue<Tree> = LinkedList()
val dr = listOf(-1, 0, 1, 0, -1, -1, 1, 1)
val dc = listOf(0, 1, 0, -1, -1, 1, -1, 1)
fun main() {
    /**
     * N: 땅의 한 변의 크기
     * M: 나무의 개수
     * K: 목표 년도
     */

    val (N, M, K) = readln().split(" ").map { it.toInt() }
    nut = Array(N) { IntArray(N) }
    ground = Array(N) { IntArray(N) { 5 } }
    for (i in nut.indices) {
        nut[i] = readln().split(" ").map { it.toInt() }.toIntArray()
    }

    for (i in 0 until M) {
        val (r, c, lev) = readln().split(" ").map { it.toInt() }
        trees.offer(Tree(r - 1, c - 1, lev))
    }
    var year = 1
    while (year <= K) {
        // 봄 : 나무들이 양분을 먹고 자라거나, 죽는다.
        spring()
        // 여름 : 죽은 나무가 양분으로 변한다.
        summer()
        // 가을 : 레벨이 5의 배수인 나무가 번식한다.
        autumn()
        // 겨울 : S2D2가 땅을 돌아다니며 양분을 추가한다.
        winter()

        year++
    }
    println(trees.size)
}
fun winter() {
    for (i in ground.indices) {
        for (j in ground.indices) {
            ground[i][j] += nut[i][j]
        }
    }
}
fun autumn() {
    val survive = PriorityQueue(trees)
    while (trees.isNotEmpty()) {
        val tree = trees.poll()
        if (tree.lev % 5 == 0) {
            for (i in dr.indices) {
                val nr = tree.r + dr[i]
                val nc = tree.c + dc[i]
                if (inRange(nr, nc)) {
                    survive.offer(Tree(nr, nc, 1))
                }
            }
        }
    }
    trees = PriorityQueue(survive)
}
fun inRange(r: Int, c: Int): Boolean = r in ground.indices && c in ground.indices
fun summer() {
    while (dead.isNotEmpty()) {
        val deadTree = dead.poll()
        ground[deadTree.r][deadTree.c] += deadTree.lev / 2
    }
}
fun spring() {
    val survive = PriorityQueue<Tree>()
    while (trees.isNotEmpty()) {
        val tree = trees.poll()
        if (tree.lev <= ground[tree.r][tree.c]) {
            survive.offer(Tree(tree.r, tree.c, tree.lev + 1))
            ground[tree.r][tree.c] -= tree.lev
        } else {
            dead.offer(tree)
        }
    }
    trees = PriorityQueue(survive)
}
data class Tree(val r: Int, val c: Int, val lev: Int) : Comparable<Tree> {
    override fun compareTo(other: Tree) : Int = lev - other.lev
 }
