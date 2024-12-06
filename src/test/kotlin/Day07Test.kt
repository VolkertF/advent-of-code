import `2023`.Day07
import org.junit.jupiter.api.Test
import util.resourceToStringList

object Day07Test {

    @Test
    fun `part 01 example input should evaluate to 6592`() {
        println(Day07.part01(resourceToStringList("day07_part01.txt")))
    }

    @Test
    fun `part 02 example input should evaluate to 6839`() {
        println(Day07.part02(resourceToStringList("day07_part01.txt")))
    }
}