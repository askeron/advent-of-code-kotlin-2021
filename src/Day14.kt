fun main() {

    fun part1(input: Pair<List<Char>, Map<Pair<Char, Char>, Char>>): Int {
        val result = input.first.transform(10) { chars ->
            chars.windowed(2)
                .flatMap { listOf(it[0], input.second[it[0] to it[1]]!!) }
                .plus(chars.last())
        }
        return result.groupingBy { it }.eachCount().values.let { counts -> counts.maxOf { it } - counts.minOf { it } }
    }

    fun part2(input: Pair<List<Char>, Map<Pair<Char, Char>, Char>>): Long {
        val result = input.first.windowed(2).groupingBy { it }.eachCount()
            .map { it.key to it.value.toLong() }
            .transform(40) { list ->
                list.flatMap { entry ->
                    val (a, c) = entry.first
                    val b = input.second[a to c]!!
                    listOf(listOf(a,b) to entry.second, listOf(b,c) to entry.second)
                }.groupBy { it.first }.map { entry -> entry.key to entry.value.sumOf { it.second } }
            }
        return result.plus(listOf(input.first.last()) to 1L)
            .groupBy { it.first[0] }
            .map { entry -> entry.value.sumOf { it.second } }
            .let { counts -> counts.maxOf { it } - counts.minOf { it } }
    }

    fun parseInput(input: List<String>) = input[0].toCharArray().toList() to input.drop(2).map { it.toCharArray() }
        .associate { it[0] to it[1] to it[6]  }

    val testInput = parseInput(readInput("Day14_test"))
    val input = parseInput(readInput("Day14"))
    assertEquals(1588, part1(testInput))
    println(part1(input))
    assertEquals(2188189693529L, part2(testInput))
    println(part2(input))
}

private fun<T> T.transform(times: Int, transform: (T) -> T): T {
    var result = this
    repeat(times) {
        result = transform.invoke(result)
    }
    return result
}