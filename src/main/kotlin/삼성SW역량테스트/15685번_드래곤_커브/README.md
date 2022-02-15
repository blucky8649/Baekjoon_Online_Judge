![](https://user-images.githubusercontent.com/83625797/153788052-bd0bd2c6-9781-468f-9cc5-1e237f5385d6.png)
[문제 풀러 가기!](https://www.acmicpc.net/problem/15685)

## 풀이
아래와 같은 단계로 차분히 풀어나가신다면 어려움 없이 풀 수 있는 그런 문제였습니다.

>1. 입력 조건에 맞는 DragonCurve를 그린다.
>2. 꼭지점이 4군데 존재하는지 살펴본다.


### 1. 드래곤 커브 그리고, 좌표에 그려넣기
주어진 입력값을 활용하여 드래곤 커브를 그려봅시다.  
n세대의 드래곤 커브는 n-1세대의 드래곤 커브를 **시계 방향으로 회전**한 모양을 이어붙인 형태입니다.  
아래 그림을 보시면 **이전 세대에서 먼저 진입한 커브가 다음 세대에서는 제일 뒤에 오는 것**을 알 수 있습니다.

<div><img src="https://images.velog.io/images/blucky8649/post/018d5bef-4ea9-4a45-94d4-dbe78041f7ec/image.png"></div>

따라서 이전 세대의 커브 각각을 **시계방향으로 회전시킨 후 역순으로 이어 붙이시면 됩니다.  
** 필자는 `ArrayList`를 활용하여 커브를 생성해보았습니다.

```kotlin
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
```

커브 정보가 담긴 `ArrayList`가 완성이 되었다면 좌표상에 그대로 그려주기만 하면 됩니다.
```kotlin
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
```

### 2. 그려진 커브의 꼭짓점을 검증해야한다.
커브를 모두 그리셨으면 좌표를 탐색하여 4개의 꼭지점이 드래곤 커브의 일부인지 판별하고, 정답을 count 하시면 됩니다.
```kotlin
// 2 단계: 커브의 네 꼭지점이 존재하는지 검증하기
for (yAxis in dragonMatrix.indices) {
    for (xAxis in dragonMatrix.indices) {
        if (!dragonMatrix[yAxis][xAxis] || !checkWhetherCurveHaveFourPointOfVertex(xAxis, yAxis)) continue
        answer ++
    }
}
// 중략.. //
fun checkWhetherCurveHaveFourPointOfVertex(xAxis: Int, yAxis: Int) : Boolean =
    inRange(xAxis, yAxis) && dragonMatrix[yAxis + 1][xAxis] && dragonMatrix[yAxis][xAxis + 1] && dragonMatrix[yAxis + 1][xAxis + 1]

fun inRange(xAxis: Int, yAxis: Int) = (xAxis in 0 until 100 && yAxis in 0 until 100)
```


## SourceCode
```kotlin
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

```
