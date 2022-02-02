[![썸네이루-001](https://user-images.githubusercontent.com/83625797/152107923-29fd2c8a-61ea-4246-8824-10ebc8888fdc.png)
](https://www.acmicpc.net/problem/2602)
[문제 풀러 가기!](https://www.acmicpc.net/problem/2602)

## 풀이
~~때려죽이고 싶은~~ DP문제입니다. 오전 내내 문제를 붙잡고있었는데도 불구하고 풀지 못하였으나 코드를 차분히 다시 적어보니 풀렸네요.

각설하고 DP문제는 점화식만 세우면 80퍼센트는 해결된겁니다.
처음에는 완전탐색을 해보았으나 50퍼정도의 진행률에서 시간초과가 나왔습니다. 범위를 보고 긴가민가 했지만 역시 안되더라구요.

그렇기에 무조건 DP로 풀게끔 유도된 문제입니다.
이 문제에 쓰인 점화식은 다음과 같습니다.

```kotlin
// 가독성을 위해 기준 돌다리는 천사 돌다리로 하겠습니다.
dp[A][시작 지점][목표 지점] = 
	if(Angel[시작 지점] == 목표단어[목표 인덱스]) {
    	// 만약 일치한다면 이전 돌에서 이번 목표지점까지 가는 경우의 수와,
        // 반대편 돌다리에서 이전 목표단어까지 도달하는 경우의 수를 더해주어 갱신해주어야 한다.
    	dp[A][시작 지점 - 1][목표 지점] + dp[D][시작 지점 - 1][목표 지점 - 1]
    } else {
    	// 만약 일치하지 않는다면 이전 경우의 수 정보만 그대로 갱신해주어야 함
        dp[A][시작 지점 - 1][목표 지점]
    }
```

## Source Code
```kotlin
package `다이나믹프로그래밍(DP)`.`2602번_돌다리_건너기`

const val D = 0 ; const val A = 1
fun main(args: Array<String>) = with(System.`in`.bufferedReader()){
    val targetStr = readLine()
    val devil = readLine()
    val angel = readLine()

    val n = devil.length
    val dp = Array(2) {Array(n) {Array(targetStr.length) {0}}}

    if (devil[0] == targetStr[0]) dp[D][0][0] = 1
    if (angel[0] == targetStr[0]) dp[A][0][0] = 1

    /**
     * 점화식은 특정 돌다리의 시작지점에서 목표지점까지 가는 경우의 수를 구함.
     * 점화식은 dp[돌다리정보][시작지점][목표지점] =
     *      돌다리[시작점] == 목표[인덱스] ? dp[돌다리정보][시작점 - 1][목표지점] + dp[돌다리정보 + 1 % 2][시작점 -1][목표지점 - 1] : dp[돌다리정보][시작점-1][목표지점]
     **/

    for (start in 1 until n) {
        dp[D][start][0] = if (devil[start] == targetStr[0]) {dp[D][start - 1][0] + 1} else {dp[D][start - 1][0]}
        for (end in 1 until targetStr.length) {
            dp[D][start][end] = if (devil[start] == targetStr[end]) {dp[D][start - 1][end] + dp[A][start - 1][end - 1]} else {dp[D][start - 1][end]}
        }
        dp[A][start][0] = if (angel[start] == targetStr[0]) {dp[A][start - 1][0] + 1} else {dp[A][start - 1][0]}
        for (end in 1 until targetStr.length) {
            dp[A][start][end] = if (angel[start] == targetStr[end]) {dp[A][start - 1][end] + dp[D][start - 1][end - 1]} else {dp[A][start - 1][end]}
        }
    }
    // 악마 다리부터 내딛는 경우와, 천사 다리부터 내딛는 경우를 더해주어야 함
    println(dp[D][n - 1][targetStr.length - 1] + dp[A][n - 1][targetStr.length - 1])

    /**
     * Mem Usage : 12228KB
     * Time : 92ms
     */
}
```
