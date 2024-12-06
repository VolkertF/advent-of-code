package `2023`

object Day07 {
    fun part01(lines: List<String>): Long = parseLines(lines)
        .asSequence()
        .map { hand ->
            val scoredHand = hand.key.toScoredHand(false)
            Hand(scoredHand.mapToHandType(), scoredHand, hand.value.toLong())
        }
        .sorted().withIndex()
        .onEach { println(it) }
        .sumOf { it.value.bid * (it.index + 1) }

    fun part02(lines: List<String>): Long = parseLines(lines)
        .asSequence()
        .map { hand ->
            val scoredHand = hand.key.toScoredHand(true)
            Hand(scoredHand.mapToHandJokerHand(), scoredHand, hand.value.toLong())
        }
        .sorted().withIndex()
        .onEach { println(it) }
        .sumOf { it.value.bid * (it.index + 1) }
}

fun cardsToScore(joker: Boolean) = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to if (joker) 1 else 11,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
)

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    HIGHEST
}

data class Hand(
    val type: HandType,
    val cards: List<Int>,
    val bid: Long
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val typeComparison = other.type.compareTo(this.type)
        if (typeComparison != 0) {
            return typeComparison
        }
        val o1Cards = this.cards
        val o2Cards = other.cards
        o1Cards.zip(o2Cards).forEach { (card1, card2) ->
            val cardComparison = card1.compareTo(card2)
            if (cardComparison != 0) {
                return cardComparison
            }
        }
        return 0
    }


}

fun parseLines(lines: List<String>) = lines.associate {
    val split = it.split("\\s+".toRegex())
    split[0] to split[1]
}

fun String.toScoredHand(joker: Boolean) = this
    .map { card -> cardsToScore(joker)[card]!! }

fun List<Int>.mapToHandType(): HandType {
    val grouped = this.groupBy { it }.toList().sortedByDescending { it.second.count() }
    val first = grouped[0]
    val firstCount = first.second.count()
    return when {
        firstCount == 5 -> HandType.FIVE_OF_A_KIND
        firstCount == 4 -> HandType.FOUR_OF_A_KIND
        firstCount == 3 && grouped[1].second.count() == 2 -> HandType.FULL_HOUSE
        firstCount == 3 -> HandType.THREE_OF_A_KIND
        firstCount == 2 && grouped[1].second.count() == 2 -> HandType.TWO_PAIRS
        firstCount == 2 -> HandType.ONE_PAIR
        else -> HandType.HIGHEST

    }
}

fun List<Int>.mapToHandJokerHand(): HandType {
    val grouped = this.groupBy { it }.toList().sortedByDescending { it.second.count() }
    val first = grouped[0]
    val firstCount = first.second.count()
    val jokerCount = grouped.find { it.first == 1 }?.second?.count() ?: 0
    return when {
        firstCount == 5 -> HandType.FIVE_OF_A_KIND
        firstCount == 4 && first.first == 1 -> HandType.FIVE_OF_A_KIND
        firstCount == 4 && jokerCount == 1 -> HandType.FIVE_OF_A_KIND
        firstCount == 4 && jokerCount == 0 -> HandType.FOUR_OF_A_KIND
        firstCount == 3 && jokerCount == 3 && grouped[1].second.count() == 2 -> HandType.FIVE_OF_A_KIND
        firstCount == 3 && jokerCount == 3 -> HandType.FOUR_OF_A_KIND
        firstCount == 3 && jokerCount == 2 -> HandType.FIVE_OF_A_KIND
        firstCount == 3 && jokerCount == 1 -> HandType.FOUR_OF_A_KIND
        firstCount == 3 && jokerCount == 0 && grouped[1].second.count() == 2 -> HandType.FULL_HOUSE
        firstCount == 3 && jokerCount == 0 -> HandType.THREE_OF_A_KIND
        firstCount == 2 && first.first == 1 && jokerCount == 2 && grouped[1].second.count() == 2 -> HandType.FOUR_OF_A_KIND
        firstCount == 2 && first.first == 1 && jokerCount == 2 -> HandType.THREE_OF_A_KIND
        firstCount == 2 && first.first != 1 && jokerCount == 2 -> HandType.FOUR_OF_A_KIND
        firstCount == 2 && jokerCount == 1 && grouped[1].second.count() == 2 -> HandType.FULL_HOUSE
        firstCount == 2 && jokerCount == 1 -> HandType.THREE_OF_A_KIND
        firstCount == 2 && jokerCount == 0 && grouped[1].second.count() == 2 -> HandType.TWO_PAIRS
        firstCount == 2 && jokerCount == 0 -> HandType.ONE_PAIR
        jokerCount == 1 -> HandType.ONE_PAIR
        else -> HandType.HIGHEST
    }
}