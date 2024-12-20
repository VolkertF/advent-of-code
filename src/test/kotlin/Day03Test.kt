import `2023`.Day03
import org.junit.jupiter.api.Test
import util.resourceTo2DCharList

object Day03Test {

    @Test
    fun `part 01 example input should evaluate to 4361`() {
        println(Day03.part01(resourceTo2DCharList("day03_part01.txt")))
    }

    @Test
    fun `part 02 example input should evaluate to 467835`() {
        println(Day03.part02(resourceTo2DCharList("day03_part02.txt")))
    }
}