fun main() {

    fun part1(input: List<String>): Int {
        val matrix = input.map { row -> row.toList().map { it.toString() != "0" } }
        val gamma = matrix.turnMatrix()
            .map { it.mostCommonElement() }
            .binaryToInt()
        val epsilon = matrix.turnMatrix()
            .map { it.leastCommonElement() }
            .binaryToInt()
        println("gamma $gamma epsilon $epsilon")
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val matrix = input.map { row -> row.toList().map { it.toString() != "0" } }
        val oxygenGeneratorRating = matrix.findFirstUnique { it.mostCommonElement(true) }.binaryToInt()
        val co2ScrubberRating = matrix.findFirstUnique { it.leastCommonElement(false) }.binaryToInt()
        println("oxygenGeneratorRating $oxygenGeneratorRating co2ScrubberRating $co2ScrubberRating")
        return oxygenGeneratorRating * co2ScrubberRating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun <T> List<List<T>>.turnMatrix(): List<List<T>> = (0 until this[0].size).map { i -> this.map { it[i] } }

private fun List<Boolean>.moreTrueThanFalseCount() = this.count { it } - this.count { !it }

private fun List<Boolean>.mostCommonElement(prefer: Boolean = true) = when(this.moreTrueThanFalseCount()) {
    in 1..Int.MAX_VALUE -> true
    0 -> prefer
    else -> false
}

private fun List<Boolean>.leastCommonElement(prefer: Boolean = false) = !this.mostCommonElement(!prefer)

private fun List<Boolean>.binaryToInt() = Integer.parseInt(this.map { if (it) "1" else "0" }.joinToString(""), 2)

private fun List<List<Boolean>>.findFirstUnique(columnCritieria: (List<Boolean>) -> Boolean): List<Boolean> {
    var i = 0
    var itemsLeft = this
    while (itemsLeft.size > 1) {
        val validColumnValue = columnCritieria.invoke(itemsLeft.turnMatrix()[i])
        itemsLeft = itemsLeft.filter { it[i] == validColumnValue }
        i++
    }
    return itemsLeft[0]
}