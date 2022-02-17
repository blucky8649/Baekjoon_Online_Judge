package 삼성SW역량테스트.`19238번_스타트택시`

import java.util.*

private lateinit var road: Array<IntArray>
private lateinit var passengerInfo: Array<Array<Passenger>>
private const val empty = -1 // 손님이 없음을 나타내는 상수
private val dy = arrayOf(-1, 0, 1, 0)
private val dx = arrayOf(0, 1, 0, -1)

fun main() {
    val (N, M, fuel) = readln().split(" ").map { it.toInt() }
    road = Array(N) { IntArray(N) }
    passengerInfo = Array(N) { Array(N) { Passenger(empty, empty, empty, empty) } }

    // 도로 상태 입력
    for (i in road.indices) {
        val st = StringTokenizer(readln())
        for (j in road.indices) {
            road[i][j] = st.nextToken().toInt()
        }
    }

    // 기사 정보 입력
    val (startR, startC) = readln().split(" ").map { it.toInt() -1 }
    val driver = Driver(startC, startR, fuel, 0)

    // 승객 정보 입력
    repeat(M) {
        val (startY, startX, arriveY, arriveX) = readln().split(" ").map { it.toInt() -1 }
        passengerInfo[startY][startX] = Passenger(startX, startY, arriveX, arriveY)
    }

    repeat(M) {
        // 1. 제일 먼저 태울 손님을 구한다.
        val firstPassenger = searchPassenger(driver)

        // 1-1. 가는 도중 연료를 다 썼거나, 벽으로 막혀 손님을 못 찾았을 때 실패
        if (driver.fuel <= 0 || firstPassenger.dst == -1) {
            println(-1)
            return
        }

        // 1-2. 기사 정보 최신화
        driver.also {
            it.x = firstPassenger.passenger.startX
            it.y = firstPassenger.passenger.startY
            it.fuel -= firstPassenger.dst
        }
        // 2. 손님을 목적지 까지 모셔다 드린다.
        val deliverDistance =  deliverPassenger(driver, firstPassenger.passenger)
        driver.fuel -= deliverDistance

        // 2-1. 목적지 까지 가는 거리가 fuel보다 많거나, 벽으로 막혀 모실 수 없는 경우엔 실패.
        if (driver.fuel < 0 || deliverDistance == -1) {
            println(-1)
            return
        }
        // 2-2. 기사 정보 최신화
        driver.also {
            it.x = firstPassenger.passenger.arriveX
            it.y = firstPassenger.passenger.arriveY
            it.fuel += deliverDistance * 2
        }
    }
    println(driver.fuel)
}
fun deliverPassenger(driver: Driver, passenger: Passenger): Int {
    val q: Queue<Driver> = LinkedList<Driver>()
    val isVisited = Array(road.size) { BooleanArray(road.size) }
    q.offer(driver)
    isVisited[driver.y][driver.x] = true

    while (!q.isEmpty()) {
        val cur = q.poll()

        // 도착 했으면 운행을 종료한다.
        if (cur.x == passenger.arriveX && cur.y == passenger.arriveY) {
            // 해당 손님 정보를 없앤다.
            passengerInfo[passenger.startY][passenger.startX] = Passenger(empty, empty, empty, empty)
            return cur.cnt
        }

        for (i in dy.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (inRange(nx, ny) && !isVisited[ny][nx] && road[ny][nx] != 1) {
                q.offer(Driver(nx, ny, cur.fuel, cur.cnt + 1))
                isVisited[ny][nx] = true
            }
        }
    }
    // 벽에 막혀 손님을 모실 수 없는 경우엔 -1 반환
    return empty
}
fun searchPassenger(driver: Driver): StartTexi {
    val firstPassenger = PriorityQueue<StartTexi>()
    val q: Queue<Driver> = LinkedList<Driver>()
    val isVisited = Array(road.size) { BooleanArray(road.size) }
    q.offer(driver)
    isVisited[driver.y][driver.x] = true

    // 만약 시작 위치에 손님이 존재한다면 그 손님을 태운다.
    if (isNotPassengerEmpty(driver.x, driver.y)) {
        return StartTexi(0, passengerInfo[driver.y][driver.x])
    }

    while (!q.isEmpty()) {
        val cur = q.poll()

        if (isNotPassengerEmpty(cur.x, cur.y)) {
            firstPassenger.offer(StartTexi(cur.cnt, passengerInfo[cur.y][cur.x]))
            continue
        }

        for (i in dy.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]
            if (inRange(nx, ny) && !isVisited[ny][nx] && road[ny][nx] != 1) {
                q.offer(Driver(nx, ny, cur.fuel, cur.cnt + 1))
                isVisited[ny][nx] = true
            }
        }
    }
    // 벽에 막혀 손님을 찾을 수 없는 경우엔 -1 반환
    return firstPassenger.poll() ?: StartTexi(empty, passengerInfo[0][0])
}

fun inRange(x: Int, y: Int): Boolean = (x in road.indices && y in road.indices)
fun isNotPassengerEmpty(x: Int, y: Int): Boolean = (passengerInfo[y][x].startY != empty)

data class Driver(var x: Int, var y: Int, var fuel: Int, var cnt: Int)
data class Passenger(val startX: Int, val startY: Int, val arriveX: Int, val arriveY: Int)
data class StartTexi(val dst: Int, val passenger: Passenger) : Comparable<StartTexi> {
    override fun compareTo(other: StartTexi): Int {
        if (this.dst == other.dst) {
            if (this.passenger.startY == other.passenger.startY) {
                return this.passenger.startX - other.passenger.startX
            }
            return this.passenger.startY - other.passenger.startY
        }
        return this.dst - other.dst
    }
}