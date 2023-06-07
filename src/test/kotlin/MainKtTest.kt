import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class MainKtTest {
    @TestFactory
    fun `expected totals`() = listOf(
        listOf(4, 6, 4, 90),
        listOf(4, 6, 20, 90),
        listOf(4, 6, 18, 480),
        listOf(4, 6, 6, 480),
    ).map {
        val gridRows = it[0]
        val gridCols = it[1]
        val bottles = it[2]
        val expected = it[3]
        DynamicTest.dynamicTest("Grid $gridRows*$gridCols with $bottles bottles should have $expected solutions") {
            assertEquals(expected, distributeBottles(gridRows, gridCols, bottles).size)
        }
    }

    @TestFactory
    fun `no duplicates`() = listOf(
        listOf(4, 6, 4),
        listOf(4, 6, 6),
        listOf(4, 6, 8),
        listOf(4, 6, 12),
        listOf(4, 6, 18),
    ).map {
        val gridRows = it[0]
        val gridCols = it[1]
        val bottles = it[2]
        DynamicTest.dynamicTest("Grid $gridRows*$gridCols with $bottles bottles should have no duplicate solutions") {
            val solutions = distributeBottles(gridRows, gridCols, bottles)
            val secondSet = hashSetOf<Grid>()
            solutions.forEach { sol ->
                assertTrue(secondSet.none { sol2 -> sol2.matches(sol) }, "Duplicate found:\n${sol.print()}")
                secondSet.add(sol)
            }
        }
    }
}