fun main() {
    fun part1(input: List<String>): Int {
        val (draws, boards) = parseInput(input)
        val drawn = mutableListOf<Int>()
        while (!boards.any { it.hasWon(drawn) }) {
            drawn += draws[drawn.size]
        }
        val winningBoards = boards.filter { it.hasWon(drawn) }
        check(winningBoards.size == 1)
        return winningBoards[0].unmarkedNumbers(drawn).sum() * drawn.last()
    }

    fun part2(input: List<String>): Int {
        val (draws, boards) = parseInput(input)
        val winningBoards = boards.filter { it.hasWon(draws) }
        val drawn = mutableListOf<Int>()
        while (!winningBoards.all { it.hasWon(drawn) }) {
            drawn += draws[drawn.size]
        }
        val lastWinningBoards = winningBoards.filter { !it.hasWon(drawn.dropLast(1)) }
        check(lastWinningBoards.size == 1)
        return lastWinningBoards[0].unmarkedNumbers(drawn).sum() * drawn.last()
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")
    check(part1(testInput) == 4512)
    println(part1(input))
    check(part2(testInput) == 1924)
    println(part2(input))
}

private fun parseInput(input: List<String>): Pair<List<Int>, List<Board>> {
    val draws = input[0].split(",").map { it.toInt() }
    val boards = input.drop(2).joinToString("\n").split("\n\n").map { Board(it) }
    return draws to boards
}

private class Board(input: String) {
    private val rows = input.split("\n").map { row -> row.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
    private val columns = rows.turnMatrix()
    private val rowsAndColumns = rows.plus(columns)
    fun hasWon(drawn: List<Int>) = rowsAndColumns.any { drawn.containsAll(it) }
    fun unmarkedNumbers(drawn: List<Int>) = rows.flatten().filter { it !in drawn }
}
