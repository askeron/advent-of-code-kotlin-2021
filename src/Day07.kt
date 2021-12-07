import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int = calculateFuel(input) { it }

    fun part2(input: List<String>): Int = calculateFuel(input) { it.gaussSum() }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")
    check(part1(testInput) == 37)
    println(part1(input))
    check(part2(testInput) == 168)
    println(part2(input))
}

private fun calculateFuel(input: List<String>, fuelFunction: (Int) -> Int): Int {
    val inputPositions = input[0].split(",").map { it.toInt() }
    return (inputPositions.minOf { it }..inputPositions.maxOf { it })
        .map { i -> inputPositions.sumOf { fuelFunction((it - i).absoluteValue) } }
        .minOf { it }
}

private fun Int.gaussSum() = (this * (this + 1)) / 2
