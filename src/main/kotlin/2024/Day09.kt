package `2024`

import kotlin.math.floor

class Day09 {
    companion object {
        fun part01(day09part01: Sequence<Char>): Long =
            day09part01.toBlockRepresentation().toList().defrag().calcCheckSum()


        fun part02(day09part02: Sequence<Char>): Long {
            return day09part02.toBlockRepresentation().toList().defrag2()
        }
    }
}

fun List<Int>.calcCheckSum(): Long = this.mapIndexed { index, id -> index * id }.sumOf { it.toLong() }

fun List<Int?>.defrag(): List<Int> {
    val result = mutableListOf<Int>()
    foldIndexed(size) { index, indexBack, value ->
        if (indexBack > index) {
            if (value == null) {
                val newIndexBack = getPreviousNotNullIndex(indexBack)
                if (newIndexBack > index) {
                    result.add(this[newIndexBack]!!)
                    return@foldIndexed newIndexBack
                }
            } else {
                result.add(value)
                return@foldIndexed indexBack
            }
        }
        return result.toList()
    }
    return result.toList()
}

fun List<Int?>.defrag2(): Long {
    val result = mutableListOf<Pair<Int?, Int>>()
    foldIndexed(null as Int? to 0) { index, currentStreak, value ->
        if (currentStreak.first == value) {
            if (index == lastIndex) {
                result.add(currentStreak.first to currentStreak.second + 1)
            }
            currentStreak.first to currentStreak.second + 1
        } else {
            if (index == lastIndex) {
                result.add(value to 1)
            }
            result.add(currentStreak.first to currentStreak.second)
            value to 1
        }
    }
    result.removeAt(0)

    return groupDefrag(result.reversed()).reversed().fold(Pair(0, 0L)) { acc, value ->
        val number = value.first ?: 0
        val sumOf = (0 until value.second).sumOf { (it + acc.first) * number.toLong() }
        Pair(acc.first + value.second, acc.second + sumOf)
    }.second
}

tailrec fun groupDefrag(
    fileGroup: List<Pair<Int?, Int>>,
    currentIndex: Int = 0,
): List<Pair<Int?, Int>> {
    if (currentIndex >= fileGroup.size || currentIndex < 0) {
        return fileGroup
    } else {
        val currentElement = fileGroup[currentIndex]
        val firstFreeIndex = fileGroup.withIndex()
            .indexOfLast { it.value.first == null && it.value.second >= currentElement.second }
        if (firstFreeIndex > currentIndex) {
            val newList = fileGroup.toMutableList()
            newList[firstFreeIndex] = currentElement
            // Add up remaining nulls with previous nulls
            if (newList[firstFreeIndex - 1].first == null) {
                newList[firstFreeIndex - 1] = Pair(
                    null,
                    fileGroup[firstFreeIndex - 1].second + fileGroup[firstFreeIndex].second - currentElement.second
                )
            } else {
                // else calculate how many nulls are remaining if any and add them before
                val remainingNulls = fileGroup[firstFreeIndex].second - currentElement.second
                if (remainingNulls > 0) {
                    newList.add(firstFreeIndex, Pair(null, remainingNulls))
                }
            }
            newList[currentIndex] = Pair(null, currentElement.second)

            return groupDefrag(
                newList.toList(),
                newList.withIndex().firstOrNull { it.index >= currentIndex && it.value.first != null }?.index ?: -1
            )
        } else {
            return groupDefrag(
                fileGroup,
                fileGroup.withIndex().firstOrNull { it.index > currentIndex && it.value.first != null }?.index ?: -1
            )
        }
    }
}

fun <E> List<E>.getPreviousNotNullIndex(startIndex: Int): Int =
    subList(0, startIndex).indexOfLast { it != null }

fun Sequence<Char>.toBlockRepresentation(): Sequence<Int?> {
    var currentFileId = 0
    return mapIndexed { index, c ->
        val filePart = if (index % 2 == 0) currentFileId++ else null
        (0 until c.digitToInt()).map { filePart }
    }.flatten()
}