package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day05Test {

    data class Crane(
        val id: String,
        val index: Int,
        val stack: MutableList<String> = ArrayList()
    )

    data class Inst(
        val count: Int,
        val fromId: String,
        val toId: String,
    )


    val lines = File("5.txt").readLines()
    val init = lines.takeWhile { it.trim().startsWith("[") }.map { line ->
        line.mapIndexed { index, c ->
            if (c.isLetter()) {
                index to c
            } else {
                -1 to null
            }
        }.associate { it }
    }

    val cranes: Map<String, Crane> =
        lines.first { it.trim().startsWith("1") }.mapIndexed { index, c ->
            if (c.isDigit()) {
                index to c
            } else {
                -1 to null
            }
        }.filter { it.first != -1 }
            .associate {
                it.second.toString() to Crane(
                    id = it.second.toString(),
                    index = it.first
                )
            }

    init {
        cranes.values.forEach { c ->
            init.forEach { row ->
                val curr = row[c.index]
                if (curr != null) {
                    c.stack.add(curr.toString())
                }
            }
        }
    }

    val instructions = lines.dropWhile { !it.startsWith("move") }
        .map {
            val match =
                Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!
            Inst(
                count = match.groupValues[1].toInt(),
                fromId = match.groupValues[2],
                toId = match.groupValues[3],
            )
        }

    @Test
    fun part1() {
        fun process(inst: Inst) {
            repeat(inst.count) {
                val box = cranes[inst.fromId]!!.stack.removeAt(0)
                cranes[inst.toId]!!.stack.add(0, box)
            }
        }

        instructions.forEach { process(it) }

        cranes.values.onEachPrintln()
        val code = cranes.values.map { it.stack.first() }.joinToString("")
        println(code)
    }

    @Test
    fun part2() {
        fun process(inst: Inst) {
            val crates = ArrayList<String>()
            repeat(inst.count) {
                crates += cranes[inst.fromId]!!.stack.removeAt(0)
            }
            crates.reversed().forEach {
                cranes[inst.toId]!!.stack.add(0, it)
            }
        }

        instructions.forEach { process(it) }

        cranes.values.onEachPrintln()
        val code = cranes.values.map { it.stack.first() }.joinToString("")
        println(code)
    }

}
