package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day04Test {

    @Test
    fun part1() {
        File("4.txt").readLines()
            .map { line ->
                val (left, right) = line.split(",").map { part ->
                    val (start, end) = part.split("-")
                    IntRange(start.toInt(), end.toInt())
                }
                right.fullyContains(left) || left.fullyContains(right)
            }
            .count { it }
            .alsoPrintln()
    }

    @Test
    fun part2() {
        File("4.txt").readLines()
            .map { line ->
                val (left, right) = line.split(",").map { part ->
                    val (start, end) = part.split("-")
                    IntRange(start.toInt(), end.toInt())
                }
                right.partiallyContains(left) || left.partiallyContains(right)
            }
            .count { it }
            .alsoPrintln()
    }
}


