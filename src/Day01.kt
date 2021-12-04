fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
                .countIncreasing()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
                .windowed(3)
                .map { it.sum() }
                .countIncreasing()
    }

    val testInput = readInput("Day01_test")
    val input = readInput("Day01")
    check(part1(testInput) == 7)
    println(part1(input))
    check(part2(testInput) == 5)
    println(part2(input))
}

private fun List<Int>.countIncreasing() = this.zipWithNext()
        .filter { it.second > it.first }
        .size

/*
private fun <E> List<E>.zipWithNextTwo(): List<Triple<E,E,E>> {
    return this.zipWithNext().zip(this.drop(2)).map { it.first toTriple it.second }
}
*/
