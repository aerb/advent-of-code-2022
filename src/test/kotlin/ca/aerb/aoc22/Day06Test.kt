package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day06Test {

    private fun firstDistinct(chars: Int) {
        File("6.txt").readLines().forEach { code ->
            code.mapIndexed { index, c -> index.plus(1) to c }
                .windowed(size = chars, step = 1, partialWindows = false)
                .first { window ->
                    window.distinctBy { it.second }.size == window.size
                }.last()
                .alsoPrintln()
        }
    }

    @Test
    fun part1() {
        firstDistinct(4)
    }

    @Test
    fun part2() {
        firstDistinct(14)
    }
}
