package `2024`

import util.Position
import util.isInBounds

class Day10 {
    companion object {
        fun part01(day10part01: List<List<Int>>): Int = day10part01.findAllTrailheads().map { it.value.toSet().count() }.sum()


        fun part02(day10part02: List<List<Int>>): Int = day10part02.findAllTrailheads().map { it.value.count() }.sum()
    }
}

fun List<List<Int>>.findAllTrailheads(): Map<Position, List<Position>> =
    foldIndexed(emptyMap()) { rowIndex, lineAcc, line ->
        line.foldIndexed(lineAcc) { columnIndex, acc, height ->
            if (height == 0) {
                acc.plus(
                    Pair(rowIndex, columnIndex) to findTrailsForPosition(
                        this,
                        rowIndex,
                        columnIndex,
                    ).map { it.last() }
                )
            } else {
                acc
            }
        }
    }

fun findTrailsForPosition(
    grid: List<List<Int>>,
    rowIndex: Int,
    columnIndex: Int,
    result: List<List<Position>> = emptyList(),
    currentPath: List<Position> = listOf(rowIndex to columnIndex),
    expectedValue: Int = 0
): List<List<Position>> =
    // if out of bounds or invalid slope the result is empty
    if (grid.isInBounds(rowIndex, columnIndex)|| grid[rowIndex][columnIndex] != expectedValue) {
        emptyList()
    }
    // we reached the end of trial
    else if (grid[rowIndex][columnIndex] == 9) {
        result.plusElement(currentPath)
    } else {
        // add potential trails of 4 map directions to result
        result.plus(listOf((0 to -1), (-1 to 0), (1 to 0), (0 to 1)).flatMap { offset ->
            val newPath = currentPath + (rowIndex + offset.first to columnIndex + offset.second)
            // look up new direction with updated current path
            findTrailsForPosition(
                grid,
                rowIndex + offset.first,
                columnIndex + offset.second,
                result,
                newPath,
                expectedValue + 1
            )
        })
    }


