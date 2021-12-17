import kotlin.math.sign

fun main() {
    fun part1(targetArea: TargetArea): Pair<Point, Int> {
        return targetArea.possibleInitialVelocities()
            .filter { targetArea.isHit(it) }
            .maxByOrNull { it.y }!!
            .let { it to it.y.gaussSum() }
    }

    fun part2(targetArea: TargetArea): Int {
        return targetArea.possibleInitialVelocities()
            .count { targetArea.isHit(it) }
    }

    val testInput = TargetArea(20, 30,-10, -5)
    val input = TargetArea(135, 155,-102, -78)
    assertEquals(Point(6, 9) to 45, part1(testInput))
    println(part1(input))
    assertEquals(112, part2(testInput))
    println(part2(input))
}

private data class TargetArea(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    fun contains(point: Point) = point.x in minX..maxX && point.y in minY..maxY
    fun isOutOfRange(point: Point) = point.x > maxX || point.y < minY
    fun possibleInitialVelocities() = (0..maxX).flatMap { x -> (minY..200).map { Point(x, it) } }
}

private fun TargetArea.isHit(initialVelocity: Point): Boolean {
    var position = Point(0,0)
    var velocity = initialVelocity
    while (true) {
        position += velocity
        velocity = Point(velocity.x + (velocity.x.sign * -1), velocity.y - 1)
        if (contains(position)) return true
        if (isOutOfRange(position)) return false
    }
}