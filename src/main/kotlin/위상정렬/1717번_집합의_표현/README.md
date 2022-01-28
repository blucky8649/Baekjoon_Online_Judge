![zz](https://media.vlpt.us/images/blucky8649/post/e9b1e063-7bdc-4989-ab9e-cbdcecda2902/%EC%8D%B8%EB%84%A4%EC%9D%B4%EB%A3%A8-001%20(2).png)

[문제 풀러 가기!](https://www.acmicpc.net/problem/1717)
## 문제
초기에 {0}, {1}, {2}, ... {n} 이 각각 n+1개의 집합을 이루고 있다. 여기에 합집합 연산과, 두 원소가 같은 집합에 포함되어 있는지를 확인하는 연산을 수행하려고 한다.

집합을 표현하는 프로그램을 작성하시오.

## 입력
> 첫째 줄에 n(1 ≤ n ≤ 1,000,000), m(1 ≤ m ≤ 100,000)이 주어진다. m은 입력으로 주어지는 연산의 개수이다. 다음 m개의 줄에는 각각의 연산이 주어진다. 합집합은 0 a b의 형태로 입력이 주어진다. 이는 a가 포함되어 있는 집합과, b가 포함되어 있는 집합을 합친다는 의미이다. 두 원소가 같은 집합에 포함되어 있는지를 확인하는 연산은 1 a b의 형태로 입력이 주어진다. 이는 a와 b가 같은 집합에 포함되어 있는지를 확인하는 연산이다. a와 b는 n 이하의 자연수 또는 0이며 같을 수도 있다.


## 출력
> 1로 시작하는 입력에 대해서 한 줄에 하나씩 YES/NO로 결과를 출력한다. (yes/no 를 출력해도 된다)

## 풀이
> **"자네 Union-Find(상호 배타적 집합)의 기본을 알고있나?"**

대표적인 **`유니온 파인드`** 문제입니다.
https://brenden.tistory.com/33
혹시 유니온 파인드에 대해 잘 모르신다면 위 블로그를 참조해 주세요.

그래도 간략하게 설명을 하자면 합집합을 찾을 때 쓰는 알고리즘입니다.

`Find` 메서드를 통해 부모노드를 찾고, 만약 부모가 같지 않다면 붙여주는 방식(`Union`)으로 합집합을 구현합니다.

특정한 두 수의 합집합 여부를 찾을때는 두 수가 같은 부모인지 알면 되겠죠?

유니온파인드의 기본도 없이 `최소 스패닝 트리`를 공부하다가 자연스럽게 얻어간 개념이었습니다. ~~**웰 - 노운 이네요.**~~

## Source code

### Java
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int[] parents;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        parents = new int[n + 1];
        for (int i = 1 ; i <= n ; i++) {
            parents[i] = i;
        }
        StringBuilder sb  = new StringBuilder();
        for (int i = 0 ; i < m ; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case 0 : union(a, b); break;
                case 1 : sb.append((isUnion(a, b) ? "YES" : "NO") + "\n"); break;
            }
        }
        System.out.print(sb);
    }
    static int find(int x) {
        if (parents[x] == x) return x;
        return parents[x] = find(parents[x]);
    }
    static void union(int a, int b) {
        int A = find(a);
        int B = find(b);

        if (A == B) return;
        parents[B] = A;
    }
    static boolean isUnion(int a, int b) {
        int A = find(a);
        int B = find(b);

        if (A == B) return true;
        return false;
    }
}

```

### Kotlin
```kotlin
import java.lang.StringBuilder

private lateinit var parent : Array<Int>
fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    val(n, m) = readLine().split(" ").map { it.toInt() }
    parent = Array(n + 1) {i -> i}
    val sb = StringBuilder()
    repeat(m) {
        val(cmd, a, b) = readLine().split(" ").map { it.toInt() }
        when(cmd) {
            0 -> union(a, b)
            1 -> if(isUnion(a, b)) {sb.append("YES \n")} else {sb.append("NO \n")}
        }
    }
    print(sb.toString())


}
fun find(x : Int) : Int {
    if (parent[x] == x) return x
    parent[x] = find(parent[x])
    return parent[x]
}
fun union(a : Int, b : Int) {
    val A = find(a)
    val B = find(b)

    if (A == B) return
    parent[B] = A
}
fun isUnion(a : Int, b : Int) : Boolean {
    val A = find(a)
    val B = find(b)

    if (A == B) return true
    return false
}
```
