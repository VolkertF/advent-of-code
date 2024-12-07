package `2024`

import kotlinx.coroutines.*

typealias Position = Pair<Int, Int>
typealias Grid = List<List<Char>>

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

        fun part02(day06part01: Grid): Int {
            val guard = getGuard(day06part01)
            val possibleObstacleLocations = moveInDirection(
                day06part01,
                guard.position,
                guard.facingDirection,
            ).distinct().minus(guard.position)
            return calculateAllLoopingPaths(guard, possibleObstacleLocations, day06part01)
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


enum class Direction { UP, DOWN, LEFT, RIGHT }

fun directionFromChar(char: Char): Direction? = when (char) {
    '^' -> Direction.UP
    'v' -> Direction.DOWN
    '<' -> Direction.LEFT
    '>' -> Direction.RIGHT
    else -> null
}

fun Direction.turnRight(): Direction = when (this) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
}


data class Guard(val position: Position, val facingDirection: Direction)

fun getGuard(grid: Grid): Guard {
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, position ->
            val direction = directionFromChar(position)
            if (direction != null) {
                return Guard(Position(rowIndex, columnIndex), direction)
            }
        }
    }
    return Guard(Position(0, 0), Direction.UP)
}

fun Position.moveUp(): Position = Pair(this.first - 1, this.second)
fun Position.moveDown(): Position = Pair(this.first + 1, this.second)
fun Position.moveLeft(): Position = Pair(this.first, this.second - 1)
fun Position.moveRight(): Position = Pair(this.first, this.second + 1)
fun Position.moveInDirection(direction: Direction): Position = when (direction) {
    Direction.UP -> this.moveUp()
    Direction.DOWN -> this.moveDown()
    Direction.LEFT -> this.moveLeft()
    Direction.RIGHT -> this.moveRight()
}

fun Position.isOutOfBounds(grid: Grid): Boolean =
    this.first >= grid.size || this.first < 0 || this.second >= grid[0].size || this.second < 0

fun Position.isObstacle(grid: Grid): Boolean = grid[this.first][this.second] == '#'

fun hasSeenObstacle(seenObstacles: List<Obstacle>, obstacle: Obstacle): Boolean = seenObstacles.contains(obstacle)

tailrec fun moveUntilDone(
    grid: Grid,
    startingPos: Position,
    direction: Direction,
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

data class Obstacle(val position: Position, val direction: Direction)

fun moveInDirection(
    grid: Grid,
    startingPos: Position,
    direction: Direction,
): List<Position> {
    return moveUntilDone(grid, startingPos, direction)
}