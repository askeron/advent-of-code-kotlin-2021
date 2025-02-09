fun main() {
    fun part1(input: Matrix): Int {
        var matrix = input
        var flashCount = 0
        repeat(100) {
            matrix = matrix.next()
            flashCount += matrix.count { it.value == 0 }
        }
        return flashCount
    }

    fun part2(input: Matrix): Int {
        var matrix = input
        var steps = 0
        while (!matrix.values.all { it == 0 }) {
            matrix = matrix.next()
            steps++
        }
        return steps
    }

    fun parseInput(input: List<String>) = input.IntMatrixToPointMap()

    val testInput = parseInput(readInput("Day11_test"))
    val input = parseInput(readInput("Day11"))
    val part1 = part1(testInput)
    check(part1 == 1656)
    println(part1(input))
    check(part2(testInput) == 195)
    println(part2(input))
}

typealias Matrix = Map<Point, Int>

private fun Matrix.next(): Matrix {
    val newMatrix = entries.associate { it.key to it.value + 1 }.toMutableMap()
    val allPoints = keys
    val flashed = mutableSetOf<Point>()

    do {
        val toFlash = allPoints.filter { newMatrix[it]!! > 9 && it !in flashed }
        toFlash.flatMap { it.neighbours }
            .filter { it in allPoints }
            .forEach { newMatrix[it] = newMatrix[it]!! + 1 }
        flashed += toFlash
    } while (toFlash.isNotEmpty())

    flashed.forEach { newMatrix[it] = 0 }

    return newMatrix.toMap()
}
