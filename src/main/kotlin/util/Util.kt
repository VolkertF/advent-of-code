package util

import java.io.BufferedReader
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

typealias Position = Pair<Int, Int>
typealias Grid<E> = List<List<E>>

enum class Direction2D { UP, DOWN, LEFT, RIGHT }


fun getResourceBufferedReader(path: String): BufferedReader? =
    {}.javaClass.getResourceAsStream("/$path")?.bufferedReader()

fun bufferedReaderToStringList(reader: BufferedReader?): List<String> = reader?.lines()?.toList() ?: emptyList()
fun bufferedReaderTo2DStringList(reader: BufferedReader?): Grid<Char> =
    reader?.lines()?.toList()?.map { it.toCharArray().toList() } ?: emptyList()

fun resourceToStringList(path: String): List<String> = bufferedReaderToStringList(getResourceBufferedReader(path))
fun resourceToCharSequence(path: String): Sequence<Char> =
    getResourceBufferedReader(path)?.readText()?.asSequence() ?: emptySequence()

fun resourceTo2DCharList(path: String): Grid<Char> =
    bufferedReaderTo2DStringList(getResourceBufferedReader(path))

fun resourceTo2DIntList(path: String): Grid<Int> =
    bufferedReaderTo2DStringList(getResourceBufferedReader(path)).map { line -> line.map { char -> char.digitToInt() } }

fun getAverageExecutionTime(repeat: Int, fn: () -> Unit): Duration {
    var average = 0.0
    repeat(repeat) {
        val currentTime = System.nanoTime()
        fn()
        average += (((System.nanoTime() - currentTime) - average) / (it + 1.0))
    }
    return average.nanoseconds
}

fun printAverageExecutionTime(repeat: Int, fn: () -> Unit) = println(getAverageExecutionTime(repeat, fn))

fun <E> Grid<E>.isInBounds(row: Int, column: Int): Boolean =
    this.isNotEmpty() && row >= 0 && row < this.size && column < this[0].size && column >= 0

/** Returns all non-null horizontal and vertical neighbours of the given position. This filters out potential out-of-bounds values.*/
fun <E> Grid<E>.getValidStraightNeighbours(x: Int, y: Int): Map<Position, E> =
    getAllStraightNeighbours(x, y).filter { it.value != null }.map { it.key to it.value!! }
        .toMap()

fun neighbourPositions2D(x: Int, y: Int): List<Position> = listOf((0 to -1), (-1 to 0), (1 to 0), (0 to 1)).map {
    val newX = x + it.first
    val newY = y + it.second
    newX to newY
}

fun neighbourPositions(x: Int, y: Int): List<Position> = listOf((0 to -1), (-1 to 0), (1 to 0), (0 to 1)).map {
    val newX = x + it.first
    val newY = y + it.second
    newX to newY
}

/** Returns all horizontal and vertical neighbours of the given position. This includes potential out-of-bounds and null values*/
fun <E> Grid<E>.getAllStraightNeighbours(x: Int, y: Int): Map<Position, E?> =
    neighbourPositions2D(x, y).associate { position ->
        if (isInBounds(position.first, position.second)) {
            Pair(position.first, position.second) to this[position.first][position.second]
        } else {
            Pair(position.first, position.second) to null
        }
    }

/** Returns all horizontal and vertical neighbours of the given position. This includes potential out-of-bounds and null values*/
fun <E> Grid<E>.getAllValidNeighbours(x: Int, y: Int): Map<Position, E> =
    getAllNeighbours(x, y).filter { it.value != null }.map { it.key to it.value!! }.toMap()


/** Returns all horizontal and vertical neighbours of the given position. This includes potential out-of-bounds and null values*/
fun <E> Grid<E>.getAllNeighbours(x: Int, y: Int): Map<Position, E?> =
    neighbourPositions(x, y).associate { position ->
        if (position.first != 0 && position.second != 0 && isInBounds(position.first, position.second)) {
            Pair(position.first, position.second) to this[position.first][position.second]
        } else {
            Pair(position.first, position.second) to null
        }
    }

