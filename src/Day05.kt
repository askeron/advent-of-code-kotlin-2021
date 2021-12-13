import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { Line(it) }
            .filter { it.straight }
            .flatMap { it.points }
            .nonUnique()
            .size
    }

    fun part2(input: List<String>): Int {
        return input.map { Line(it) }
            .flatMap { it.points }
            .nonUnique()
            .size
    }

    val testInput = readInput("Day05_test")
    val input = readInput("Day05")
    check(part1(testInput) == 5)
    println(part1(input))
    check(part2(testInput) == 12)
    println(part2(input))
}

private class Line(input: String) {
    private val startAndEnd = input.split(" -> ")
        .map { point -> point.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
    private val start = startAndEnd[0]
    private val end = startAndEnd[1]
    val straight = start.x == end.x || start.y == end.y
    val points: List<Point> by lazy {
        if (start == end) {
            listOf(start)
        } else {
            val diff = end - start
            val smallestDiff = diff / diff.values.map { it.absoluteValue }.filter { it > 0 }.minOf { it }
            val points = mutableListOf(start)
            while (points.last() != end) {
                points += points.last() + smallestDiff
            }
            points.toList()
        }
    }
}

private operator fun Point.div(b: Int) = Point(x / b, y / b)

private fun <T> List<T>.nonUnique() = this.groupingBy { it }.eachCount().filter { it.value > 1 }