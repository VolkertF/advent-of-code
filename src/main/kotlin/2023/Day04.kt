package `2023`

import kotlin.math.pow

object Day04 {
        fun part01(lines: List<String>):Int = getListOfWinningCards(lines)
            .sumOf { if (it.size >= 2) 2.0.pow(it.size - 1.0) else it.size.toDouble() }.toInt()

        fun part02(lines: List<String>):Int  {
            val multiplierArray: Array<Int> = Array(size = lines.size) { 1 }
            getListOfWinningCards(lines).asSequence().map { it.size }
                .mapIndexed { index, amountOfWinningCards ->
                    val multiplier = multiplierArray[index]
                    repeat(amountOfWinningCards) {
                        val lookedAtIndex = index + it + 1
                        if (lookedAtIndex < multiplierArray.size) {
                            val futureIndexMultiplier = multiplierArray[lookedAtIndex]
                            val newFutureMultiplier =  futureIndexMultiplier + multiplier
                            multiplierArray[lookedAtIndex] = newFutureMultiplier
                        }
                    }
                }.joinToString()
            return multiplierArray.sum()
        }
}

fun getListOfWinningCards(lines: List<String>) = lines.map {
    val split = it.split("|", limit = 2)
    val winningCards =
        split.first().trim()
            .split(":", limit = 2)[1].trim()
            .split("\\s+".toRegex())
            .map { word -> word.toInt() }
    val myCards = split[1].trim()
        .split("\\s+".toRegex())
        .map { word -> word.toInt() }
    myCards.filter { myCard -> winningCards.contains(myCard) }
}