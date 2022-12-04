import kotlin.math.max

fun main() {
    class DeterministicDice {
        var rolled = 0
        fun next() = (rolled++ % 100) + 1
        fun nextSummed(times: Int) = (1..times).sumOf { next() }
    }

    class Player(number: Int, initialPosition: Int) {
        var position = initialPosition
        var score = 0
        fun move(steps: Int) {
            position += steps
            while (position > 10) position -= 10
            score += position
        }
    }

    fun part1(input: Pair<Int, Int>): Int {
        val dice = DeterministicDice()
        val players = mutableListOf(
            Player(1, input.first),
            Player(2, input.second),
        )
        while (true) {
            players[0].move(dice.nextSummed(3))
            val winner = players.firstOrNull { it.score >= 1000 }
            if (winner != null) {
                return players.first { it != winner }.score * dice.rolled
            }
            players.removeAt(0).also { players += it }
        }
    }

    class UniverseCounter {
        var player1Wins = 0L
        var player2Wins = 0L
        fun maxWins() = max(player1Wins, player2Wins)
    }

    val universeCounter = UniverseCounter()

    data class ImmutablePlayer(
        val position: Int,
        val score: Int,
    ) {
        fun getMoved(steps: Int): ImmutablePlayer {
            var newPosition = position + steps
            while (newPosition > 10) newPosition -= 10
            return ImmutablePlayer(position = newPosition, score = score + newPosition)
        }
    }

    data class State(
        val player1Next: Boolean,
        val player1: ImmutablePlayer,
        val player2: ImmutablePlayer,
    ) {
        val players = listOf(player1, player2)

        fun nextRound(diceRoll: Int) {
            val newState = if (player1Next) {
                copy(player1 = player1.getMoved(diceRoll), player1Next = !player1Next)
            } else {
                copy(player2 = player2.getMoved(diceRoll), player1Next = !player1Next)
            }
            if (!newState.isFinishedAndIncrementCounterIfNeeded()) {
                newState.nextRound(1)
                newState.nextRound(2)
                newState.nextRound(3)
            }
        }

        fun isFinishedAndIncrementCounterIfNeeded(): Boolean {
            if (player1.score >= 1000) {
                universeCounter.player1Wins++
                return true
            }
            if (player2.score >= 1000) {
                universeCounter.player2Wins++
                return false
            }
            return false
        }
    }


    fun part2(input: Pair<Int, Int>): Long {
        // while not using much memory, the solution is not nearly fast enough to get ever an example
        val state = State(
            true,
            ImmutablePlayer(input.first, 0),
            ImmutablePlayer(input.second, 0),
        )
        state.nextRound(1)
        state.nextRound(2)
        state.nextRound(3)
        return universeCounter.maxWins()
    }

    fun parseInput(input: List<String>) = input.map { it.split(' ').last().toInt() }.let { it[0] to it[1] }

    val testInput = parseInput(readInput("Day21_test"))
    val input = parseInput(readInput("Day21"))
    assertEquals(739785, part1(testInput))
    println(part1(input))
    assertEquals(444356092776315L, part2(testInput))
    println(part2(input))
}
