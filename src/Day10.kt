fun main() {
    data class Result(val illegalChar: Char?, val remainingStack: List<Char>)
    fun getFirstIllegalChar(text: String): Result {
        val stack = mutableListOf<Char>()
        text.toCharArray().forEach { c ->
            if (c.isOpen()) {
                stack += c
            } else {
                if (stack.last() == c.invert()) {
                    stack.removeLast()
                } else {
                    return Result(c, stack)
                }
            }
        }
        return Result(null, stack.toList())
    }

    fun part1(input: List<String>): Int {
        return input.mapNotNull { getFirstIllegalChar(it).illegalChar }
            .sumOf { illegalCharToPoints[it]!! }
    }

    fun part2(input: List<String>): Long {
        val scores = input.map { getFirstIllegalChar(it) }
            .filter { it.illegalChar == null }
            .map { it.remainingStack }
            .map { stack ->
                stack.reversed().map { it.invert() }
            }
            .onEach { println(it.toString()) }
            .map { list ->
                list.map { autocompleteCharToPoints[it]!! }
                    .fold(0L) { acc, i -> acc * 5 + i }
            }
        return scores.middleScore()
    }

    fun parseInput(input: List<String>) = input

    val testInput = parseInput(readInput("Day10_test"))
    val input = parseInput(readInput("Day10"))
    check(part1(testInput) == 26397)
    println(part1(input))
    check(part2(testInput) == 288957L)
    println(part2(input))
}

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
private fun <K, V> Map<K, V>.inverted() = this.entries.associate{(k,v)-> v to k}
private fun <T: Comparable<T>> List<T>.middleScore(): T = this.sorted().let { it[it.size / 2] }
