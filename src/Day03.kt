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

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")
    check(part1(testInput) == 198)
    println(part1(input))
    check(part2(testInput) == 230)
    println(part2(input))
}

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