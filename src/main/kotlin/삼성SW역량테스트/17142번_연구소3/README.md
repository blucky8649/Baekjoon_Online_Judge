![썸네이루_삼성-001 (3)](https://user-images.githubusercontent.com/83625797/154609533-9ddf2989-a045-45c3-82e1-9196feebf0ab.png)

[문제 풀러 가기!](https://www.acmicpc.net/problem/17142)
## 풀이

### 문제 접근 방법

>**시간 제한**
>Java 8: 1 초
>Kotlin (JVM): 1 초

시간 제한을 1초로 제한을 둔다는 것의 의미는 **시간 복잡도 및 효율성에 신경을 써라** 라는 의미가 내포되어있습니다.

따라서 이번 문제는 보자마자 **조합과 BFS**가 생각났는데 1초안에 풀 수 있는지 계산을 해봅시다.

시간 복잡도 계산을 하기 위해서는 먼저 문제의 **제한사항**을 보셔야 합니다.

> 첫째 줄에 연구소의 크기 **8(4 ≤ N ≤ 50)**, 놓을 수 있는 바이러스의 개수 **M(1 ≤ M ≤ 10)** 이 주어진다.  
> 둘째 줄부터 N개의 줄에 연구소의 상태가 주어진다. 0은 빈 칸, 1은 벽, 2는 바이러스를 놓을 수 있는 위치이다. **2의 개수는 M보다 크거나 같고, 10보다 작거나 같은 자연수이다.**

`조합(Permutation)` : 최악의 경우는 10C5 이므로 최대 경우의 수는 **252회**가 됩니다.

`연구소 탐색` : 연구소의 최대 크기는 **50*50** 이므로 최대 **2500**의 연산량이 필요합니다.

통상 자바에서는 **1초에 대략 10억회**의 연산을 합니다. 
이 문제를 풀기 위해서는 최대 **630,000회(252 * 2500)의 연산량**밖에 필요로 하지 않으므로 `조합 + BFS`만으로 충분한 연산이 가능합니다.


### 1단계. 조합 구하기
바이러스가 활성화될 수 있는 자리에서 M곳의 바이러스를 활성화하는 알고리즘을 작성해줍시다. M개의 바이러스를 활성화 시켰으면, BFS를 통해 바이러스를 퍼뜨려줘야합니다.  
효율성 향상을 위해 **중복 없는 조합**을 구해줍니다.

```kotlin
fun Permutation(start: Int, depth: Int, m: Int) {
    if (depth == m) {
        // BFS를 통해 바이러스를 흩뿌려준다.
        spreadVirus(sizeWithoutVirus)
        return
    }

    for (i in start until virusPosList.size) {
        virusSelected.push(virusPosList[i])
        Permutation(i + 1, depth + 1, m)
        virusSelected.pop()
    }
}
```

### 2단계. 바이러스 퍼뜨리기(BFS)
바이러스는 일반적인 BFS로 퍼뜨릴 수 있으나, **빈 연구실의 크기, 퍼뜨리는데 걸린 시간**을 지속적으로 갱신해주셔야합니다.
```kotlin
fun spreadVirus(sizeWithoutVirus: Int) {
    var size = sizeWithoutVirus
    val isVisited = Array(rab.size) { BooleanArray(rab.size) }
    var time = 0 // 최대 걸린 시간
    val q: Queue<Virus> = LinkedList<Virus>()

    for (v in virusSelected) {
        q.offer(v)
        isVisited[v.y][v.x] = true
    }

    while (q.isNotEmpty()) {
        val cur = q.poll()
        time = max(time, cur.cnt)

        for (i in dy.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (inRange(nx, ny) && !isVisited[ny][nx] && rab[ny][nx] != -1) {
                
                if (rab[ny][nx] == 0) {
                    size--
                }
                if (size == 0) {
                    answer = min(answer, cur.cnt + 1)
                    return
                }
                isVisited[ny][nx] = true
                q.offer(Virus(nx, ny, cur.cnt + 1))
            }
        }
    }
}

fun inRange(x: Int, y: Int) = (x in rab.indices && y in rab.indices)
```

## 전체 소스 코드
```kotlin
package 삼성SW역량테스트.`17142번_연구소3`

import java.util.*
import kotlin.math.*

private lateinit var rab: Array<IntArray>
private lateinit var isSelected: BooleanArray
private val dy = arrayOf(-1, 0, 1, 0)
private val dx = arrayOf(0, 1, 0, -1)
private val virusPosList = ArrayList<Virus>()
private val virusSelected = Stack<Virus>()
private var sizeWithoutVirus = 0
private var answer = Integer.MAX_VALUE

fun main() {
    val (n, m) = readln().split(" ").map { it.toInt() }
    sizeWithoutVirus = n * n
    rab = Array(n) { IntArray(n) }
    for (i in 0 until n) {
        val st = StringTokenizer(readln())
        for (j in 0 until n) {
            rab[i][j] = st.nextToken().toInt()
            if (rab[i][j] == 2) {
                virusPosList.add(Virus(j, i, 0))
                rab[i][j] = -9
                sizeWithoutVirus--
            } else if (rab[i][j] == 1) {
                rab[i][j] = -1 // 벽
                sizeWithoutVirus--
            }
        }
    }
    if (sizeWithoutVirus == 0) {
        println(0)
        return
    }
    // 1. 바이러스가 될 수 있는 모든 자리에서 m개의 자리를 뽑는 조합을 구한다.
    isSelected = BooleanArray(virusPosList.size)
    Permutation(0, 0, m)

    println(if (answer == Int.MAX_VALUE) -1 else answer)
}

fun Permutation(start: Int, depth: Int, m: Int) {
    if (depth == m) {
        // 2. BFS를 통해 바이러스를 흩뿌려준다.
        spreadVirus(sizeWithoutVirus)
        return
    }

    for (i in start until virusPosList.size) {
        virusSelected.push(virusPosList[i])
        Permutation(i + 1, depth + 1, m)
        virusSelected.pop()
    }
}

fun spreadVirus(sizeWithoutVirus: Int) {
    var size = sizeWithoutVirus
    val isVisited = Array(rab.size) { BooleanArray(rab.size) }
    var time = 0 // 최대 걸린 시간
    val q: Queue<Virus> = LinkedList<Virus>()

    for (v in virusSelected) {
        q.offer(v)
        isVisited[v.y][v.x] = true
    }

    while (q.isNotEmpty()) {
        val cur = q.poll()
        time = max(time, cur.cnt)

        for (i in dy.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (inRange(nx, ny) && !isVisited[ny][nx] && rab[ny][nx] != -1) {

                if (rab[ny][nx] == 0) {
                    size--
                }
                if (size == 0) {
                    answer = min(answer, cur.cnt + 1)
                    return
                }
                isVisited[ny][nx] = true
                q.offer(Virus(nx, ny, cur.cnt + 1))
            }
        }
    }
}

fun inRange(x: Int, y: Int) = (x in rab.indices && y in rab.indices)
data class Virus(val x: Int, val y: Int, val cnt: Int)
```
