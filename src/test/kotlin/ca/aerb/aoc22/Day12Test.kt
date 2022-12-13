package ca.aerb.aoc22

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.junit.jupiter.api.Test
import java.io.File


class Day12Test {

    data class Position(val x: Int, val y: Int) {
        fun moves(): Sequence<Position> =
            sequenceOf(
                Position(x + 1, y),
                Position(x - 1, y),
                Position(x , y + 1),
                Position(x , y - 1),
            )
    }

    @Test
    fun moves() {
        Position(1, 1).moves().toList().onEachPrintln()
    }

    @Test
    fun part1() {

        val map = HashMap<Position, Int>()
        val lines = File("12.txt").readLines()

        lateinit var curr: Position
        lateinit var exit: Position

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val pos = Position(x, y)
                val height = when {
                    c.isLowerCase() -> c.code
                    c == 'S' -> {
                        curr = pos
                        'a'.code
                    }

                    c == 'E' -> {
                        exit = pos
                        'z'.code
                    }

                    else -> error(c)
                } - 'a'.code

                map[pos] = height
            }
        }

        println("$curr -> $exit")
        map.toList().onEachPrintln()

        val directedGraph: Graph<Position, DefaultEdge> = DefaultDirectedGraph(
            DefaultEdge::class.java
        )

        map.keys.forEach { pos ->
            directedGraph.addVertex(pos)
        }

        map.keys.flatMap { pos ->
            pos.moves().filter { it in map }
                .filter { map[it]!! - map[pos]!! <= 1 }
                .map { pos to it }
        }.forEach { (from, to) ->
            directedGraph.addEdge(from, to)
        }

        DijkstraShortestPath(directedGraph).getPath(curr, exit).length.alsoPrintln { "shortest = $it" }
    }

    @Test
    fun part2() {

        val map = HashMap<Position, Int>()
        val lines = File("12.txt").readLines()


        lateinit var exit: Position

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val pos = Position(x, y)
                val height = when {
                    c.isLowerCase() -> c.code
                    c == 'S' -> {
                        'a'.code
                    }

                    c == 'E' -> {
                        exit = pos
                        'z'.code
                    }

                    else -> error(c)
                } - 'a'.code

                map[pos] = height
            }
        }



        val directedGraph: Graph<Position, DefaultEdge> = DefaultDirectedGraph(
            DefaultEdge::class.java
        )

        map.keys.forEach { pos ->
            directedGraph.addVertex(pos)
        }

        map.keys.flatMap { pos ->
            pos.moves().filter { it in map }
                .filter { map[it]!! - map[pos]!! <= 1 }
                .map { pos to it }
        }.forEach { (from, to) ->
            directedGraph.addEdge(from, to)
        }

        val starts = map.toList().filter { (_, height) -> height == 0 }.map { it.first }
        starts.map { start ->
            println(start)
            DijkstraShortestPath(directedGraph).getPath(start, exit)?.length ?: Int.MAX_VALUE
        }.min().alsoPrintln { "short $it" }


    }
}
