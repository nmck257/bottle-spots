import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class MainKtTest {
    @TestFactory
    fun expectedTotals() = listOf(
        listOf(4, 6, 4, 90),
        listOf(4, 6, 18, 480),
    ).map {
        val gridRows = it[0]
        val gridCols = it[1]
        val bottles = it[2]
        val expected = it[3]
        DynamicTest.dynamicTest("Grid $gridRows*$gridCols with $bottles bottles should have $expected solutions") {
            assertEquals(expected, distributeBottles(gridRows, gridCols, bottles).size)
        }
    }
}