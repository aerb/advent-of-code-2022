package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File
import java.lang.StringBuilder

class Day13Test {

    sealed interface Node {
        fun maybeWrap(): Node
        fun wrap(): Node = ListNode(listOf(this))
    }

    data class NumNode(val value: Int) : Node {
        override fun maybeWrap(): Node = ListNode(listOf(this))
        override fun toString(): String = value.toString()
    }

    data class ListNode(val values: List<Node>) : Node {
        override fun maybeWrap(): Node = this
        override fun toString(): String =
            values.joinToString(prefix = "[", postfix = "]")
    }

    private fun String.toNode(i: Int): Pair<Int, Node> {
        var curr = i
        when {
            this[curr].isDigit() -> {
                val sb = StringBuilder()
                while (this[curr].isDigit()) {
                    sb.append(this[curr++])
                }
                return curr to NumNode(sb.toString().toInt())
            }

            this[curr] == '[' -> {
                curr++
                val nodes = ArrayList<Node>()
                while (this[curr] != ']') {
                    val (nextI, node) = toNode(curr)
                    nodes += node
                    curr = nextI
                    if (this[curr] == ',') {
                        curr++
                    }
                }
                curr++
                return curr to ListNode(nodes)
            }

            else -> error("Unexpected pos=$curr value=${this[curr]}")
        }
    }


    enum class Order {
        Yes, No, Unknown
    }


    private fun inOrder(left: Node, right: Node): Order {
        if (left is NumNode && right is NumNode) {
            return when {
                left.value < right.value -> Order.Yes
                left.value > right.value -> Order.No
                else -> Order.Unknown
            }
        } else if (left is ListNode && right is ListNode) {
            val order = left.values.zip(right.values)
                .map { inOrder(it.first, it.second) }
                .firstOrNull { it != Order.Unknown }
            if (order != null) {
                return order
            }
            return when {
                left.values.size < right.values.size -> Order.Yes
                left.values.size > right.values.size -> Order.No
                else -> Order.Unknown
            }
        } else {
            return inOrder(left.maybeWrap(), right.maybeWrap())
        }
    }

    private val packets = File("13.txt").readText().split("\n\n")
        .map { line ->
            val (left, right) = line.trim().reader()
                .readLines()
                .map { it.toNode(0).second }
            left to right
        }

    @Test
    fun part1() {
        packets.onEachPrintln()
            .mapIndexed { index, pair ->
                index.plus(1) to inOrder(pair.first, pair.second)
            }.onEachPrintln()
            .filter { it.second == Order.Yes }
            .sumOf { it.first }
            .alsoPrintln { "sum = $it" }
    }

    @Test
    fun part2() {
        val div0 = NumNode(2).wrap().wrap()
        val div1 = NumNode(6).wrap().wrap()
        val sorted = packets.flatMap { listOf(it.first, it.second) }
            .plus(div0)
            .plus(div1)
            .sortedWith { left, right ->
                when(inOrder(left, right)) {
                    Order.Yes -> -1
                    Order.No -> +1
                    Order.Unknown -> 0
                }
            }

        val i0 = sorted.indexOf(div0) + 1
        val i1= sorted.indexOf(div1) + 1
        println("$i0 * $i1 = ${i0 * i1}")
    }
}

