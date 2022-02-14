package 삼성SW역량테스트.`15685번_드래곤_커브`

val yAxisDirectionInfo = arrayOf(0, -1, 0, 1) // 동, 북, 서, 남
val xAxisDirectionInfo = arrayOf(1, 0, -1, 0)
val dragonMatrix = Array(101) { BooleanArray(101) }
fun main() {
    val curveCount = readln().toInt()
    var answer = 0
    // 1 단계: 커브 만들기
    repeat(curveCount) {
        val(xAxis, yAxis, direction, generation) = readln().split(" ").map { it.toInt() }
        createCurve(xAxis, yAxis, direction, generation)
    }
    // 2 단계: 커브의 네 꼭지점이 존재하는지 검증하기
    for (yAxis in dragonMatrix.indices) {
        for (xAxis in dragonMatrix.indices) {
            if (!dragonMatrix[yAxis][xAxis] || !checkWhetherCurveHaveFourPointOfVertex(xAxis, yAxis)) continue
            answer ++
        }
    }
    println(answer)
}

fun checkWhetherCurveHaveFourPointOfVertex(xAxis: Int, yAxis: Int) : Boolean =
    inRange(xAxis, yAxis) && dragonMatrix[yAxis + 1][xAxis] && dragonMatrix[yAxis][xAxis + 1] && dragonMatrix[yAxis + 1][xAxis + 1]

fun inRange(xAxis: Int, yAxis: Int) = (xAxis in 0 until 100 && yAxis in 0 until 100)

 fun createCurve(xAxis: Int, yAxis: Int, direction: Int, generation: Int) {
    val curveToMove = ArrayList<Int>() // 이전 세대의 커브정보를 저장하기 위한

    curveToMove.add(direction)
    for (gen in 1 .. generation) {
        // 이전 세대 커브를 기점으로 시계 방향으로 회전
        // ex) 북 -> 동, 동 -> 남
        val curveSizeOfPreviousGeneration = curveToMove.size
        for (curveIndex in curveSizeOfPreviousGeneration - 1 downTo 0) {
            curveToMove.add((curveToMove[curveIndex] + 1) % 4) // Rotate Clockwise
        }
    }

    // 커브를 제작했다면, 이제 좌표상에 그려주어야 한다.
    drawCurveInMatrix(xAxis, yAxis, curveToMove)
}
fun drawCurveInMatrix(xAxis: Int, yAxis: Int, curveToMove: ArrayList<Int>){
    dragonMatrix[yAxis][xAxis] = true

    var xAxisMoveNext = xAxis
    var yAxisMoveNext = yAxis
    for (direction in curveToMove) {
        xAxisMoveNext += xAxisDirectionInfo[direction]
        yAxisMoveNext += yAxisDirectionInfo[direction]

        dragonMatrix[yAxisMoveNext][xAxisMoveNext] = true
    }
}
