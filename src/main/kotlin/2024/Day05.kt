package `2024`

class Day05 {
    companion object {
        fun part01(day05part01: List<String>): Int = Pair(extractRules(day05part01), extractUpdates(day05part01))
            .let { (rules, updates) ->
                findValidUpdates(updates, rules).sumOf {
                    it[it.size / 2]
                }
            }

        fun part02(day05part01: List<String>): Int {
            return Pair(extractRules(day05part01), extractUpdates(day05part01))
                .let { (rules, updates) ->
                    findInvalidUpdates(updates, rules).map { invalidUpdate ->
                        orderUpdate(invalidUpdate, rules)
                    }.sumOf {
                        it[it.size / 2]
                    }
                }
        }
    }
}

fun extractRules(day03part01: List<String>) =
    day03part01.subList(0, day03part01.indexOf("")).map {
        val splits = it.split("|")
        splits[0].toInt() to splits[1].toInt()
    }

fun extractUpdates(day03part01: List<String>) =
    day03part01.subList(day03part01.indexOf("") + 1, day03part01.size).map { update ->
        update.split(",").map { it.toInt() }
    }

fun findValidUpdates(updates: List<List<Int>>, rules: List<Pair<Int, Int>>) = updates.filter { update ->
    validateUpdate(update, rules)
}

fun findInvalidUpdates(updates: List<List<Int>>, rules: List<Pair<Int, Int>>) = updates.filter { update ->
    !validateUpdate(update, rules)
}

fun validateUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean = rules.all {
    validateRule(update, it)
}

fun validateRule(updates: List<Int>, rule: Pair<Int, Int>): Boolean {
    if (updates.contains(rule.first) && updates.contains(rule.second)) {
        return updates.indexOfFirst { pageNumber -> pageNumber == rule.first } < updates.indexOfFirst { pageNumber -> pageNumber == rule.second }
    }
    return true
}

fun validationPass(updates: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    val newList = mutableListOf<Int>()
    updates.forEach { page ->
        rules.filter { it.first == page }.firstOrNull() {
            newList.contains(it.second)
        }.let { index ->
            if (index != null) {
                newList.add(newList.indexOf(index.second), page)
            } else {
                newList.add(page)
            }
        }
    }
    return newList.toList()
}

tailrec fun orderUpdate(updates: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    val newUpdate = validationPass(updates, rules)
    return if (!validateUpdate(newUpdate, rules) && newUpdate != updates) {
        orderUpdate(newUpdate, rules)
    } else {
        return newUpdate
    }
}