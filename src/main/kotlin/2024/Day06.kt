package `2024`

import kotlinx.coroutines.*
import util.Direction2D
import util.Grid
import util.Position


class Day06 {
    companion object {
        fun part01(day06part01: Grid): Int {
            val guard = getGuard(day06part01)
            return moveInDirection(
                day06part01,
                guard.position,
                guard.facingDirection,
            ).distinct().size
        }

        fun part02(day06part02: Grid): Int {
            val guard = getGuard(day06part02)
            val possibleObstacleLocations = moveInDirection(
                day06part02,
                guard.position,
                guard.facingDirection,
            ).distinct().minus(guard.position)
            return calculateAllLoopingPaths(guard, possibleObstacleLocations, day06part02)
        }
    }
}

fun calculateAllLoopingPaths(guard: Guard, possibleObstacleLocations: List<Position>, grid: Grid) =
    runBlocking {
        withContext(Dispatchers.IO) {
            possibleObstacleLocations.map {
                async {
                    // Update the current possible location in the grid by making it an obstacle...
                    val updatedGrid = grid.toMutableList()
                    val updatedColumn = updatedGrid[it.first].toMutableList()
                    updatedColumn[it.second] = '#'
                    updatedGrid[it.first] = updatedColumn
                    // ...and run the simulation
                    moveInDirection(
                        updatedGrid,
                        guard.position,
                        guard.facingDirection,
                    ) == emptyList<Position>()
                }

            }.awaitAll().count { it }
        }
    }


fun directionFromChar(char: Char): Direction2D? = when (char) {
    '^' -> Direction2D.UP
    'v' -> Direction2D.DOWN
    '<' -> Direction2D.LEFT
    '>' -> Direction2D.RIGHT
    else -> null
}

fun Direction2D.turnRight(): Direction2D = when (this) {
    Direction2D.UP -> Direction2D.RIGHT
    Direction2D.RIGHT -> Direction2D.DOWN
    Direction2D.DOWN -> Direction2D.LEFT
    Direction2D.LEFT -> Direction2D.UP
}


data class Guard(val position: Position, val facingDirection: Direction2D)

fun getGuard(grid: Grid): Guard {
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, position ->
            val direction = directionFromChar(position)
            if (direction != null) {
                return Guard(Position(rowIndex, columnIndex), direction)
            }
        }
    }
    return Guard(Position(0, 0), Direction2D.UP)
}

fun Position.moveUp(): Position = Pair(this.first - 1, this.second)
fun Position.moveDown(): Position = Pair(this.first + 1, this.second)
fun Position.moveLeft(): Position = Pair(this.first, this.second - 1)
fun Position.moveRight(): Position = Pair(this.first, this.second + 1)
fun Position.moveInDirection(direction: Direction2D): Position = when (direction) {
    Direction2D.UP -> this.moveUp()
    Direction2D.DOWN -> this.moveDown()
    Direction2D.LEFT -> this.moveLeft()
    Direction2D.RIGHT -> this.moveRight()
}

fun Position.isOutOfBounds(grid: Grid): Boolean =
    this.first >= grid.size || this.first < 0 || this.second >= grid[0].size || this.second < 0

fun Position.isObstacle(grid: Grid): Boolean = grid[this.first][this.second] == '#'

fun hasSeenObstacle(seenObstacles: List<Obstacle>, obstacle: Obstacle): Boolean = seenObstacles.contains(obstacle)

tailrec fun moveUntilDone(
    grid: Grid,
    startingPos: Position,
    direction: Direction2D,
    visited: List<Position> = emptyList(),
    seenObstacles: List<Obstacle> = emptyList()
): List<Position> {
    val newPos = startingPos.moveInDirection(direction)
    return if (newPos.isOutOfBounds(grid)) {
        visited.plus(startingPos)
    } else if (newPos.isObstacle(grid)) {
        val obstacle = Obstacle(newPos, direction)
        if (hasSeenObstacle(seenObstacles, obstacle)) {
            return emptyList()
        }
        moveUntilDone(grid, startingPos, direction.turnRight(), visited.plus(startingPos), seenObstacles.plus(obstacle))
    } else {
        moveUntilDone(grid, newPos, direction, visited.plus(startingPos), seenObstacles)
    }
}

data class Obstacle(val position: Position, val direction: Direction2D)

fun moveInDirection(
    grid: Grid,
    startingPos: Position,
    direction: Direction2D,
): List<Position> {
    return moveUntilDone(grid, startingPos, direction)
}