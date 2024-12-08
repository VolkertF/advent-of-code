package `2024`

import kotlin.math.abs

class Day08 {
    companion object {
        fun part01(day06part01: List<List<Char>>): Int =
            findAntinodes(day06part01).flatten().distinct().count()



        fun part02(day06part02: List<List<Char>>): Int {
            getStations(day06part02).also { println(it) }
            return findAntinodes(day06part02).flatten().distinct().count()
        }

    }
}

fun findAntinodes(grid: List<List<Char>>) =
    getStations(grid)
        .map { stationEntry->
            val bounds = Pair(grid.size, grid[0].size)
            findAntinodesForStation(stationEntry.value,bounds ).filter { position-> !isOutOfBounds(position, bounds) }
        }

private fun getStations(grid: List<List<Char>>) =
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


fun findAntinodesForStation(stations: List<Position>, bounds: Pair<Int,Int>): List<Position> =
    stations.flatMapIndexed { index, current ->
        findAntinodesForPosition(current, stations.minusIndex(index))
    }


fun findAntinodesForPosition(position: Position, candidates: List<Position>): List<Position> =
    candidates.flatMap { candidate ->
        val yDistance = abs(candidate.first - position.first)
        val xDistance = abs(candidate.second - position.second)
        val towardsPosX =
            if (candidate.first >= position.first) position.first - yDistance else position.first + yDistance
        val towardsPosY =
            if (candidate.second >= position.second) position.second - xDistance else position.second + xDistance
        val towardsCandidateX =
            if (candidate.first >= position.first) candidate.first + yDistance else candidate.first - yDistance
        val towardsCandidateY =
            if (candidate.second >= position.second) candidate.second + xDistance else candidate.second - xDistance
        val pos1 = Pair(towardsPosX, towardsPosY)
        val pos2 = Pair(towardsCandidateX, towardsCandidateY)
        listOf(pos1, pos2)
    }

fun isOutOfBounds(pair: Position, bounds:Pair<Int,Int>): Boolean {
    return pair.first < 0 || pair.first >= bounds.first || pair.second >= bounds.second || pair.second < 0
}
