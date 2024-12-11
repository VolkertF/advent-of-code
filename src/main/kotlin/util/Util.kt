package util

import java.io.BufferedReader
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

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
    bufferedReaderTo2DStringList(getResourceBufferedReader(path)).map {line-> line.map {char-> char.digitToInt() } }

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

fun <E> List<List<E>>.isInBounds(row:Int,column:Int):Boolean = this.isNotEmpty() && row >= 0 && row < this.size && column < this[0].size && column >= 0
