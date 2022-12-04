package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day03Test {

    private fun Char.toPriority(): Int =
        if (isLowerCase()) code - 96
        else code - 38

    @Test
    fun part1() {
        File("3.txt").readLines().map { line ->
            require(line.length % 2 == 0)
            val a = line.substring(0, line.length / 2)
            val b = line.substring(line.length / 2)

            a.toList().intersect(b.toSet()).sumOf { it.toPriority() }
        }.sum().also { println(it) }
    }

    @Test
    fun part2() {
        File("3.txt").readLines().asSequence()
            .map { it.toSet() }.chunked(3).map {
                it[0].intersect(it[1]).intersect(it[2])
            }.map {
                it.sumOf { it.toPriority() }
            }.sum()
            .also { println(it) }
    }
}
