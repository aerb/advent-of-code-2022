package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day10Test {

    private val instructions = File("10.txt").readLines().map { it.split(" ") }

    @Test
    fun part1() {
        var cycle = 1
        var x = 1

        val interesting = ArrayList<Pair<Int, Int>>()

        fun advance(op: () -> Unit = {}) {
            cycle ++
            op()
            if (cycle in setOf(20, 60, 100, 140, 180, 220)) {
                println("Interesting at cycle=$cycle and x=$x")
                interesting += cycle to x
            }
        }

        for (inst in instructions) {
            println(inst)
            when (inst.first()) {
                "noop" -> advance()
                "addx" -> {
                    val count = inst[1].toInt()
                    advance()
                    advance {
                        x += count
                    }

                }
                else -> error(inst)
            }
            println("cycle:$cycle x=$x")
        }

        interesting.sumOf { it.first * it.second }
            .alsoPrintln { "sum = $it" }
    }

    @Test
    fun part2() {
        var cycle = 1
        var x = 1

        var col = 0
        val row = ArrayList<Char>()

        fun advance(op: () -> Unit = {}) {
            if (col in setOf(x - 1, x, x + 1)) {
                row += '#'
            } else {
                row += '.'
            }
            println("cycle:$cycle x=$x col=$col")
            println(row.joinToString(""))
            col = (col + 1) % 40
            cycle ++
            op()

        }

        for (inst in instructions) {
            println(inst)
            when (inst.first()) {
                "noop" -> advance()
                "addx" -> {
                    val count = inst[1].toInt()
                    advance()
                    advance {
                        x += count
                    }

                }
                else -> error(inst)
            }
        }

        row.chunked(40).forEach {
            println(it.joinToString(""))
        }
    }
}
