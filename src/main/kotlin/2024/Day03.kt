package `2024`

class Day03 {
    companion object {
        fun part01(day03part01: List<String>): Int = day03part01.sumOf { it ->
            mulRegex.findAll(it).sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
        }


        fun part02(day03part01: List<String>): Int = run {
            var enabled = true
            day03part01.flatMap { line ->
                mulDoDontRegex.findAll(line).map {
                    if (it.value.startsWith("mul(") && enabled) {
                        it.groupValues[1].toInt() * it.groupValues[2].toInt()
                    } else {
                        if (it.value == "do()") {
                            enabled = true
                        } else if (it.value == "don't()") {
                            enabled = false
                        }
                        0
                    }
                }
            }.sum()
        }
    }
}

// finds strings like "mul(12,123)"
val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()

// finds strings like "mul(12,123)", "do()" or "don't()"
val mulDoDontRegex = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)""".toRegex()