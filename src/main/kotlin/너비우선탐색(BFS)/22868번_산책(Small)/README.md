[![백쥰](https://media.vlpt.us/images/blucky8649/post/717f89c5-e74d-4a6a-ae13-3e51d6e8da7b/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(4).png)](https://www.acmicpc.net/problem/22868)

[문제 풀러 가기!](https://www.acmicpc.net/problem/22868)

## 문제
코로나로 인하여 확찐자가 되버려 오늘부터 산책을 하려고 한다. 산책할 경로를 정하려고 한다.

현재 있는 곳 $S$에서 출발하여 $S$와 다른 곳인 $E$를 찍고 다시 $S$로 돌아오는 경로로 만들려고 한다. 산책을 할 때 이미 갔던 정점을 또 가기 싫어 $E$에서 $S$로 올 때 $S$에서 $E$로 간 정점을 제외한 다른 정점으로 이동하려고 한다. 또한 산책 거리가 긴 것을 싫어하여 $S$에서 $E$로 가는 짧은 거리와 $E$에서 $S$로 가는 짧은 거리를 원한다.

정점 $S$에서 정점 $E$로 이동할 때, 짧은 거리의 경로가 여러개 나올 수 있다. 그 중, 정점 $S$에서 정점 $E$로 이동한 경로를 나열했을 때, 사전순으로 가장 먼저 오는 것을 선택한다.

예를 들어, 정점 1에서 정점 2로 이동한다고 했을 때, 짧은 거리의 경로가 1 4 3 2와 1 3 4 2가 있다고 가정을 해보자. 두 개의 경로중 사전순으로 먼저 오는 것은 1 3 4 2이므로 정점 1에서 정점 2로 가는 최단 경로 중 두 번째 것을 선택한다.

이와 같이 산책 경로를 정할 때, 산책 전체 경로의 거리($S$에서 $E$로 가는 거리 + $E$에서 $S$로 가는 거리)를 구해보자.

## 입력
 첫 번째 줄에는 정점의 개수 $N$과 두 정점 사이를 잇는 도로의 개수 $M$이 공백으로 구분되어 주어진다.

두 번째 줄부터 $M + 1$ 번째 줄까지 정점 $A, B$가 공백으로 구분되어 주어진다. 정점 $A$와 정점 $B$ 사이의 거리는 항상 1이다. 이때, 정점 $A$와 정점 $B$는 양방향으로 이동해도 된다.

정점 $A$와 정점 $B$를 잇는 도로는 두개 이상 주어지지 않는다.

 $M + 2$번째 줄에는 정점 $S$와 정점 $E$가 공백으로 구분되어 주어진다.

**산책을 할 수 있는 경로가 있는 데이터만 주어진다.**

## 출력
산책의 전체 경로의 길이를 출력한다.

## 풀이
인접리스트를 활용한 BFS문제였습니다. 일반적으로 가중치가 없는 노드에서의 최단거리는 BFS를 사용하여 해결합니다.

일반적인 BFS문제인데, 짚고 넘어가야 할 예외사항이 있습니다.
>정점 S에서 정점 E로 이동할 때, 짧은 거리의 경로가 여러개 나올 수 있다. 그 중, 정점 S에서 정점 E로 이동한 경로를 나열했을 때, **사전순**으로 가장 먼저 오는 것을 선택한다.
예를 들어, 정점 1에서 정점 2로 이동한다고 했을 때, 짧은 거리의 경로가 1 4 3 2와 1 3 4 2가 있다고 가정을 해보자. 두 개의 경로중 사전순으로 먼저 오는 것은 **1 3 4 2**이므로 정점 1에서 정점 2로 가는 최단 경로 중 두 번째 것을 선택한다.

총 이동 경로를 String 문자열로 나열하였을 때 사전순으로 가장 앞에오는 루트를 채택한다고 적혀있습니다.
푸는 방법은 다음과 같습니다.

1. S -> E로 가는 모든 경로를 **완전탐색**한다.
물론 S->E로 가는 모든 경로들을  우선순위 큐에 넣고 peek에 있는 데이터를 추출하는 경우도 있습니다. 다만 문제 보기에 노드의 개수가 최대 1만개, 간선의 개수가 최대 5만개이기 때문에, 인접 행렬로 작성된다면 **O(10000^2)**, 인접 리스트로 작성된다면 **O(10000+50000)**의 시간복잡도가 형성됩니다. 물론 인접리스트로 작성된다면 통과될 수도 있겠지만, 효율적으로 좋지 않습니다.

2. 인접리스트를 **오름차순 정렬**한 뒤 BFS를 수행한다.
이는 제가 풀었던 방법입니다. 사전에 모든 인접리스트의 노드들을 오름차순 정렬해준다면, BFS를 돌려서 가장 처음으로 End point에 도달되는 값이 정답이 됩니다. 모든 경로들을 완전탐색할 필요 없이 빠르게 최단경로만 짚고 리턴해주면 되기 때문에 효율적으로 로직을 수행할 수 있습니다.

## Source Code
```kotlin
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
    isVisited[S] = false // 도착지점 미방문 처리
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

```
