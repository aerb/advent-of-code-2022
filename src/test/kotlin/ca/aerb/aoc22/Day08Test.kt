package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day08Test {

    private var lines: List<String> = File("8.txt").readLines()

    private var map: java.util.HashMap<Position, Tree>
    private var edgeOfCols: Set<Int>
    private var edgeOfRows: Set<Int>

    data class Position(val row: Int, val col: Int)

    data class Tree(
        val height: Int,
        val position: Position,
        var isVisible: Boolean
    )

    init {
        edgeOfRows = setOf(0, lines.lastIndex)
        edgeOfCols = setOf(0, lines.first().lastIndex)

        map = HashMap()

        lines.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                val p = Position(row, col)
                map[p] = Tree(
                    height = c.toString().toInt(),
                    position = p,
                    isVisible = col in edgeOfCols || row in edgeOfRows
                )
            }
        }
    }

    fun fetchAbove(position: Position): List<Tree> {
        return IntRange(0, position.row - 1).map { row ->
            map.getValue(Position(row, position.col))
        }.reversed()
    }

    fun fetchBelow(position: Position): List<Tree> {
        return IntRange(position.row + 1, edgeOfRows.max()).map { row ->
            map.getValue(Position(row, position.col))
        }
    }

    fun fetchLeft(position: Position): List<Tree> {
        return IntRange(0, position.col - 1).map { col ->
            map.getValue(Position(position.row, col))
        }.reversed()
    }

    fun fetchRight(position: Position): List<Tree> {
        return IntRange(position.col + 1, edgeOfCols.max()).map { col ->
            map.getValue(Position(position.row, col))
        }
    }

    fun fetchAdjacent(tree: Tree): List<List<Tree>> {
        return listOf(
            fetchAbove(tree.position),
            fetchBelow(tree.position),
            fetchLeft(tree.position),
            fetchRight(tree.position),
        )
    }

    @Test
    fun adj() {
        val count = fetchAdjacent(Tree(0, Position(2, 2), false)).flatten()
            .map { it.position }
            .onEachPrintln()
            .count()

        val numY = lines.size - 1
        val numX = lines.first().length - 1
        require(count == (numY + numX))
    }

    @Test
    fun part1() {
        fun determineVisible(tree: Tree) {
            tree.isVisible = fetchAdjacent(tree).any { direction ->
                val result = direction.all { tree.height > it.height }
                if (tree.position == Position(3, 1)) {
                    println(result)
                }
                result
            }
        }

        map.values.forEach {
            if (it.isVisible) return@forEach
            determineVisible(it)
        }

        map.values.onEachPrintln()
        map.values.count { it.isVisible }.alsoPrintln()
    }

    @Test
    fun part2() {
        fun score(tree: Tree): Int {
            return fetchAdjacent(tree).map { direction ->

                val result = direction.takeUntil { it.height >= tree.height }
                //println("$tree ${result.map { it.height }}")
                result.size
            }.fold(1) { acc, i -> acc * i }
        }

        map.values.map { tree ->
            tree to score((tree))
        }.sortedBy { it.second }
            .onEachPrintln()
    }
}

private fun <E> List<E>.takeUntil(function: (E) -> Boolean): List<E> {
    val filtered = ArrayList<E>()
    forEach { item ->
        val result = function(item)
        filtered += item
        if (result) {
            return filtered
        }
    }
    return filtered
}
