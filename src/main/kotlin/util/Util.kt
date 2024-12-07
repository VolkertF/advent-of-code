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
fun resourceTo2DCharList(path: String): List<List<Char>> =
    bufferedReaderTo2DStringList(getResourceBufferedReader(path))
fun getAverageExecutionTime(repeat: Int, fn: () -> Unit): Duration {
    var average = 0.0
    repeat(repeat) {
        val currentTime = System.nanoTime()
        fn()
        average += (((System.nanoTime() - currentTime) - average) / (it + 1.0))
    }
    return average.nanoseconds
}