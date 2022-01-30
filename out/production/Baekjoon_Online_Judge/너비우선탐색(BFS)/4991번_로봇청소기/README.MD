![ㅋㅋㅋㅋ](https://media.vlpt.us/images/blucky8649/post/79dd581d-a910-416e-96a0-7fcbfb48bee1/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(1).png)

[문제 풀러 가기!](https://www.acmicpc.net/problem/4991)
## 문제
오늘은 직사각형 모양의 방을 로봇 청소기를 이용해 청소하려고 한다. 이 로봇 청소기는 유저가 직접 경로를 설정할 수 있다.

방은 크기가 1×1인 정사각형 칸으로 나누어져 있으며, 로봇 청소기의 크기도 1×1이다. 칸은 깨끗한 칸과 더러운 칸으로 나누어져 있으며, 로봇 청소기는 더러운 칸을 방문해서 깨끗한 칸으로 바꿀 수 있다.

일부 칸에는 가구가 놓여져 있고, 가구의 크기도 1×1이다. 로봇 청소기는 가구가 놓여진 칸으로 이동할 수 없다. 

로봇은 한 번 움직일 때, 인접한 칸으로 이동할 수 있다. 또, 로봇은 같은 칸을 여러 번 방문할 수 있다.

방의 정보가 주어졌을 때, 더러운 칸을 **`모두`** 깨끗한 칸으로 만드는데 필요한 이동 횟수의 최솟값을 구하는 프로그램을 작성하시오.

## 입력
입력은 여러 개의 테스트케이스로 이루어져 있다.
각 테스트 케이스의 첫째 줄에는 방의 가로 크기 w와 세로 크기 h가 주어진다. (1 ≤ w, h ≤ 20) 둘째 줄부터 h개의 줄에는 방의 정보가 주어진다. 방의 정보는 4가지 문자로만 이루어져 있으며, 각 문자의 의미는 다음과 같다.
>  - . : 깨끗한 칸
 - \* : 더러운 칸
 - x : 가구
 - o : 로봇 청소기의 시작 위치
 
더러운 칸의 개수는 10개를 넘지 않으며, 로봇 청소기의 개수는 항상 하나이다.

입력의 마지막 줄에는 0이 두 개 주어진다.


## 출력
> 각각의 테스트 케이스마다 더러운 칸을 모두 깨끗한 칸으로 바꾸는 이동 횟수의 최솟값을 한 줄에 하나씩 출력한다. 만약, 방문할 수 없는 더러운 칸이 존재하는 경우에는 -1을 출력한다.

#### 예제 입력 1
```
7 5
.......
.o...*.
.......
.*...*.
.......
15 13
.......x.......
...o...x....*..
.......x.......
.......x.......
.......x.......
...............
xxxxx.....xxxxx
...............
.......x.......
.......x.......
.......x.......
..*....x....*..
.......x.......
10 10
..........
..o.......
..........
..........
..........
.....xxxxx
.....x....
.....x.*..
.....x....
.....x....
0 0
```

#### 예제 출력 1
```
8
49
-1
```

## 풀이
전형적인 삼성 SW 역량 테스트 마지막문제스러운 문제다.
이 문제의 핵심은 다음과 같다.
> 
1. `BFS`를 이용하여 모든 노드(청소기 or 쓰레기) 사이를 최단 경로로 묶어서 인접 리스트로 작성한다.
2. 작성된 인접리스트를 기반으로 가능한 모든 경로를 `완전탐색` 한다.

1번 로직부터 천천히 코드를 보며 구현해보자.
```java
/** BFS로 모든 최단 경로에 대한 정보를 저장한다. **/
for (int start = 0 ; start < dust_idx - 1; start++) {
    for (int end = start + 1 ; end < dust_idx ; end++) {
        int weight = BFS(dusts[start], dusts[end], R, C, map);
        if (weight == -1) continue;
        // 양방향 노드
        adj_list[start].add(new Node2(end, weight));
        adj_list[end].add(new Node2(start, weight));
     }
}
```
위와 같은 로직으로 모든 경로에 대한 거리 정보를 저장한다. 구체적인 BFS코드의 경우 다음과 같다.

```java
static int BFS(Dot2 start, Dot2 end, int R, int C, char[][] map) {
    Queue<Dot2> q = new LinkedList<>();
    boolean[][] isVisited = new boolean[R][C];
    q.offer(new Dot2(start.x, start.y, 0));
    isVisited[start.y][start.x] = true;

    while (!q.isEmpty()) {
        Dot2 d = q.poll();

        if (d.y == end.y && d.x == end.x) {
            return d.cnt;
        }
        for (int i = 0 ; i < 4 ; i++) {
            int nx = d.x + dx[i];
            int ny = d.y + dy[i];
            if (nx < 0 || nx >= C || ny < 0 || ny >= R || isVisited[ny][nx] || map[ny][nx] == 'x') continue;
            q.offer(new Dot2(nx, ny, d.cnt + 1));
            isVisited[ny][nx] = true;
        }
    }
    return -1;
}
```
위 코드는 전형적인 Start Node 와 End Node 사이의 거리를 구하는 로직이다.

이제 인접 리스트가 작성 됐다면, 로봇 청소기가 이동하는 모든 경로에 대한 총 이동거리를 구해주고 이의 최솟값을 구해주기만 하면 된다. 다음 코드를 보자.

```java
static void Permutation(int start, int depth, ArrayList<Node2>[] adj_list, int sum, int dusts) {
    if (depth == dusts - 1) {
        answer = Math.min(answer, sum);
        return;
    }

    for (Node2 next : adj_list[start]) {
        if (check[next.end]) continue;
        check[next.end] = true;
        Permutation(next.end, depth + 1, adj_list, sum + next.weight, dusts);
        check[next.end] = false;
    }
}
```
먼지를 다 제거 했을 경우에만 정답의 대소비교를할 수 있다.
만약 제거할 수 없는 먼지가 하나라도 없다면 answer는 어떤 값을 하고 있을까? 당연히 초기값을 유지하고 있을 것이다.
따라서 만약 answer 가 초기값이라면 -1 출력, 아니라면 answer값을 출력하는 방식으로 정답을 출력하면 된다.

## Source code

### Java
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int[] dy = {-1, 0, 1, 0};
    static int[] dx = {0, 1, 0, -1};
    static int answer = Integer.MAX_VALUE;
    static boolean[] check;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            answer = Integer.MAX_VALUE;
            StringTokenizer st = new StringTokenizer(br.readLine());
            int C = Integer.parseInt(st.nextToken());
            int R = Integer.parseInt(st.nextToken());
            if (R + C == 0) break;

            char[][] map = new char[R][C];
            Dot[] dusts = new Dot[11];
            int dust_idx = 1;

            /** 배열에 데이터 삽입 **/
            for (int i = 0 ; i < R ; i++) {
                String str = br.readLine();
                for (int j = 0 ; j < C ; j++) {
                    map[i][j] = str.charAt(j);
                    if (map[i][j] == 'o') {
                        dusts[0] = new Dot(j, i);
                    }
                    else if (map[i][j] == '*'){
                        dusts[dust_idx++] = new Dot(j, i);
                    }
                }
            }
            /** 인접 리스트 선언 **/
            ArrayList<Node>[] adj_list = new ArrayList[dust_idx];
            for (int i = 0 ; i < dust_idx ; i++) {
                adj_list[i] = new ArrayList<Node>();
            }

            /** BFS로 모든 최단 경로에 대한 정보를 저장한다. **/
            for (int start = 0 ; start < dust_idx - 1; start++) {
                for (int end = start + 1 ; end < dust_idx ; end++) {
                    int weight = BFS(dusts[start], dusts[end], R, C, map);
                    if (weight == -1) continue;
                    // 양방향 노드
                    adj_list[start].add(new Node(end, weight));
                    adj_list[end].add(new Node(start, weight));
                }
            }
            /** 백트래킹을 이용하여 모든 경로를 탐색하여 최솟값을 출력한다. **/
            check = new boolean[dust_idx];
            check[0] = true;
            Permutation(0, 0, adj_list, 0, dust_idx);
            System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
        }
    }
    static void Permutation(int start, int depth, ArrayList<Node>[] adj_list, int sum, int dusts) {
        if (depth == dusts - 1) {
            answer = Math.min(answer, sum);
            return;
        }

        for (Node next : adj_list[start]) {
            if (check[next.end]) continue;
            check[next.end] = true;
            Permutation(next.end, depth + 1, adj_list, sum + next.weight, dusts);
            check[next.end] = false;
        }
    }
    static int BFS(Dot start, Dot end, int R, int C, char[][] map) {
        Queue<Dot> q = new LinkedList<>();
        boolean[][] isVisited = new boolean[R][C];
        q.offer(new Dot(start.x, start.y, 0));
        isVisited[start.y][start.x] = true;

        while (!q.isEmpty()) {
            Dot d = q.poll();

            if (d.y == end.y && d.x == end.x) {
                return d.cnt;
            }
            for (int i = 0 ; i < 4 ; i++) {
                int nx = d.x + dx[i];
                int ny = d.y + dy[i];
                if (nx < 0 || nx >= C || ny < 0 || ny >= R || isVisited[ny][nx] || map[ny][nx] == 'x') continue;
                q.offer(new Dot(nx, ny, d.cnt + 1));
                isVisited[ny][nx] = true;
            }
        }
        return -1;
    }
}
class Dot {
    int x;
    int y;
    int cnt;
    Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }
    Dot(int x, int y, int cnt) {
        this.x = x;
        this.y = y;
        this.cnt = cnt;
    }
}
class Node {
    int end;
    int weight;
    Node(int end, int weight) {
        this.end = end;
        this.weight = weight;
    }
}
```
### Kotlin
```kotlin
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

```

