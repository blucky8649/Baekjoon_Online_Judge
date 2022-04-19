package 삼성SW역량테스트.`12100번_2048`

import java.util.*
import kotlin.math.max

lateinit var board: Array<IntArray>
var n: Int = 0
var answer = 0
fun main() {
    n = readln().toInt()
    board = Array(n) { IntArray(n) }

    for (i in board.indices) {
        val str = readln()
        board[i] = str.split(" ").map { it.toInt() }.toIntArray()
    }

    permutation(0, "")
    println(answer)

}
fun permutation(cnt: Int, route: String) {
    if (cnt == 5) {
        board.forEach { array ->
            array.forEach { num ->
                answer = max(answer, num)
            }
        }
        return
    }

    val clone_board = Array(n) {IntArray(n)}
    for (i in board.indices) {
        clone_board[i] = board[i].clone()
    }
    for (i in 0..3) {
        move(i)
        permutation(cnt + 1, "$route $i")
        for (i in board.indices) {
            board[i] = clone_board[i].clone()
        }
    }
}

fun move(rotate: Int) {
    val clone_board = Array(n) { IntArray(n) }
    when (rotate) {
        0 -> {
            // 위 쪽으로
            for (c in board[0].indices) {
                val stack = Stack<Int>()
                var cursor = 0
                for (r in board.indices) {
                    if (board[r][c] == 0) continue
                    if (stack.isEmpty() || board[r][c] != stack.peek() || cursor != board[r][c]) {
                        stack.push(board[r][c])
                        cursor = board[r][c]
                    } else {
                        stack.push(stack.pop() * 2)
                        cursor = 0
                    }
                }
                val list = stack.toList()
                for (i in list.indices) {
                    clone_board[i][c] = list[i]
                }
            }
        }
        1 -> {
            // 아래쪽으로
            for (c in board[0].indices) {
                val stack = Stack<Int>()
                var cursor = 0
                for (r in board.size - 1 downTo 0) {
                    if (board[r][c] == 0) continue
                    if (stack.isEmpty() || board[r][c] != stack.peek() || cursor != board[r][c]) {
                        stack.push(board[r][c])
                        cursor = board[r][c]
                    } else {
                        stack.push(stack.pop() * 2)
                        cursor = 0
                    }
                }

                val list = stack.toList()
                for (i in list.indices) {
                    clone_board[board.size - 1 - i][c] = list[i]
                }
            }
        }
        2 -> {
            // 왼쪽으로
            for (r in board.indices) {
                val stack = Stack<Int>()
                var cursor = 0
                for (c in board[0].indices) {
                    if (board[r][c] == 0) continue
                    if (stack.isEmpty() || board[r][c] != stack.peek() || cursor != board[r][c]) {
                        stack.push(board[r][c])
                        cursor = board[r][c]
                    } else {
                        stack.push(stack.pop() * 2)
                        cursor = 0
                    }
                }
                val list = stack.toList()
                for (i in list.indices) {
                    clone_board[r][i] = list[i]
                }
            }
        }
        3 -> {
            // 오른쪽으로
            for (r in board.indices) {
                val stack = Stack<Int>()
                var cursor = 0
                for (c in board[0].size - 1 downTo 0) {
                    if (board[r][c] == 0) continue
                    if (stack.isEmpty() || board[r][c] != stack.peek() || cursor != board[r][c]) {
                        stack.push(board[r][c])
                        cursor = board[r][c]
                    } else {
                        stack.push(stack.pop() * 2)
                        cursor = 0
                    }
                }
                val list = stack.toList()
                for (i in list.indices) {
                    clone_board[r][board.size - 1 - i] = list[i]
                }
            }
        }
    }
    for (i in board.indices) {
        board[i] = clone_board[i].clone()
    }
}