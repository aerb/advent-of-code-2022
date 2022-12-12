package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day11Test {

    data class Monkey(
        val id: String,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val divisibleBy: Long,
        val trueThrow: Int,
        val falseThrow: Int,
        var inspected: Long = 0,
    )

    @Test
    fun part2() {
        val monkeys = File("11.txt").readText().split("\n\n")
            .map { group ->
                val lines = group.reader().readLines().map { it.trim() }
                Monkey(
                    id = lines[0].removeSurrounding("Monkey ", ":"),
                    items = lines[1].removePrefix("Starting items: ")
                        .split(", ")
                        .map { it.toLong() }
                        .toMutableList(),
                    operation = lines[2].removePrefix("Operation: ").split("=")
                        .last().split(" ")
                        .filter { it.isNotEmpty() }
                        .let(this::toOperator),
                    divisibleBy = lines[3].removePrefix("Test: divisible by ")
                        .toLong(),
                    trueThrow = lines[4].removePrefix("If true: throw to monkey ")
                        .toInt(),
                    falseThrow = lines[5].removePrefix("If false: throw to monkey ")
                        .toInt(),
                )
            }
            .onEachPrintln()

        val cycleLength = monkeys.map { it.divisibleBy }.product()
            .alsoPrintln { "cycle $it" }

        repeat(10_000) {
            for (m in monkeys) {
                m.items.forEach { item ->
                    val worry = m.operation(item) % cycleLength
                    if (worry % m.divisibleBy == 0L) {
                        monkeys[m.trueThrow].items += worry
                    } else {
                        monkeys[m.falseThrow].items += worry
                    }
                }
                m.inspected += m.items.size
                m.items.clear()
            }
        }

        println()
        println()

        monkeys.onEachPrintln()

        monkeys.sortedByDescending { it.inspected }
            .take(2)
            .fold(1L) { acc, m -> acc * m.inspected}
            .alsoPrintln { "MB = $it" }
    }

    @Test
    fun part1() {
        val monkeys = File("11.txt").readText().split("\n\n")
            .map { group ->
                val lines = group.reader().readLines().map { it.trim() }
                Monkey(
                    id = lines[0].removeSurrounding("Monkey ", ":"),
                    items = lines[1].removePrefix("Starting items: ")
                        .split(", ")
                        .map { it.toLong() }
                        .toMutableList(),
                    operation = lines[2].removePrefix("Operation: ").split("=")
                        .last().split(" ")
                        .filter { it.isNotEmpty() }
                        .let(this::toOperator),
                    divisibleBy = lines[3].removePrefix("Test: divisible by ")
                        .toLong(),
                    trueThrow = lines[4].removePrefix("If true: throw to monkey ")
                        .toInt(),
                    falseThrow = lines[5].removePrefix("If false: throw to monkey ")
                        .toInt(),
                )
            }
            .onEachPrintln()


        repeat(20) {
            for (m in monkeys) {
                m.items.forEach { item ->
                    val worry = m.operation(item) / 3
                    if (worry % m.divisibleBy == 0L) {
                        monkeys[m.trueThrow].items += worry
                    } else {
                        monkeys[m.falseThrow].items += worry
                    }
                }
                m.inspected += m.items.size
                m.items.clear()
            }
        }

        println()
        println()

        monkeys.onEachPrintln()

        monkeys.sortedByDescending { it.inspected }
            .take(2)
            .fold(1L) { acc, m -> acc * m.inspected}
            .alsoPrintln { "MB = $it" }
    }

    private fun toOperator(parts: List<String>): (Long) -> Long {
        return { arg ->
            val operand0 = if (parts[0] == "old") arg else parts[0].toLong()
            val operand1 = if (parts[2] == "old") arg else parts[2].toLong()
            if (parts[1] == "*") operand0 * operand1
            else if (parts[1] == "+") operand0 + operand1
            else error(parts)
        }
    }
}


