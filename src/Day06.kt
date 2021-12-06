fun main() {
    fun part1(input: List<String>): Int {
        var state = input[0].split(",").map { it.toInt() }
        (1..80).forEach { day ->
            state = state.map { it - 1 }
            state = state.plus(state.filter { it < 0 }.map { 8 })
            state = state.map { if (it < 0) 6 else it }
            println("After ${day.toString().padStart(2)} days: ${state.joinToString(",")}")
        }
        return state.count()
    }

    fun part2(input: List<String>): Long {
        val initialPreState = input[0].split(",").map { it.toInt() }.groupingBy { it }.eachCount()
        val state = (0..8).map { i -> initialPreState[i] ?: 0}.map { it.toLong() }.toMutableList()
        (1..256).forEach { _ ->
            state += state[0]
            state[7] = state[7] + state[0]
            state.removeFirst()
        }
        return state.sum()
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")
    check(part1(testInput) == 5934)
    println(part1(input))
    check(part2(testInput) == 26984457539)
    println(part2(input))
}
