class Day03 {
    companion object {

        fun part01(field: Engine): Int =
            extractNumberWindows(field).flatten()
                .map { findAdjacentSymbols(field, it) }.sumOf { it.chars.toInt() }

        fun part02(field: Engine): Int =
            extractNumberWindows(field).flatten()
                .map { findAdjacentSymbols(field, it) }.filter { it.isAdjacentToGear() }.groupBy { it.adjacentSymbol }
                .filter { it.value.size == 2 }.values.map { groupedWindows -> groupedWindows.map { window -> window.chars.toInt() } }
                .sumOf { it.reduce { acc, i -> acc * i } }
    }
}

typealias Engine = List<List<Char>>

data class NumberWindow(
    val chars: String = "",
    val indices: Pair<Int, Int>,
    val yIndex: Int,
    val adjacentSymbol: Triple<Char, Int, Int>? = null
)

fun extractNumberWindows(field: Engine): List<List<NumberWindow>> = field.mapIndexed { yIndex, line ->
    val result: MutableList<NumberWindow> = mutableListOf()
    var currentNumberWindow: NumberWindow? = null
    for (index in line.indices) {
        val isDigit = line[index].isDigit()
        if (isDigit && currentNumberWindow == null) {
            currentNumberWindow =
                NumberWindow(chars = line[index].toString(), yIndex = yIndex, indices = index to index)
        } else if (isDigit && currentNumberWindow != null) {
            currentNumberWindow = currentNumberWindow.copy(
                chars = currentNumberWindow.chars.plus(line[index]),
                indices = currentNumberWindow.indices.first to index
            )
        } else if (currentNumberWindow != null) {
            result.add(currentNumberWindow)
            currentNumberWindow = null
        }
    }
    if (currentNumberWindow != null) {
        result.add(currentNumberWindow)
    }
    result
}

fun findAdjacentSymbols(field: Engine, window: NumberWindow): NumberWindow {
    val fromY = window.yIndex - 1
    val fromX = window.indices.first - 1
    val toY = window.yIndex + 1
    val toX = window.indices.second + 1
    (fromY..toY).forEach outer@{ currentY ->
        (fromX..toX).forEach inner@{ currentX ->
            val currentChar = field.getOrElse(currentY) { return@outer }.getOrElse(currentX) { return@inner }
            if (currentChar.isSymbol()) {
                return window.copy(adjacentSymbol = Triple(currentChar, currentX, currentY))
            }
        }
    }
    return window
}

fun Char.isSymbol(): Boolean = !isDigit() && this != '.'
fun Char.isGear(): Boolean = this == '*'
fun NumberWindow.isAdjacentToGear(): Boolean = this.adjacentSymbol?.first?.isGear() ?: false