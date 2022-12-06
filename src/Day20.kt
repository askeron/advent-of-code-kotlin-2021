fun main() {
    fun part1(input: Pair<List<Boolean>, Set<Point>>): Int {
        return input.second
            .enhanced(input.first, 1)
            .enhanced(input.first, 2)
            .size
    }

    fun part2(input: Pair<List<Boolean>, Set<Point>>): Int {
        var image = input.second
        (1..50).forEach { image = image.enhanced(input.first, it) }
        return image.size
    }

    fun parseInput(input: List<String>) = input[0].toCharArray().map { it == '#' } to input.drop(2).charMatrixToActivePoints()

    val testInput = parseInput(readInput("Day20_test"))
    val input = parseInput(readInput("Day20"))
    assertEquals(35, part1(testInput))
    println(part1(input))
    assertEquals(3351, part2(testInput))
    println(part2(input))
}

fun List<String>.charMatrixToActivePoints() = flatMapIndexed { x, s ->
    s.toCharArray().mapIndexed { y, c -> if (c == '#') Point(x,y) else null }
}.filterNotNull().toSet()

fun Set<Point>.getArea(additionalBorder: Int = 0): Set<Point> = ((minOf { it.x } - additionalBorder)..(maxOf { it.x } + additionalBorder)).flatMap { x ->
    ((minOf { it.y } - additionalBorder)..(maxOf { it.y } + additionalBorder)).map { Point(x, it) }
}.toSet()

fun Set<Point>.enhanced(enhanceIndex: List<Boolean>, step: Int): Set<Point> {
    val points = this.toMutableSet()
    if (step % 2 == 0 && enhanceIndex[0]) {
        // background is lit
        points += getArea(3).minus(getArea())
    }
    return getArea(2).filter { point ->
        enhanceIndex[point.neighboursWithItself.joinToString("") { if (points.contains(it)) "1" else "0" }.toInt(2)]
    }.toSet()
}
