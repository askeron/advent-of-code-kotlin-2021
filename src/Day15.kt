import java.util.*

fun main() {
    fun common(input: Map<Point, Int>): Int {
        val allPoints = input.keys
        val edgesWithCosts = input.entries.flatMap { entry ->
            entry.key.neighboursNotDiagonal
                .filter { it in allPoints }
                .map { it to entry.key toTriple entry.value }
        }.toSet()
        val end = Point(allPoints.maxOf { it.x }, allPoints.maxOf { it.y })
        return shortestPathByDijkstra(edgesWithCosts, Point(0, 0), end)!!.second
    }

    fun part1(input: Map<Point, Int>): Int {
        return common(input)
    }

    fun part2(input: Map<Point, Int>): Int {
        return common(input.flatMap { entry ->
            buildList {
                (0..4).forEach { x ->
                    (0..4).forEach { y ->
                        add(Point(
                            x * (input.keys.maxOf { it.x } + 1) + entry.key.x,
                            y * (input.keys.maxOf { it.y } + 1) + entry.key.y
                        ) to (entry.value + x + y).let { if (it > 9) it - 9 else it })
                    }
                }
            }
        }.toMap().also { println(it.matrixString()) })
    }

    fun parseInput(input: List<String>) = input.IntMatrixToPointMap()

    val testInput = parseInput(readInput("Day15_test"))
    val input = parseInput(readInput("Day15"))
    assertEquals(40, part1(testInput))
    println(part1(input))
    assertEquals(315, part2(testInput))
    println(part2(input))
}

private fun <T> shortestPathByDijkstra(
    edgesWithCosts: Set<Triple<T, T, Int>>,
    start: T,
    end: T,
): Pair<List<T>, Int>? {
    val costsMap = mutableMapOf<T, Int>()
    val previousNodeMap = mutableMapOf<T, T>()
    val edgesMap = edgesWithCosts.groupBy({ it.first }) { it.second to it.third }
    val queue = LinkedList<T>()
    val processed = mutableSetOf<T>()

    costsMap[start] = 0
    queue += start

    while (queue.isNotEmpty()) {
        val from = queue.minByOrNull { costsMap[it] ?: Int.MAX_VALUE }!!
        queue.remove(from)
        processed += from
        val fromCosts = costsMap[from] ?: Int.MAX_VALUE
        edgesMap[from]?.forEach { edge ->
            val to = edge.first
            val toCosts = fromCosts + edge.second
            if ((costsMap[to] ?: Int.MAX_VALUE) > toCosts) {
                costsMap[to] = toCosts
                previousNodeMap[to] = from
            }
            if (to !in processed && to !in queue) {
                queue += to
            }
        }
    }

    val reversedPath = mutableListOf(end)
    while (reversedPath.last() != start) {
        reversedPath += previousNodeMap[reversedPath.last()] ?: return null
    }
    return reversedPath.toList().reversed() to costsMap[end]!!
}
