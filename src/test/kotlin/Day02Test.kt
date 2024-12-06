import `2023`.Day02
import org.junit.jupiter.api.Test
import util.resourceToStringList

object Day02Test {

    @Test
    fun `part 01 example input should evaluate to 8`() {
        Day02.part01(resourceToStringList("day02_part01.txt")).also{println(it)}
    }

    @Test
    fun `part 02 example input should evaluate to 2286`() {
        Day02.part02(resourceToStringList("day02_part02.txt")).also{println(it)}
    }
}