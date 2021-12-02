fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
                .countIncreasing()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
                .zipWithNextTwo()
                .map { it.first + it.second + it.third }
                .countIncreasing()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

private fun List<Int>.countIncreasing() = this.zipWithNext()
        .map { it.second - it.first }
        .filter { it > 0 }
        .size

private fun <E> List<E>.zipWithNextTwo(): List<Triple<E,E,E>> {
    return this.zipWithNext().zip(this.drop(2)).map { it.first toTriple it.second }
}
