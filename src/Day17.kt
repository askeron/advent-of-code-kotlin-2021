import kotlin.math.max
import kotlin.math.sign

fun main() {
    fun part1(targetArea: TargetArea): Pair<Point, Int> {
        return (0..2000).flatMap { x -> (targetArea.minY..2000).map { Point(x, it) } }
            .map { it to targetArea.isHitAndGetMaxHeight(it) }
            .filter { it.second.first }
            .maxByOrNull { it.second.second }!!
            .let { it.first to it.second.second }
    }

    fun part2(targetArea: TargetArea): Int {
        return (0..2000).flatMap { x -> (targetArea.minY..2000).map { Point(x, it) } }
            .map { it to targetArea.isHitAndGetMaxHeight(it) }
            .filter { it.second.first }
            .count()
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
    fun isOutOf(point: Point) = point.x > maxX || point.y < minY
}

private fun TargetArea.isHitAndGetMaxHeight(initialVelocity: Point): Pair<Boolean, Int> {
    var position = Point(0,0)
    var velocity = initialVelocity
    var maxHeight = 0
    while (true) {
        position += velocity
        maxHeight = max(position.y, maxHeight)
        velocity = Point(velocity.x + (velocity.x.sign * -1), velocity.y - 1)
        if (contains(position)) return true to maxHeight
        if (isOutOf(position)) return false to maxHeight
    }
}