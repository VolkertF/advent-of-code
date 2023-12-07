object Day06 {
        fun part01(lines: List<String>): Int {
            val times = lines[0].split(":")[1].trim()
                .split("\\s+".toRegex())
                .map { it.toLong() }
            val distances = lines[1].split(":")[1].trim()
                .split("\\s+".toRegex())
                .map { it.toLong() }
            return reduceTimesToProductOfWinningOptions(times, distances)
        }


        fun part02(lines: List<String>): Int {
            val times = lines[0].split(":")[1].trim()
                .replace("\\s+".toRegex(), "")
                .toLong()
            val distances = lines[1].split(":")[1].trim()
                .replace("\\s+".toRegex(), "")
                .toLong()
            return reduceTimesToProductOfWinningOptions(listOf(times), listOf(distances))
        }
}

fun reduceTimesToProductOfWinningOptions(times: List<Long>, distances: List<Long>): Int =
    times.mapIndexed { index, maxTime ->
        0.rangeTo(maxTime)
            .count { buttonHoldingTime ->
                (maxTime - buttonHoldingTime) * buttonHoldingTime > distances[index]
            }
    }.reduce { acc, value -> acc * value }