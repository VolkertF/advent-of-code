package util

import java.io.BufferedReader
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

typealias Position = Pair<Int, Int>
typealias Grid = List<List<Char>>
enum class Direction2D { UP, DOWN, LEFT, RIGHT }


fun getResourceBufferedReader(path: String): BufferedReader? =
    {}.javaClass.getResourceAsStream("/$path")?.bufferedReader()

fun bufferedReaderToStringList(reader: BufferedReader?): List<String> = reader?.lines()?.toList() ?: emptyList()
fun bufferedReaderTo2DStringList(reader: BufferedReader?): List<List<Char>> =
    reader?.lines()?.toList()?.map { it.toCharArray().toList() } ?: emptyList()

fun resourceToStringList(path: String): List<String> = bufferedReaderToStringList(getResourceBufferedReader(path))
fun resourceToCharSequence(path: String): Sequence<Char> =
    getResourceBufferedReader(path)?.readText()?.asSequence() ?: emptySequence()

fun resourceTo2DCharList(path: String): List<List<Char>> =
    bufferedReaderTo2DStringList(getResourceBufferedReader(path))

fun resourceTo2DIntList(path: String): List<List<Int>> =
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

fun <E> List<List<E>>.isInBounds(row: Int, column: Int): Boolean =
    this.isNotEmpty() && row >= 0 && row < this.size && column < this[0].size && column >= 0

fun <E> List<List<E>>.getStraightNeighbours(x: Int, y: Int): List<E> =
    listOf((0 to -1), (-1 to 0), (1 to 0), (0 to 1)).mapNotNull {
        val newX = x + it.first
        val newY = y + it.second
        if (isInBounds(newX, newY)) {
            this[newX][newY]
        } else {
            null
        }
    }

fun <E> List<List<E>>.getStraightNeighboursWithPosition(x: Int, y: Int): Map<Position,E> =
    listOf((0 to -1), (-1 to 0), (1 to 0), (0 to 1)).mapNotNull {
        val newX = x + it.first
        val newY = y + it.second
        if (isInBounds(newX, newY)) {
            Pair(newX,newY) to this[newX][newY]
        } else {
            null
        }
    }.toMap()

fun <E> List<List<E>>.getAllNeighbours(x: Int, y: Int): List<E> =
    (0..1).flatMap { xOffset ->
        (0..1).mapNotNull { yOffset ->
            val newX = x + xOffset
            val newY = y + yOffset
            if (newX != 0 && newY != 0 && isInBounds(newX, newY)) {
                this[newX][newY]
            } else {
                null
            }
        }
    }

