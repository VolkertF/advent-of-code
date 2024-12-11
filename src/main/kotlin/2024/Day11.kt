package `2024`

class Day11 {
    companion object {
        fun part01(day11part01: List<String>): Long = day11part01.toStones().sumOf { stone -> blink(stone, 25) }

        fun part02(day11part02: List<String>): Long = day11part02.toStones().sumOf { stone -> blink(stone, 75) }
    }
}

fun List<String>.toStones() = first().split("\\s+".toRegex()).map { it.toLong() }

fun blink(
    stone: Long,
    times: Int = 1,
    remainingBlinks: Int = times,
    seen: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()
): Long {
    if (remainingBlinks == 0) {
        return 1
    } else {
        return seen.getOrPut(stone to remainingBlinks) {
            if (stone == 0L)
                blink(1L, times, remainingBlinks - 1, seen)
            else {
                val stoneString = stone.toString()
                if (stoneString.length % 2 == 0) {
                    val stone1 = stoneString.substring(0, stoneString.length / 2).toLong()
                    val stone2 = stoneString.substring(stoneString.length / 2).toLong()
                    blink(stone1, times, remainingBlinks - 1, seen) + blink(stone2, times, remainingBlinks - 1, seen)
                } else {
                    blink(stone * 2024, times, remainingBlinks - 1, seen)
                }
            }
        }
    }
}