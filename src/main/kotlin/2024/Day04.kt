package `2024`

class Day04 {
    companion object {
        fun part01(day03part01: List<List<Char>>): Int =
            countLinesBidirectional(day03part01)
                .plus(countLinesBidirectional(rotate90(day03part01)))
                .plus(checkCross(day03part01))

        fun part02(day03part01: List<List<Char>>): Int = checkMASCross(day03part01)
    }
}

fun checkCross(grid: List<List<Char>>): Int = grid.windowed(4).sumOf { lines ->
    (0 until grid[0].size - 3).sumOf { xPos ->
        val x1 = listOf(lines[0][xPos], lines[1][xPos + 1], lines[2][xPos + 2], lines[3][xPos + 3])
        val x2 = listOf(lines[0][xPos + 3], lines[1][xPos + 2], lines[2][xPos + 1], lines[3][xPos])
        countLinesBidirectional(listOf(x1, x2))
    }
}

fun checkMASCross(grid: List<List<Char>>): Int = grid.windowed(3).sumOf { lines ->
    (0 until grid[0].size - 2).sumOf { xPos ->
        val x1 = listOf(lines[0][xPos], lines[1][xPos + 1], lines[2][xPos + 2])
        val x2 = listOf(lines[0][xPos + 2], lines[1][xPos + 1], lines[2][xPos])
        val masRegex = """MAS""".toRegex()
        if (checkLineBidirectional(x1, masRegex) && checkLineBidirectional(x2, masRegex)) {
            1 as Int
        } else {
            0 as Int
        }
    }
}

fun countLinesBidirectional(grid: List<List<Char>>): Int =
    grid.sumOf { line -> countLineBidirectional(line) }

fun countLineBidirectional(line: List<Char>, regex: Regex = xmasRegex): Int =
    findOccurrences(line, regex) + findOccurrences(line.asReversed(), regex)

fun checkLineBidirectional(line: List<Char>, regex: Regex = xmasRegex): Boolean =
    (findOccurrences(line, regex) + findOccurrences(line.asReversed(), regex)) >= 1


fun rotate90(grid: List<List<Char>>): List<List<Char>> {
    val columns = grid[0].size
    val rows = grid.size
    val resultList = mutableListOf<List<Char>>()
    for (column in 0 until columns) {
        val newRow = mutableListOf<Char>()
        for (row in 0 until rows) {
            newRow.add(grid[row][column])
        }
        resultList.add(newRow.toList())
    }
    return resultList.toList()
}

fun findOccurrences(grid: List<Char>, regex: Regex = xmasRegex): Int {
    return regex.findAll(grid.joinToString("")).count()
}

val xmasRegex = """XMAS""".toRegex()