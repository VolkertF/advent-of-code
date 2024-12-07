package `2024`

import kotlin.math.abs

class Day02 {
    companion object {
        fun part01(day02part01: List<String>): Int {

            return day02part01.count { reportString ->
                isSafeReport(reportString.toIntList())
            }
        }

        fun part02(day02part02: List<String>): Int {
            return day02part02.count { reportString ->
                isSafeReportWithTolerance(reportString.toIntList())
            }
        }
    }
}

fun String.toIntList(): List<Int> {
    return this.split("\\s+".toRegex()).map { it.toInt() }
}

fun isSafeReport(reports: List<Int>): Boolean = reports.windowed(2).all { window ->
    abs(window[0] - window[1]) in 1..3
}.and(isStrictlyAscending(reports).or(isStrictlyDescending(reports)))

fun <T> List<T>.minusIndex(removeIndex: Int): List<T> = this.filterIndexed { index, _ -> removeIndex != index }

fun isSafeReportWithTolerance(reports: List<Int>): Boolean =
    isSafeReport(reports).or(reports.indices.any { currentIndex -> isSafeReport(reports.minusIndex(currentIndex)) })


fun isStrictlyAscending(reports: List<Int>): Boolean = reports.windowed(2).all { window -> window[0] < window[1] }
fun isStrictlyDescending(reports: List<Int>): Boolean = reports.windowed(2).all { window -> window[1] < window[0] }