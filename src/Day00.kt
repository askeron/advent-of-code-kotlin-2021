fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    fun parseInput(input: List<String>) = input

    val testInput = parseInput(readInput("Day00_test"))
    val input = parseInput(readInput("Day00"))
    check(part1(testInput) == 0)
    println(part1(input))
    check(part2(testInput) == 0)
    println(part2(input))
}
