import `2023`.Day04
import org.junit.jupiter.api.Test
import util.resourceToStringList

object Day04Test {

    @Test
    fun `part 01 example input should evaluate to 13`() {
        println(Day04.part01(resourceToStringList("day04_part01.txt")))
    }

    @Test
    fun `part 02 example input should evaluate to 30`() {
        println(Day04.part02(resourceToStringList("day04_part02.txt")))
    }
}