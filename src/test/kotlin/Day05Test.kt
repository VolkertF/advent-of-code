import org.junit.jupiter.api.Test
import util.resourceToStringList

object Day05Test {

    @Test
    fun `part 01 example input should evaluate to 35`() {
        println(Day05.part01(resourceToStringList("day05_part01.txt")))
    }

    @Test
    fun `part 02 example input should evaluate to 46`() {
        println(Day05.part02(resourceToStringList("day05_part01.txt")))
    }
}