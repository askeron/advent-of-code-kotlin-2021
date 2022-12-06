import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T> List<T>.allDistinct() = this.size == this.distinct().size

fun <T : Comparable<T>> List<T>.isSorted() = this == this.sorted()

fun <T> List<T>.singleValue(): T {
    assert(size == 1)
    return get(0)
}

infix fun <A, B, C> Pair<A, B>.toTriple(that: C): Triple<A, B, C> = Triple(first, second, that)

fun <T> assertEquals(expected: T, actual: T) {
    check(expected == actual) { "expected $expected but found $actual" }
}

fun <T> T.transform(times: Int, transform: (T) -> T): T {
    var result = this
    repeat(times) {
        result = transform.invoke(result)
    }
    return result
}

fun <T> List<T>.transformUntilNoChange(transform: (List<T>) -> List<T>) : List<T> =
    transform.invoke(this).let {
        if (it == this) {
            it
        } else {
            it.transformUntilNoChange(transform)
        }
    }

fun List<String>.IntMatrixToPointMap() = flatMapIndexed { x, s ->
    s.toCharArray().mapIndexed { y, c -> Point(x,y) to c.digitToInt() }
}.toMap()

fun Int.gaussSum() = (this * (this + 1)) / 2

fun <T> List<T>.getAllDistinctCombinations(): List<List<T>> {
    var result = this.map { listOf(it) }
    repeat(this.size - 1) {
        result = result.zipInList(this).filter { it.allDistinct() }
    }
    return result
}

fun <T> List<List<T>>.zipInList(list: List<T>): List<List<T>> = this.flatMap { x -> list.map { x.plus(it) } }

fun List<Int>.toIntByDigits(): Int {
    assert(all { it in 0..9 })
    var result = 0
    forEach {
        result = result * 10 + it
    }
    return result
}

fun <T> List<T>.nonUnique() = this.groupingBy { it }.eachCount().filter { it.value > 1 }

fun <T> List<List<T>>.turnMatrix(): List<List<T>> = (0 until this[0].size).map { i -> this.map { it[i] } }

fun <K, V> Map<K, V>.inverted() = entries.associate{(k,v)-> v to k}

fun Iterable<Point>.mirror() = map { Point(it.y, it.x) }.toList()

fun Iterable<Point>.matrixString(pointChar: Char, noPointChar: Char): String =
    matrixString { x, y -> if (contains(Point(x, y))) pointChar else noPointChar }

fun Iterable<Point>.matrixString(charFunction: (Int, Int) -> Char): String {
    return (0..this.maxOf { it.x }).joinToString("\n") { x ->
        (0..this.maxOf { it.y }).joinToString("") { y ->
            charFunction.invoke(x, y).toString()
        }
    }
}

fun Map<Point, Int>.matrixString() = keys.matrixString { x, y -> this[Point(x,y)]!!.digitToChar()}
