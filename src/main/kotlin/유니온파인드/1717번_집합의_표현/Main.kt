package 유니온파인드.`1717번_집합의_표현`

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