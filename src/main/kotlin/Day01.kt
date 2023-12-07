
object Day01 {
        fun part01(lines: List<String>): Int = lines.sumOf { line ->
            findDigitsForLine(line, digits)
        }


        fun part02(lines: List<String>): Int = lines.sumOf { line ->
            findDigitsForLine(line,digits+words)
        }
}

val words = mapOf(
    "zero" to 0,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)
val digits = mapOf("0" to 0, "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)

fun findDigitsForLine(input: String, prefix: Map<String, Int>): Int =
    input.mapIndexedNotNull { index, _ ->
        prefix.entries.find { (word, _) ->
            input.substring(index).startsWith(word)
        }?.value
    }
        .let { it.first() * 10 + it.last() }