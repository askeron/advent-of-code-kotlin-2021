fun main() {

    fun common(
        input: List<Connection>,
        entryPredicate: (List<String>) -> Boolean
    ): Int {
        val connectionMap = input.plus(input.map { it.second to it.first })
            .filter { it.second != "start" }
            .distinct().groupBy({ it.first }) { it.second }
        return listOf(listOf("start"))
            .transformUntilNoChange { list ->
                list.filterNot { it.contains("end") }.flatMap { entry ->
                    (connectionMap[entry.last()] ?: emptyList()).map {
                        entry.plus(it)
                    }
                }.plus(list.filter { it.contains("end") }).filter(entryPredicate)
            }.size
    }

    fun part1(input: List<Connection>): Int {
        return common(input) { entry -> entry.filter { it.smallCave() }.allDistinct() }
    }

    fun part2(input: List<Connection>): Int {
        return common(input) { entry ->
            val counts = entry.filter { it.smallCave() }.groupingBy { it }.eachCount().values
            counts.none { it > 2 } && counts.count { it == 2 } <= 1
        }
    }

    fun parseInput(input: List<String>) = input.map { line -> line.split("-").let { it[0] to it[1] } }

    val testInput = parseInput(readInput("Day12_test"))
    val input = parseInput(readInput("Day12"))
    assertEquals(226, part1(testInput))
    println(part1(input))
    assertEquals(3509, part2(testInput))
    println(part2(input))
}

private fun String.smallCave() = lowercase() == this

private fun <T> List<T>.transformUntilNoChange(transform: (List<T>) -> List<T>) : List<T> =
    transform.invoke(this).let {
        if (it == this) {
            it
        } else {
            it.transformUntilNoChange(transform)
        }
    }

typealias Connection = Pair<String, String>
