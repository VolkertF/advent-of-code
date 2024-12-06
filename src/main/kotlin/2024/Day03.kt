package `2024`

class Day03 {
    companion object {
        fun part01(day03part01: List<String>): Int = day03part01.sumOf { it ->
            mulRegex.findAll(it).sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
        }


        fun part02(day03part01: List<String>): Int {
            return 0
        }
    }
}

// finds strings like "mul(12,123)
val mulRegex = "mul\\((\\d+),(\\d+)\\)".toRegex()