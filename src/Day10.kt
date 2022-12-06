fun main() {
    fun getFirstIllegalChar(text: String): Result {
        val stack = mutableListOf<Char>()
        text.toCharArray().forEach { c ->
            if (c.isOpen()) {
                stack += c
            } else {
                if (stack.last() != c.invert()) {
                    return IllegalCharResult(c)
                }
                stack.removeLast()
            }
        }
        return Success(stack.toList())
    }

    fun part1(input: List<String>): Int {
        return input.map { getFirstIllegalChar(it) }
            .filterIsInstance<IllegalCharResult>()
            .sumOf { illegalCharToPoints[it.illegalChar]!! }
    }

    fun part2(input: List<String>): Long {
        return input.map { getFirstIllegalChar(it) }
            .filterIsInstance<Success>()
            .map { it.remainingStack }
            .map { stack ->
                stack.reversed().map { it.invert() }
                    .map { autocompleteCharToPoints[it]!! }
                    .fold(0L) { acc, i -> acc * 5 + i }
            }.middleScore()
    }

    fun parseInput(input: List<String>) = input

    val testInput = parseInput(readInput("Day10_test"))
    val input = parseInput(readInput("Day10"))
    check(part1(testInput) == 26397)
    println(part1(input))
    check(part2(testInput) == 288957L)
    println(part2(input))
}

sealed class Result
class IllegalCharResult(val illegalChar: Char) : Result()
class Success(val remainingStack: List<Char>) : Result()

private val openToClosed = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val illegalCharToPoints = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

private val autocompleteCharToPoints = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L,
)

private fun Char.invert() = openToClosed.plus(openToClosed.inverted())[this]!!
private fun Char.isOpen() = this in openToClosed.keys
private fun <T: Comparable<T>> List<T>.middleScore(): T = sorted().let { it[it.size / 2] }
