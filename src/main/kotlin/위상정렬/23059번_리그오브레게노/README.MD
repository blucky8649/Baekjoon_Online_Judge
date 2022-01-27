![initial](https://media.vlpt.us/images/blucky8649/post/dce84936-5333-4f44-a3ef-b7bf6ffb72e1/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001.png)

[문제 풀러 가기!](https://www.acmicpc.net/problem/23059)
## 문제
백남이는 새 학기를 맞이하여, 리그 오브 레게노(League of Legeno)라는 게임을 시작했다. 리그 오브 레게노는 AOS(Aeon of Strife) 종류의 게임으로, 5명의 플레이어가 한 팀이 되어 상대편의 주요 건물을 부수는 것이 게임의 승리 목표이다. 게임 내에서 유저들은 게임에서 승리하기 위해 자신의 캐릭터의 능력치를 올리도록 해야 한다. 맵에 등장하는 몬스터나 상대 팀의 플레이어를 처치하며 경험치와 골드를 보상으로 얻고, 이 경험치를 통해 캐릭터의 레벨을 올림으로써 레벨 증가에 따른 능력치를 얻게 된다. 그러나 한 게임에서 레벨에 대한 일정 상한선이 존재한다. 다른 방법으로는 골드를 사용하여 아이템들을 구매함으로써 자신의 능력치를 높일 수 있다.

아이템 사이에 미리 정해진 구매 순서가 존재한다. 이제 막 게임을 시작한 백남이는 구매 순서 전체가 아니라 두 아이템 사이의 선후관계 일부만 알고 있다. 백남이가 다음 과정을 반복하여 아이템을 구매할 때, 아이템의 전체 구매 순서를 알아내자.

- 현재 구매할 수 있는 아이템 중 아직 구매하지 않은 아이템을 모두 찾는다.
- 찾은 아이템을 `사전 순`으로 모두 구매한다.

## 입력
> 첫째 줄에는 백남이가 알고 있는 아이템 사이의 관계의 수 $N$(1 ≤ $N$ ≤ 200,000)를 입력받는다. $N$개의 줄에 걸쳐서 아이템 이름을 의미하는 문자열 2개 A B가 주어진다. 아이템 A는 아이템 B를 구입하기 위해 앞서 구매해야 하는 것을 의미하며, 아이템 A와 아이템 B는 항상 다르다. 모든 아이템은 선후관계에서 적어도 한 번씩 등장한다. 아이템 이름은 알파벳 소문자로만 이루어져 있고, 공백을 포함하지 않는다. 아이템 이름의 길이는 1 이상 15 이하이다.


## 출력
> 먼저 구매해야 하는 아이템부터 순서대로 각 줄에 걸쳐서 출력하라. 단, 모든 아이템을 구매할 수 없다면 -1을 출력한다.

#### 예제 입력 1
```
4
galeforce everfrost
riftmaker everfrost
goredrinker galeforce
stridebreaker galeforce
```

#### 예제 출력 1
```
goredrinker
riftmaker
stridebreaker
galeforce
everfrost
```
#### 예제 입력 2
```
2
riftmaker galeforce
galeforce riftmaker
```

#### 예제 출력 2
```
-1
```

#### 예제 입력 3
```
2
goredrinker galeforce
riftmaker everfrost
```

#### 예제 출력 3
```
goredrinker
riftmaker
everfrost
galeforce
```

## 풀이
**`위상 정렬`**을 활용한 응용 문제이다.
기존 위상 정렬은 `Integer`로 구성된 `인접 리스트`를 만들었다면, 이 문제는 `HashMap`을 이용하여 `String`으로 구성된 `인접 리스트`를 만드는 것이 포인트다.

문제는 `Kotlin`으로 먼저 풀었지만 직관적인 가독성을 고려하여 `Java`로 설명하겠다.

위상 정렬에 관한 설명은 따로 포스팅할 예정이라서 본문에서 자세하게 설명하지 못하는 점 양해 부탁드립니다.

```java
/** 인접리스트와 Indegree 정보를 담을 HashMap 선언 **/
HashMap<String, ArrayList<String>> list = new HashMap<>();
HashMap<String, Integer> indegree = new HashMap<>();
```
필자는 이렇게 두 HashMap을 이용하여 입력 자료를 저장했다.


이제 본격적인 위상 정렬을 꺼내기에 앞서 문제의 핵심을 다시 한번 짚어보자. 

> - 현재 구매할 수 있는 아이템 중 아직 구매하지 않은 아이템을 모두 찾는다.
- **_찾은 아이템을 `사전 순`으로 모두 구매한다._**

찾은 아이템을 우선순위에 맞춰 사전순으로 정렬해주는 과정이 필요하다. ~~(필자는 이걸 못보고 맞왜틀을 시전하고 있었다.)~~

따라서 결과값을 우선순위, 사전순으로 정렬을 하기 위해서 다음과 같은 Data class를 선언해주었다.
```java
class Node implements Comparable<Node>{
    String value;
    int priority;
    Node(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }
    @Override
    public int compareTo(Node other) {
        if (this.priority == other.priority) {
            return this.value.compareTo(other.value);
        }
        return this.priority - other.priority;
    }

}
```
이후 다음 위상정렬 코드를 보자.
```java
static void topological_sort(HashMap<String, ArrayList<String>> list, HashMap<String, Integer> indegree) {
        Queue<Node> q = new LinkedList<>();
        PriorityQueue<Node> result = new PriorityQueue<>();

        for (String k : indegree.keySet()) {
            int v = indegree.get(k);
            if (v == 0) q.offer(new Node(k, 0)); // 초기 우선순위는 0 순위
        }

        while (!q.isEmpty()) {
            Node cur = q.poll();
            result.offer(cur);

            for (String item : list.get(cur.value)) {
                indegree.put(item, indegree.get(item) - 1);
                if (indegree.get(item) == 0) {
                    q.offer(new Node(item, cur.priority + 1)); // 우선순위 증가 후 queue에 추가
                }
            }
        }

        if (result.size() != list.size()) {
            System.out.println(-1);
            return;
        }

        StringBuilder sb = new StringBuilder();
        while (!result.isEmpty()) {
            sb.append(result.poll().value + " ");
        }
        System.out.println(sb.toString());
    }
}
```

이런식으로 우선순위를 고려하여 위상정렬을 구현하면 원하는 결과값을 출력할 수 있을 것이다.


## Source Code
### Java
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        HashMap<String, ArrayList<String>> list = new HashMap<>();
        HashMap<String, Integer> indegree = new HashMap<>();
        int n = Integer.parseInt(br.readLine());

        for (int i = 0 ; i < n ; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            String start = st.nextToken();
            String end = st.nextToken();

            list.putIfAbsent(start, new ArrayList<>());
            list.putIfAbsent(end, new ArrayList<>());
            list.get(start).add(end);

            indegree.putIfAbsent(end, 0);
            indegree.putIfAbsent(start, 0);
            indegree.put(end, indegree.get(end) + 1);
        }

        topological_sort(list, indegree);
    }
    static void topological_sort(HashMap<String, ArrayList<String>> list, HashMap<String, Integer> indegree) {
        Queue<Node> q = new LinkedList<>();
        PriorityQueue<Node> result = new PriorityQueue<>();

        for (String k : indegree.keySet()) {
            int v = indegree.get(k);
            if (v == 0) q.offer(new Node(k, 0));
        }

        while (!q.isEmpty()) {
            Node cur = q.poll();
            result.offer(cur);

            for (String item : list.get(cur.value)) {
                indegree.put(item, indegree.get(item) - 1);
                if (indegree.get(item) == 0) {
                    q.offer(new Node(item, cur.priority + 1));
                }
            }
        }

        if (result.size() != list.size()) {
            System.out.println(-1);
            return;
        }

        StringBuilder sb = new StringBuilder();
        while (!result.isEmpty()) {
            sb.append(result.poll().value + " ");
        }
        System.out.println(sb.toString());
    }
}
class Node implements Comparable<Node>{
    String value;
    int priority;
    Node(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }
    @Override
    public int compareTo(Node other) {
        if (this.priority == other.priority) {
            return this.value.compareTo(other.value);
        }
        return this.priority - other.priority;
    }

}
```
### Kotlin
```kotlin
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
```
