fun main() {
    fun part1(input: Input): Int {
        return input.points
            .fold(input.folds.take(1))
            .size
    }

    fun part2(input: Input): String {
        return input.points.fold(input.folds).mirror().matrixString()
    }

    fun parseInput(input: List<String>): Input {
        val points = input.takeWhile { it.isNotEmpty() }
            .map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
            .toSet()
        val folds = input.takeLastWhile { it.isNotEmpty() }
            .map { it.removePrefix("fold along ").split("=").let { Axis.valueOf(it[0].uppercase()) to it[1].toInt() } }
        return Input(points, folds)
    }

    val testInput = parseInput(readInput("Day13_test"))
    val input = parseInput(readInput("Day13"))
    assertEquals(17, part1(testInput))
    println(part1(input))
    assertEquals("""#####
#...#
#...#
#...#
#####""", part2(testInput))
    println()
    println(part2(input))
}

private enum class Axis { X, Y }
private data class Input(val points: Set<Point>, val folds: List<Pair<Axis, Int>>)
private fun Int.fold(value: Int) = this - ((this - value) * 2)
private fun Set<Point>.fold(folds: List<Pair<Axis, Int>>): Set<Point> {
    var points = this
    folds.forEach { fold ->
        if (fold.first == Axis.Y) {
            points = points.mirror()
        }
        points = points.filter { it.x < fold.second }
            .plus(points.filter { it.x > fold.second }.map {
                Point(it.x.fold(fold.second), it.y)
            }).toSet()
        if (fold.first == Axis.Y) {
            points = points.mirror()
        }
    }
    return points
}
private fun Set<Point>.mirror() = map { Point(it.y, it.x) }.toSet()

private fun Iterable<Point>.matrixString(): String {
    return (0..this.maxOf { it.x }).joinToString("\n") { x ->
        (0..this.maxOf { it.y }).joinToString("") { y ->
            if (contains(Point(x, y))) "#" else "."
        }
    }
}
