package `2024`

typealias Operator = (Long, Long) -> Long

class Day07 {
    companion object {
        fun part01(day06part01: List<String>): Long =
            day06part01.map(::extractTestData)
                .sumOf { (goal, operands) ->
                    processTest(goal, operands, generatePermutations(operands.size - 1, listOf(Plus(), Multiply())))
                }

        fun part02(day06part02: List<String>): Long =
            day06part02.map(::extractTestData)
                .sumOf { (goal, operands) ->
                    processTest(
                        goal,
                        operands,
                        generatePermutations(operands.size - 1, listOf(Plus(), Multiply(), Merge()))
                    )
                }

    }
}

fun extractTestData(testString: String): Pair<Long, List<Long>> = testString.split(":")
    .let { splitString ->
        splitString.first().toLong() to splitString.last().trim().split("\\s+".toRegex()).map { it.toLong() }
    }

fun processTest(goal: Long, operands: List<Long>, allPermutations: List<List<Operation>>): Long =
    if (checkAllCalculations(goal, operands, allPermutations)) {
        goal
    } else {
        0
    }

fun checkAllCalculations(
    goal: Long,
    operands: List<Long>,
    allOperators: List<List<Operation>>
): Boolean {
    if (operands.size < 2) {
        return operands.first() == goal
    }
    return if (allOperators.isEmpty()) operands.first() == goal else
        allOperators.firstOrNull { operators ->
        if (operators.isEmpty()) return goal == operands.first()
        val result = operators.foldIndexed(operands[0]) { index, acc, operator ->
            operator.apply(acc, operands[index + 1])
        }
        result == goal
    } != null
}

abstract class Operation {
    abstract fun apply(a: Long, b: Long): Long
}

class Plus : Operation() {
    override fun apply(a: Long, b: Long): Long = a + b

}

class Multiply : Operation() {
    override fun apply(a: Long, b: Long): Long = a * b

}

class Merge : Operation() {
    override fun apply(a: Long, b: Long): Long = (a.toString() + b.toString()).toLong()
}

fun generatePermutations(size: Int, symbols: List<Operation>): List<List<Operation>> {
    tailrec fun generate(
        operatorsSoFar: List<List<Operation>>,
        depth: Int
    ): List<List<Operation>> {
        if (depth == 0) return operatorsSoFar
        val nextLevel = operatorsSoFar.flatMap { current -> symbols.map { symbol -> current + symbol } }
        return generate(nextLevel, depth - 1)
    }

    return generate(listOf(emptyList()), size)
}