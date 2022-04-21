package 삼성SW역량테스트.`13458번_시험감독`

fun main() {
    val n = readln().toInt()
    val list = readln().split(" ").map { it.toInt() }.toMutableList()
    val (B, C) = readln().split(" ").map { it.toInt() }

    var answer: Long = n.toLong()
    list
        .filter { it - B > 0 }
        .forEach {
            answer += if ((it - B) % C == 0) (it - B) / C else (it - B) / C + 1
        }
    println(answer)
}
