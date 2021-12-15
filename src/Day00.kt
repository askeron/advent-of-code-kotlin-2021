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
    assertEquals(0, part1(testInput))
    println(part1(input))
    assertEquals(0, part2(testInput))
    println(part2(input))
}
