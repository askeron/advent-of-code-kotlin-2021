fun main() {
    fun part1(input: List<String>): Int {
        val commands = input.map { line -> line.split(" ").let { it[0] to it[1].toInt() } }
        val horizontalPosition = commands.filter { it.first == "forward" }.sumOf { it.second }
        val depth = (commands.filter { it.first == "down" }.sumOf { it.second }
            - commands.filter { it.first == "up" }.sumOf { it.second })
        return horizontalPosition * depth
    }

    fun part2(input: List<String>): Int {
        val commands = input.map { line -> line.split(" ").let { it[0] to it[1].toInt() } }
        var horizontalPosition = 0
        var depth = 0
        var aim = 0
        commands.forEach { (command, value) ->
            when (command) {
                "down" -> aim += value
                "up" -> aim -= value
                "forward" -> {
                    horizontalPosition += value
                    depth += value * aim
                }
            }
        }
        return horizontalPosition * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
