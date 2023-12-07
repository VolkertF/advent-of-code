import org.junit.jupiter.api.Test
import util.resourceToStringList

object Day01Test {

    @Test
    fun `part 01 example input should evaluate to 142`() {
        println(Day01.part01(resourceToStringList("day01_part01.txt")))

    }

    @Test
    fun `part 02 example input should evaluate to 281`() {
        println(Day01.part02(resourceToStringList("day01_part02.txt")))
    }
}