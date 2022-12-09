package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day07Test {

    data class Node(val name: String, val isDir: Boolean, val files: MutableList<Node> = ArrayList(), var size: Long =0)

    fun calc(): HashMap<String, Node> {
        val path = ArrayList<String>()

        val root = Node("", true)
        val files = HashMap<String, Node>()
        files[""] = root

        fun processFile(lines: List<String>) {
            lines.forEach { line ->
                val parts = line.split(" ")
                val parent = files.getValue(path.joinToString("/"))
                if (parts[0] == "dir") {
                    val dirPath = path + parts[1]
                    val new = files.getOrPut(dirPath.joinToString("/")) {
                        Node(parts[1], isDir = true)
                    }
                    parent.files.add(new)
                } else {
                    parent.files.add(
                        Node(size = parts[0].toLong(), isDir = false, name = parts[1])
                    )
                }

            }
        }

        File("7.txt").readText().split("$").map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.reader().readLines() }
            .forEach { lines ->
                val commands = lines.first().split(" ")
                when (commands[0]) {
                    "cd" -> {
                        when (val dir = commands[1]) {
                            "/" -> path.clear()
                            ".." -> path.removeAt(path.lastIndex)
                            else -> path += dir
                        }
                    }
                    "ls" -> {
                        processFile(lines.drop(1))
                    }
                }

            }


        fun sumNode(n: Node): Long {
            if (n.isDir) {
                val sum = n.files.sumOf { sumNode(it) }

                n.size = sum
            }

            return n.size
        }

        sumNode(root)
        return files

    }

    @Test
    fun part1() {
        calc()
            .values.filter { it.isDir }
            .sortedBy { it.size }
            .filter { it.size <= 100000 }
            .onEach { println(it.name + " " + it.size) }
            .sumOf { it.size }
            .also { println("total $it") }
    }

    @Test
    fun part2() {
        val files = calc()
        val rootSize = files[""]!!.size

        val minDelete = 30000000 - (70000000 - rootSize)
        files.values
            .filter { it.isDir }
            .filter { it.size >= minDelete }
            .sortedBy { it.size }
            .onEachPrintln { it.name to it.size }
    }
}
