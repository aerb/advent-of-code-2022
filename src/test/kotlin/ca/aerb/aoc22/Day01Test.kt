package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day01Test {
    @Test
    fun part2() {
        File("1.p1.txt").readText().split("\n\n")
            .map { group ->
                group.reader().readLines().sumOf { line -> line.toInt() }
            }
            .sorted()
            .takeLast(3)
            .sum()
            .also { println(it) }
    }
}
