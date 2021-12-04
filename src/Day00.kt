fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day00_test")
    val input = readInput("Day00")
    check(part1(testInput) == 1)
    println(part1(input))
    check(part2(testInput) == 1)
    println(part2(input))
}
