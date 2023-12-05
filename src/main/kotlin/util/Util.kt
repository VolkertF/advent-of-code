package util

import java.io.BufferedReader

fun getResourceBufferedReader(path: String): BufferedReader? =
    {}.javaClass.getResourceAsStream("/$path")?.bufferedReader()
fun bufferedReaderToStringList(reader: BufferedReader?): List<String> = reader?.lines()?.toList() ?: emptyList()
fun bufferedReaderTo2DStringList(reader: BufferedReader?): List<List<Char>> =
    reader?.lines()?.toList()?.map { it.toCharArray().toList() } ?: emptyList()
fun resourceToStringList(path: String): List<String> = bufferedReaderToStringList(getResourceBufferedReader(path))
fun resourceTo2DStringList(path: String): List<List<Char>> =
    bufferedReaderTo2DStringList(getResourceBufferedReader(path))
