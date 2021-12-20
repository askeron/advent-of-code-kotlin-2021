data class Point(val x: Int, val y: Int) {
    operator fun unaryMinus() = Point(-x, -y)
    operator fun plus(b: Point) = Point(x + b.x, y + b.y)
    operator fun minus(b: Point) = this + (-b)
    val values = listOf(x, y)

    val neighboursNotDiagonal by lazy { listOf(
        Point(-1,0),
        Point(1,0),
        Point(0,-1),
        Point(0,1),
    ).map { this + it } }

    val neighboursWithItself by lazy { listOf(
        Point(-1,-1),
        Point(-1,0),
        Point(-1,1),
        Point(0,-1),
        Point(0,0),
        Point(0,1),
        Point(1,-1),
        Point(1,0),
        Point(1,1),
    ).map { this + it } }

    val neighbours by lazy { neighboursWithItself.filterNot { it == this } }
}