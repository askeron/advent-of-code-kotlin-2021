fun main() {
    fun common(input: Map<Point, Int>): Long {
        val allPoints = input.keys
        val edgesWithCosts = input.entries.flatMap { entry ->
            entry.key.neighboursNotDiagonal
                .filter { it in allPoints }
                .map { it to entry.key toTriple entry.value.toLong() }
        }.toSet()
        val end = Point(allPoints.maxOf { it.x }, allPoints.maxOf { it.y })
        return shortestPathByDijkstra(edgesWithCosts, Point(0, 0), end).second
    }

    fun part1(input: Map<Point, Int>): Long {
        return common(input)
    }

    fun part2(input: Map<Point, Int>): Long {
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
    assertEquals(40L, part1(testInput))
    println(part1(input))
    assertEquals(315L, part2(testInput))
    //println(part2(input))
}

private fun <T> shortestPathByDijkstra(
    edgesWithCosts: Set<Triple<T, T, Long>>,
    start: T,
    end: T,
): Pair<List<T>, Long> {
    data class Node(val id: T, var costs: Long = Long.MAX_VALUE, var previousNode: T? = null)
    val nodeMap = edgesWithCosts.flatMap { listOf(it.first, it.second) }.associateWith { Node(it) }
    val edgesMap = edgesWithCosts.groupBy({ it.first }) { it.second to it.third }
    val queue = nodeMap.keys.toMutableSet()

    nodeMap[start]!!.costs = 0

    while (queue.isNotEmpty()) {
        val from = queue.minByOrNull { nodeMap[it]!!.costs }!!
        queue.remove(from)
        val fromCosts = nodeMap[from]!!.costs
        edgesMap[from]!!.forEach { edge ->
            val to = edge.first
            val toNode = nodeMap[to]!!
            val toCosts = fromCosts + edge.second
            if (toNode.costs > toCosts) {
                toNode.costs = toCosts
                toNode.previousNode = from
            }
        }
    }

    val reversedPath = mutableListOf(end)
    while (reversedPath.last() != start) {
        reversedPath += nodeMap[reversedPath.last()]!!.previousNode!!
    }
    return reversedPath.toList().reversed() to nodeMap[end]!!.costs
}

private fun Map<Point, Int>.matrixString(): String {
    return (0..keys.maxOf { it.x }).joinToString("\n") { x ->
        (0..keys.maxOf { it.y }).joinToString("") { y ->
            this[Point(x,y)]!!.toString()
        }
    }
}
