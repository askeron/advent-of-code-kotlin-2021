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