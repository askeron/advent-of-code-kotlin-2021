import java.io.File
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

fun main() {
    fun createDay(day: Int) {
        val dayString = day.toString().padStart(2, '0')
        File("src/Day$dayString.kt").writeText(
            File("src/Day00.kt").readText().replace("Day00", "Day$dayString")
        )
        File("src/Day$dayString.txt").writeText("")
        File("src/Day${dayString}_test.txt").writeText("")
    }

    assert(LocalDateTime.now().month == Month.DECEMBER)
    val lastDay = File("src").list()
        ?.mapNotNull { Regex("Day(\\d{2}).kt").find(it)?.groupValues?.get(1)?.toInt() }
        ?.maxOrNull()!!
    (lastDay+1..LocalDateTime.now().dayOfMonth).filter { it <= 24 }.forEach { createDay(it) }
}
