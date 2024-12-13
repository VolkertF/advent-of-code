package `2024`

import util.Grid
import util.Position
import kotlin.math.abs

class Day08 {
    companion object {
        fun part01(day08part01: Grid<Char>): Int =
            findAntinodes(grid = day08part01, includeStations = false, limit = 1).flatten().distinct().count()


        fun part02(day08part02: Grid<Char>): Int {
            return findAntinodes(grid = day08part02, includeStations = true, limit = Integer.MAX_VALUE).flatten()
                .distinct().count()
        }

    }
}

fun findAntinodes(grid: Grid<Char>, includeStations: Boolean, limit: Int) =
    getStations(grid)
        .map { stationEntry ->
            val bounds = Pair(grid.size, grid[0].size)
            findAllAntinodesForStation(
                stations = stationEntry.value,
                bounds = bounds,
                includeStations = includeStations,
                limit = limit
            )
        }

private fun getStations(grid: Grid<Char>) =
    grid.foldIndexed(emptyMap<Char, List<Position>>()) { index, acc, line ->
        line.foldIndexed(acc) { index2, acc2, char ->
            if (char == '.') {
                acc2
            } else if (acc2.contains(char)) {
                val newPos = acc2.toMutableMap()
                newPos[char] = acc2[char]!!.plus(Pair(index, index2))
                newPos.toMap()
            } else {
                acc2.plus(char to listOf(Pair(index, index2)))
            }
        }
    }.filter { it.value.size > 1 }

fun findAllAntinodesForStation(
    stations: List<Position>,
    bounds: Pair<Int, Int>,
    includeStations: Boolean,
    limit: Int
): List<Position> =
    stations.flatMapIndexed { index, current ->
        findAllAntinodesForPosition(
            position = current,
            candidates = stations.minusIndex(index),
            bounds = bounds,
            includeStations = includeStations,
            limit = limit
        )
    }

fun findAllAntinodesForPosition(
    position: Position,
    candidates: List<Position>,
    bounds: Pair<Int, Int>,
    includeStations: Boolean,
    limit: Int
): List<Position> =
    candidates.flatMap { candidate ->
        listOf(
            findAntinodesAlongLine(fromPosition = candidate, towardsPosition = position, bounds = bounds, limit = limit, depth = 0),
            findAntinodesAlongLine(fromPosition = position, towardsPosition = candidate, bounds = bounds, limit = limit, depth = 0),
        ).flatten()
    }.let {
        if (includeStations) {
            it.plus(candidates).plus(position)
        } else {
            it
        }
    }

tailrec fun findAntinodesAlongLine(
    fromPosition: Position,
    towardsPosition: Position,
    bounds: Pair<Int, Int>,
    limit: Int,
    depth: Int,
    currentList: List<Position> = emptyList()
): List<Position> {
    val nextAntinode = calculateNextAntinode(fromPosition, towardsPosition)
    return if (depth >= limit || isOutOfBounds(nextAntinode, bounds)) {
        currentList
    } else {
        findAntinodesAlongLine(
            fromPosition = towardsPosition,
            towardsPosition = nextAntinode,
            bounds = bounds,
            depth = depth + 1,
            limit = limit,
            currentList = currentList.plus(nextAntinode),
        )
    }
}

fun calculateNextAntinode(
    fromPosition: Position,
    towardsPosition: Position
): Position {
    val yDistance = abs(fromPosition.first - towardsPosition.first)
    val xDistance = abs(fromPosition.second - towardsPosition.second)
    val newPosX =
        if (fromPosition.first >= towardsPosition.first) towardsPosition.first - yDistance else towardsPosition.first + yDistance
    val newPosY =
        if (fromPosition.second >= towardsPosition.second) towardsPosition.second - xDistance else towardsPosition.second + xDistance
    return Pair(newPosX, newPosY)
}

fun isOutOfBounds(pair: Position, bounds: Pair<Int, Int>): Boolean {
    return pair.first < 0 || pair.first >= bounds.first || pair.second >= bounds.second || pair.second < 0
}
