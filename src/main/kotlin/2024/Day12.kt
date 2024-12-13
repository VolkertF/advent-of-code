package `2024`

import util.*

class Day12 {
    companion object {
        fun part01(day12part01: Grid<Char>): Int =
            day12part01.findFarmPlots().buildFences().sumOf { it.first * it.second }

        fun part02(day12part02: List<String>): Long = 0
    }
}

fun Grid<Char>.findFarmPlots(): List<Set<Position>> =
    foldIndexed(emptyList()) { rowIndex, allPlotsWrapper, line ->
        line.foldIndexed(allPlotsWrapper) { columnIndex, allPlots, char ->
            if (allPlots.all { !it.contains(rowIndex to columnIndex) }) {
                val plot = determinePlot(char, rowIndex to columnIndex, this, allPlots)
                allPlots.plusElement(plot)
            } else {
                allPlots
            }
        }
    }

// TODO make tailrec by passing list of positions to visit and working through it

fun determinePlot(
    plotChar: Char,
    currentPosition: Position,
    farm: Grid<Char>,
    knownPlots: List<Set<Position>>,
    currentPlot: MutableSet<Position> = mutableSetOf(),
): Set<Position> {
    // If out of bounds, not what we are looking for or already a known plot item this is useless
    if (!farm.isInBounds(
            currentPosition.first,
            currentPosition.second
        ) || farm[currentPosition.first][currentPosition.second] != plotChar || knownPlots.any { plot ->
            plot.contains(
                currentPosition.first to currentPosition.second
            )
        }
    ) {
        return emptySet()
    }
    val unvisitedNeighbours = farm.getValidStraightNeighbours(currentPosition.first, currentPosition.second)
        .filter { neighbour ->
            knownPlots.none { plot ->
                plot.contains(neighbour.key)
            } && !currentPlot.contains(neighbour.key)
        }
    return if (unvisitedNeighbours.isEmpty()) {
        currentPlot + (currentPosition.first to currentPosition.second)
    } else {
        currentPlot.add(currentPosition.first to currentPosition.second)
        currentPlot.addAll(unvisitedNeighbours.flatMap {
            determinePlot(
                plotChar,
                it.key,
                farm,
                knownPlots,
                currentPlot,
            )
        })
        currentPlot
    }
}


fun List<Set<Position>>.buildFences(): List<Pair<Int, Int>> = map {plot->
    plot.count() to plot.fold( 0){fenceCount,currentLand ->
        neighbourPositions2D(currentLand.first, currentLand.second).count { !plot.contains(it) } + fenceCount
    }
}