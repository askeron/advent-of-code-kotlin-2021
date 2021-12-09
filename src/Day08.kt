fun main() {
    fun part1(input: List<String>): Int {
        return input.map { InputEntry(it) }
            .map { SegmentTransformation.getValid(it.uniquePatterns) to it.outputValues }
            .flatMap { (sf, outputValues) -> outputValues.map { sf.transform(it) }.map { segmentToDigit[it]!! } }
            .count { it in listOf(1, 4, 7, 8) }
    }

    fun part2(input: List<String>): Int {
        return input.map { InputEntry(it) }
            .map { SegmentTransformation.getValid(it.uniquePatterns) to it.outputValues }
            .sumOf { (sf, outputValues) -> outputValues.map { sf.transform(it) }.map { segmentToDigit[it]!! }.toIntByDigits() }
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")
    check(part1(testInput) == 26)
    println(part1(input))
    check(part2(testInput) == 61229)
    println(part2(input))
}

private val letters = ('a'..'g').toList()
private val digitSegments = listOf(
    "abcefg",
    "cf",
    "acdeg",
    "acdfg",
    "bcdf",
    "abdfg",
    "abdefg",
    "acf",
    "abcdefg",
    "abcdfg",
).map { Segment.of(it) }
private val segmentToDigit = digitSegments.mapIndexed { index, segment -> segment to index }.toMap()

private data class Segment(val value: List<Int>) {
    init {
        assert(value.allDistinct())
        assert(value.isSorted())
        assert(value.all { it in 0..6 })
    }

    companion object {
        fun of(s: String) = Segment(letters.withIndex().filter { s.contains(it.value) }.map { it.index})
    }
}

private class InputEntry(inputLine: String) {
    private val seperatedStrings = inputLine.split(" | ").map { it.split(" ") }
    val uniquePatterns = seperatedStrings[0].map { Segment.of(it) }
    val outputValues = seperatedStrings[1].map { Segment.of(it) }
}

private data class SegmentTransformation(val value: List<Int>) {
    private val map by lazy { value.mapIndexed { index, i -> index to i }.toMap() }

    init {
        assert(value.size == 7)
        assert(value.allDistinct())
        assert(value.all { it in 0..6 })
    }

    fun transform(segment: Segment) = Segment(segment.value.map { map[it]!! }.sorted())

    companion object {
        private val all = (0..6).toList().getAllDistinctCombinations().map { SegmentTransformation(it) }

        fun getValid(uniqueSegments: List<Segment>) = all.first { sf ->
            uniqueSegments.map { sf.transform(it) }.containsAll(digitSegments)
        }
    }
}

private fun <T> List<T>.getAllDistinctCombinations(): List<List<T>> {
    var result = this.map { listOf(it) }
    repeat(this.size - 1) {
        result = result.zipInList(this).filter { it.allDistinct() }
    }
    return result
}

private fun <T> List<List<T>>.zipInList(list: List<T>): List<List<T>> = this.flatMap { x -> list.map { x.plus(it) } }

private fun List<Int>.toIntByDigits(): Int {
    assert(all { it in 0..9 })
    var result = 0
    forEach {
        result = result * 10 + it
    }
    return result
}
