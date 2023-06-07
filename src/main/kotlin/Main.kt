import java.lang.IllegalStateException

fun main(args: Array<String>) {
    val (gridRows, gridCols, bottles) = args.map(String::toInt)
        .map { it.also { if (it % 2 == 1 || it < 4) throw IllegalArgumentException("All args must be even and >4")} }
    println("Filling a $gridRows*$gridCols grid with $bottles bottles, such that each row and column has an even number of bottles...")
    distributeBottles(gridRows, gridCols, bottles)
        .let(::describe)
}
fun describe(gridSet: Collection<Grid>) {
    println("There are ${gridSet.size} solutions!")
    println("Here are a few:")
    println((1..3)
        .joinToString("\n\n") { gridSet.random().print() })
}

fun distributeBottles(gridRows: Int, gridCols: Int, bottles: Int): Collection<Grid> {
    (gridRows * gridCols - bottles)
        .let { openSpots -> if (openSpots < bottles) return distributeBottles(gridRows, gridCols, openSpots).map(Grid::negate) }
    return distributeBottles(bottles, setOf(Grid(gridRows, gridCols)))
}
fun distributeBottles(bottles: Int, currentGrids: Set<Grid>): Set<Grid> = when {
    bottles % 4 == 2 -> distributeBottles(bottles - 6, distributeSix(currentGrids))
    bottles >= 4 -> distributeBottles(bottles - 4, distributeFour(currentGrids))
    bottles == 0 -> currentGrids
    else -> throw IllegalStateException("Can't distribute less than four bottles! ($bottles)")
}

fun distributeFour(currentGrids: Set<Grid>) = currentGrids.flatMap { distributeFour(it) }.toSet()
@Suppress("UnnecessaryVariable")
fun distributeFour(grid: Grid): Set<Grid> =
    (0 until grid.dimensions().first).flatMap { bottle1Row ->
        grid.openCols(bottle1Row).flatMap { bottle1Col ->
            val bottle2Row = bottle1Row
            grid.openCols(bottle2Row, *zeroThrough(bottle1Col)).flatMap { bottle2Col ->
                val bottle3Col = bottle2Col
                grid.openRows(bottle3Col, *zeroThrough(bottle2Row)).mapNotNull { bottle3Row ->
                    val bottle4Row = bottle3Row
                    val bottle4Col = bottle1Col
                    if (grid.hasBottle(bottle4Row, bottle4Col)) null
                    else grid
                        .withBottle(bottle1Row, bottle1Col)
                        .withBottle(bottle2Row, bottle2Col)
                        .withBottle(bottle3Row, bottle3Col)
                        .withBottle(bottle4Row, bottle4Col)
                }
            }
        }
    }.toSet()
fun distributeSix(currentGrids: Set<Grid>) = currentGrids.flatMap { distributeSix(it) }.toSet()
fun distributeSix(grid: Grid): Set<Grid> =
    TODO()

fun zeroThrough(max: Int) = (0 .. max).toList().toIntArray()
typealias Grid = Array<BooleanArray>
fun Grid(gridRows: Int, gridCols: Int): Grid = Array(gridRows) { BooleanArray(gridCols) }
fun Grid.print() = joinToString("\n") { row -> row.joinToString("") { col -> if (col) "1" else "0" } }
fun Grid.negate(): Grid = map { row -> row.map { !it }.toBooleanArray() }.toTypedArray()
fun Grid.withBottle(row: Int, col: Int) = when {
    hasBottle(row, col) -> throw IllegalStateException("Cannot add bottle at ($row, $col):\n${print()}")
    else -> copy().let { it[row][col] = true; it }
}
fun Grid.hasBottle(row: Int, col: Int) = this[row][col]
fun Grid.copy() = Array(size) { get(it).clone() }
fun Grid.dimensions() = let { Pair(it.size, it[0].size) }

fun Grid.openRows(col: Int, vararg forbiddenRow: Int): Iterable<Int> = openIndices(getColumn(col), *forbiddenRow)
fun Grid.openCols(row: Int, vararg forbiddenCol: Int): Iterable<Int> = openIndices(get(row).toList(), *forbiddenCol)
fun openIndices(booleanArray: Iterable<Boolean>, vararg forbiddenIndex: Int) =
    booleanArray.withIndex().filter { !forbiddenIndex.contains(it.index) && !it.value }.map { it.index }
fun Grid.getColumn(i: Int) = map { it[i] }