class Day02 {
    companion object {

        fun part01(lines: List<String>) =
            lines.map { line -> lineToGameData(line) }.filter { isValidGame(it.second) }.sumOf { validGame ->
                validGame.first
            }

        fun part02(lines: List<String>) =
            lines.map { line -> lineToGameData(line) }.sumOf { getPowerOfGame(it.second) }
    }
}

typealias CubeColor = String
typealias NumberOfCubes = Int
typealias GameSet = List<Pair<CubeColor, NumberOfCubes>>
typealias CubeEntry = Pair<CubeColor, NumberOfCubes>

fun lineToGameData(line: String): Pair<Int, List<GameSet>> {
    val split = line.split(":", limit = 2)
    val id = split.first().substringAfter("Game").trim().toInt()
    val gameSets = split.last().split(";").map { set ->
        set.split(",").map { cubeEntry ->
            val splitCubeEntry = cubeEntry.trim().split("\\s".toRegex())
            splitCubeEntry.last() to splitCubeEntry.first().toInt()
        }
    }
    return id to gameSets
}

// Part 01
val validCubes = mapOf("green" to 13, "blue" to 14, "red" to 12)

fun isValidGame(gameSets: List<GameSet>): Boolean =
    gameSets.all { gameSet -> isValidGameSet(gameSet) }


fun isValidGameSet(gameSet: List<CubeEntry>): Boolean =
    gameSet.all { cubeEntry -> isCubeEntryValid(cubeEntry) }


fun isCubeEntryValid(cubeEntry: CubeEntry): Boolean =
    validCubes.entries.any { validEntry ->
        validEntry.key == cubeEntry.first && validEntry.value >= cubeEntry.second
    }


// Part 02
fun convertToColorMapWithAllAmounts(gameSets: List<GameSet>) =
    gameSets.flatten().groupBy { cubeEntry -> cubeEntry.first }
        .mapValues { cubeEntries -> cubeEntries.value.map { entry -> entry.second } }


fun getPowerOfGame(game: List<GameSet>): Int =
    convertToColorMapWithAllAmounts(game).values.map { it.max() }.reduce { acc, int -> acc * int }
