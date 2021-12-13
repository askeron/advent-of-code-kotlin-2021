fun main() {
    fun part1(input: Matrix): Int {
        var state = State(input)
        repeat(100) {
            state = state.next()
        }
        return state.flashCount
    }

    fun part2(input: Matrix): Int {
        var state = State(input)
        var i = 0
        while (!state.allHaveFlashed) {
            state = state.next()
            i++
        }
        return i
    }

    fun parseInput(input: List<String>) = input.flatMapIndexed { x, s ->
        s.toCharArray().mapIndexed { y, c -> Point(x,y) to c.toString().toInt() }
    }.toMap()

    val testInput = parseInput(readInput("Day11_test"))
    val input = parseInput(readInput("Day11"))
    val part1 = part1(testInput)
    check(part1 == 1656)
    println(part1(input))
    check(part2(testInput) == 195)
    println(part2(input))
}

typealias Matrix = Map<Point, Int>

data class State(val matrix: Matrix, val flashCount: Int = 0, val allHaveFlashed: Boolean = false)

private fun State.next(): State {
    val newMatrix = matrix.entries.associate { it.key to it.value + 1 }.toMutableMap()
    val allPoints = matrix.keys
    val flashed = mutableSetOf<Point>()

    do {
        val toFlash = allPoints.filter { newMatrix[it]!! > 9 }
            .minus(flashed)
        toFlash.flatMap { it.getNeighbours() }
            .filter { it in allPoints }
            .forEach { newMatrix[it] = newMatrix[it]!! + 1 }
        flashed += toFlash
    } while (toFlash.isNotEmpty())

    flashed.forEach { newMatrix[it] = 0 }

    return State(newMatrix.toMap(), flashCount + flashed.size, flashed.containsAll(allPoints))
}

private fun Point.getNeighbours() = listOf(
    Point(-1,-1),
    Point(-1,0),
    Point(-1,1),
    Point(0,-1),
    Point(0,1),
    Point(1,-1),
    Point(1,0),
    Point(1,1),
).map { this + it }
