fun main() {
    data class Point(val x: Int, val y: Int) {
        operator fun plus(b: Point) = Point(x + b.x, y + b.y)
        fun getNeighbours() = listOf(
            Point(-1,0),
            Point(1,0),
            Point(0,-1),
            Point(0,1),
        ).map { this + it }
    }

    fun part1(valueMap: Map<Point, Int>): Int {
        val points = valueMap.keys
        val lowPoints = points.filter { point ->
            point.getNeighbours()
                .mapNotNull { valueMap[it] }
                .all { it > valueMap[point]!! }
        }
        return lowPoints.sumOf { valueMap[it]!! + 1 }
    }

    fun Map<Point, Int>.getRecursivelyHigherNeighboursBelow9(point: Point): Set<Point> =
        point.getNeighbours()
            .filter { (this[it] ?: -1) in (this[point]!!+1)..8 }
            .flatMap { this.getRecursivelyHigherNeighboursBelow9(it).plus(it) }
            .toSet()

    fun part2(valueMap: Map<Point, Int>): Int {
        val points = valueMap.keys
        val lowPoints = points.filter { point ->
            point.getNeighbours()
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
