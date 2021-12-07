import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val inputPositions = parseInput(input)
        return (inputPositions.minOf { it }..inputPositions.maxOf { it })
            .map { i -> inputPositions.sumOf { (it - i).absoluteValue } }
            .minOf { it }
    }

    fun part2(input: List<String>): Int {
        val inputPositions = parseInput(input)
        return (inputPositions.minOf { it }..inputPositions.maxOf { it })
            .map { i -> inputPositions.sumOf { (it - i).absoluteValue.gaussSum() } }
            .minOf { it }
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")
    check(part1(testInput) == 37)
    println(part1(input))
    check(part2(testInput) == 168)
    println(part2(input))
}

private fun parseInput(input: List<String>) = input[0].split(",").map { it.toInt() }

private fun Int.gaussSum() = (this * (this + 1)) / 2
