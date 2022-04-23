[문제 풀러 가기!](https://www.acmicpc.net/problem/12100)

## 풀이
이 문제는 제한 시간이 5초지만 4방향으로 5번만 움직여 주면 되기 때문에 완전탐색(백트래킹)을 사용하였습니다.

### 백트래킹
상하좌우로 5번 이동했을 시점에서 최고값을 구해주면 됩니다. 
```kotlin
fun permutation(cnt: Int) {
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
        permutation(cnt + 1)
        for (i in board.indices) {
            board[i] = clone_board[i].clone()
        }
    }
}
```

### 블럭 이동
블럭을 이동하는데 있어서 짚고 넘어가야할 부분이 있습니다.
> 한 번의 이동에서 이미 합쳐진 블록은 또 다른 블록과 다시 합쳐질 수 없다. 

예를 들어 다음과 같은 게임 판이 존재한다고 가정해봅시다.  
|2|2|2|  
|2|2|2|  
|4|4|4|  
여기서 위로 한번 이동시켰을 때 다음과 같이 결과 값이 나와야합니다. 
|4|4|4|  
|4|4|4|  
|0|0|0|  

따라서 저는 방향별로 `스택`과 `cursor`라는 변수를 이용하여 블록들이 중복으로 합쳐질 수 없도록 로직을 작성했습니다.

```kotlin
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
```

## Source Code
```kotlin
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

    permutation(0)
    println(answer)

}
fun permutation(cnt: Int) {
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
        permutation(cnt + 1)
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
```
