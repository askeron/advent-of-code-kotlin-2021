fun main() {
    fun part1(valueMap: Map<Point, Int>): Int {
        val points = valueMap.keys
        val lowPoints = points.filter { point ->
            point.neighboursNotDiagonal
                .mapNotNull { valueMap[it] }
                .all { it > valueMap[point]!! }
        }
        return lowPoints.sumOf { valueMap[it]!! + 1 }
    }

    fun Map<Point, Int>.getRecursivelyHigherNeighboursBelow9(point: Point): Set<Point> =
        point.neighboursNotDiagonal
            .filter { (this[it] ?: -1) in (this[point]!!+1)..8 }
            .flatMap { this.getRecursivelyHigherNeighboursBelow9(it).plus(it) }
            .toSet()

    fun part2(valueMap: Map<Point, Int>): Int {
        val points = valueMap.keys
        val lowPoints = points.filter { point ->
            point.neighboursNotDiagonal
                .mapNotNull { valueMap[it] }
                .all { it > valueMap[point]!! }
        }
        val basins = lowPoints.map { valueMap.getRecursivelyHigherNeighboursBelow9(it).plus(it) }
        return basins
            .map { it.size }
            .sortedByDescending { it }
            .let { it[0] * it[1] * it[2] }
    }

    fun parseInput(input: List<String>) = input.map { x -> x.toCharArray().toList().map { it.toString().toInt() } }
        .flatMapIndexed { x, list -> list.mapIndexed { y, value -> Point(x, y) to value } }.toMap()

    val testInput = parseInput(readInput("Day09_test"))
    val input = parseInput(readInput("Day09"))
    check(part1(testInput) == 15)
    println(part1(input))
    check(part2(testInput) == 1134)
    println(part2(input))
}
