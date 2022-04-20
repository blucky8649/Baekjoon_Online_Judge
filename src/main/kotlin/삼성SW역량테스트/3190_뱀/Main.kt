package 삼성SW역량테스트.`3190_뱀`

import java.util.*

val dr = listOf(-1, 0, 1, 0) // 북 동 남 서
val dc = listOf(0, 1, 0, -1)
fun main() {
    val n = readln().toInt()
    val board = Array(n) {IntArray(n)}
    val k = readln().toInt()

    for (i in 0 until k) {
        val (r, c) = readln().split(" ").map { it.toInt() - 1 }

        board[r][c] = 1
    }
    val l = readln().toInt()
    val moveList: Queue<Command> = LinkedList()
    for (i in 0 until l) {
        val st = StringTokenizer(readln())

        val x = st.nextToken().toInt()
        val c = st.nextToken()

        moveList.add(Command(x, c))
    }
    val snake = ArrayDeque<Snake>()
    snake.add(Snake(0, 0)) // 첫 시작 위치 설정
    var dir = 1
    var count = 0
    while (true) {
        // 현재 방향으로 한 칸 이동
        val nr = snake.peekFirst().r + dr[dir]
        val nc = snake.peekFirst().c + dc[dir]
        snake.addFirst(Snake(nr, nc))

        // 벽과 부딪히면 게임은 끝남
        if (!(snake.peekFirst().r in board.indices && snake.peekFirst().c in board[0].indices)) break

        // 자기 자신의 몸과 부딪히면 게임은 끝남
        if (board[nr][nc] == 9) break

        // 사과가 없으면 꼬리를 자른다.
        if (board[nr][nc] != 1) {
            val last = snake.pollLast()
            board[last.r][last.c] = 0
        }
        // 사과는 먹어서 없어진다.
        board[nr][nc] = 9


        count++
        // 만약 방향 전환 명령이 있으면, 방향 전환을 한다.
        moveList.peek()?.let {
            val peek = moveList.peek()
            if (count == peek.x) {
                moveList.poll()
                when (peek.c) {
                    "L" -> {
                        // 왼쪽 방향 전환
                        dir = (dir + 3) % 4
                    }
                    "D" -> {
                        // 오른쪽 방향 전환
                        dir = (dir + 1) % 4
                    }
                }
            }
        }
    }
    println(++count)
}
fun Array<IntArray>.printArray() {
    for (i in indices) {
        println(get(i).contentToString())
    }
}
data class Command(val x: Int, val c: String)
data class Snake(val r: Int, val c: Int)