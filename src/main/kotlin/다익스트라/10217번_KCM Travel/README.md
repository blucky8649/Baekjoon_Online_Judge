
![](https://media.vlpt.us/images/blucky8649/post/a196e702-e790-4b81-95dd-fcc1a494da86/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001.png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/10217)

## 풀이

다익스트라에 동적프로그래밍(DP) 개념이 들어간 문제입니다..

일반적인 다익스트라와 달리, 이 문제는 비용이라는 매개변수녀석이 추가적으로 들어갑니다.

그러나 초기 문제를 해석할 때 그냥 일반 다익스트라로 풀 수 있을줄 알았습니다.

```kotlin
fun dijkstra(start: Int, dst: IntArray, M: Int, N: Int, nodeList: Array<ArrayList<Node>>): Int {
    val isVisited = BooleanArray(N + 1)
    val q = PriorityQueue<Node>()
    Arrays.fill(dst, INF)
    q.offer(Node(start, 0, 0))
    dst[start] = 0

    while (!q.isEmpty()) {
        val cur: Node = q.poll()

        if (isVisited[cur.end]) continue
        isVisited[cur.end] = true

        for (next in nodeList[cur.end]) {
            if (dst[next.end] > dst[cur.end] + next.weight && cur.cost + next.cost <= M) {
                q.offer(Node(next.end, dst[next.end], cur.cost + next.cost))
                dst[next.end] = dst[cur.end] + next.weight
            }
        }
    }
    return dst[N]
}
```
요딴식으로 말이죠.  
비용에 대한 고려를 `다음노드에 가는 총비용이 M을 초과할 경우에 스킵하는 로직`으로 짰으나 시원하게 **틀렸습니다**가 나왔습니다. 


질문 게시판에 찾아가 보니 좋은 반례가 있었습니다.
```
1
6 149 8
1 2 60 20
2 3 30 70
1 3 100 80
1 3 20 180
3 4 20 100
3 5 150 20
5 6 50 40
4 6 30 50
ans: 240
wrong: Poor KCM
```

이 반례인데요, 보기 쉽게 그림으로 그려봅시다.

<div align = "left" > <img src="https://images.velog.io/images/blucky8649/post/d0bb889a-0bb6-4828-a85b-4c378e3055ce/%EA%B7%B8%EB%9E%98%ED%94%84%20%EB%B0%98%EB%A1%80.png"> </div>
빨간색 값은 가는 비용, 파란색 값은 가는 거리를 나타내었습니다.

여기에서 만약, 답으로 가는 길을 구한다면 다음과 같이 그릴 수 있습니다.
<div align = "left" > <img src="https://images.velog.io/images/blucky8649/post/3bef3687-88b9-4a08-846e-da735ac69caa/image.png"> </div>
  
살짝 감이 잡히시나요?  

저는 비용 1번 공항에서 2번을 거쳐 3번 공항으로 가고 싶은데, 그땐 이미 dst[3]은 최소거리인 **80**으로 갱신되어 못가게 됩니다.  
  
이 로직 오류를 방지하기 위해 dst 배열을 2차원으로 dst[도착노드][소요비용] 의 형식으로 2차원 배열로 구성하면 됩니다.
그렇게 된다면 20의 비용으로 3번을 가는 거리를 따로 갱신할 수 있어 정확한 정답을 구할 수 있게됩니다.

## Source Code
```kotlin
package 다익스트라.`10217번_KCM Travel`

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
```

마치.. 여러 경우를 고려해야하는 2차원 행렬에서의 BFS로직의 isVisited를 다차원(3차원 이상) 배열로 구성하는 것과 비슷한 논리네요.  
좋은 다익스트라의 응용에 대한 공부가 될 수 있는 문제였습니다.
