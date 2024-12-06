package `2024`

import kotlin.math.abs

class Day01 {
    companion object {
        fun part01(day01part01: List<String>): Int {
            val (leftSide, rightSide) = splitToIntLists(day01part01)
            val leftSideSorted = leftSide.sorted()
            val rightSideSorted = rightSide.sorted()
            return leftSideSorted.mapIndexed() { index, it ->
                abs(it - rightSideSorted[index])
            }.sum()
        }

        fun part02(day01part01: List<String>): Int {
            val (leftSide, rightSide) = splitToIntLists(day01part01)
            val storedCounter = mutableMapOf<Int, Int>()
            return leftSide.sumOf { left ->
                if (storedCounter.containsKey(left)) {
                    storedCounter[left]!!
                } else {
                    val counted = rightSide.count { right -> left == right } * left
                    storedCounter[left] = counted
                    counted
                }
            }
        }
    }
}

fun splitToIntLists(day01part01: List<String>): Pair<List<Int>, List<Int>> {
    val splits = day01part01.map {
        val split = it.split("\\s+".toRegex())
        split[0] to split[1]
    }

    val leftSide = splits.map { it.first.toInt() }
    val rightSide = splits.map { it.second.toInt() }
    return Pair(leftSide, rightSide)
}