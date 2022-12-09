package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09Test {

    data class Position(var row: Int, var col: Int)

    private val moves = File("9.txt").readLines().map { line ->
        val parts = line.split(" ")
        parts[0].single() to parts[1].toInt()
    }.flatMap { (move, size) -> List(size) { move } }

    @Test
    fun part2() {
        val knots = List(10) { Position(0, 0) }
        val head = knots.first()
        val tail = knots.last()

        val positions = HashSet<Position>()
        positions += Position(0, 0)

        moves.forEach { move ->
            when (move) {
                'U' -> head.row++
                'D' -> head.row--
                'L' -> head.col--
                'R' -> head.col++
            }
            knots.zipWithNext().forEach { (a, b) ->
                val diff = diff(a, b)
                val dC = diff.col.absoluteValue
                val dR = diff.row.absoluteValue
                val xC = diff.col.absoluteValue > 1
                val xR = diff.row.absoluteValue > 1
                when {
                    xC -> {
                        b.col += diff.col.sign
                        if (dR != 0) {
                            b.row += diff.row.sign
                        }
                    }

                    xR -> {
                        b.row += diff.row.sign
                        if (dC != 0) {
                            b.col += diff.col.sign
                        }
                    }
                }
            }
            positions += tail.copy()

            println("$move")
            println("head = $head")
            println("tail = $tail")
            println("visited = ${positions.size}")
        }
    }

    @Test
    fun part1() {

        val head = Position(0, 0)
        val tail = Position(0, 0)

        val positions = HashSet<Position>()
        positions += Position(0, 0)

        fun diff() = Position(
            head.row - tail.row,
            head.col - tail.col
        )

        moves.forEach { move ->
            when (move) {
                'U' -> head.row++
                'D' -> head.row--
                'L' -> head.col--
                'R' -> head.col++
            }
            val diff = diff()
            val dC = diff.col.absoluteValue
            val dR = diff.row.absoluteValue
            val xC = diff.col.absoluteValue > 1
            val xR = diff.row.absoluteValue > 1
            when {
                xC -> {
                    tail.col += diff.col.sign
                    if (dR != 0) {
                        tail.row += diff.row.sign
                    }
                }

                xR -> {
                    tail.row += diff.row.sign
                    if (dC != 0) {
                        tail.col += diff.col.sign
                    }
                }
            }
            positions += tail.copy()

            println("$move")
            println("head = $head")
            println("tail = $tail")
            println("visited = ${positions.size}")
        }
    }

    private fun diff(a: Position, b: Position): Position = Position(
        a.row - b.row,
        a.col - b.col
    )
}



