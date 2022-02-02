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