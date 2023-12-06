import org.junit.jupiter.api.Test
import util.resourceToStringList

class Day06Test {

    @Test
    fun `part 01 example input should evaluate to 288`() {
        println(Day06.part01(resourceToStringList("day06_part01.txt")))
    }

    @Test
    fun `part 02 example input should evaluate to 71503`() {
        println(Day06.part02(resourceToStringList("day06_part02.txt")))
    }
}