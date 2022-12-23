package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File
import java.util.TreeMap

class Day14Test {

    data class Coord(val x: Int, val y: Int) {
        fun up(i: Int): Coord = copy(y = y - i)
        fun down(i: Int): Coord = copy(y = y + i)
        fun left(i: Int): Coord = copy(x = x - i)
        fun right(i: Int): Coord = copy(x = x + i)
    }

    enum class Object {
        Rock,
        Sand,
    }

    private var floor: Int
    private var yRange: IntRange
    private var xRange: IntRange
    val map = TreeMap<Int, TreeMap<Int, Object>>()

    private val entrance = Coord(500, 0)

    private fun insert(c: Coord, o: Object) {
        map.getOrPut(c.x, ::TreeMap)[c.y] = o
    }

    private fun nextItemBelow(c: Coord): Coord? {
        val col = map[c.x]?.navigableKeySet() ?: return null
        val y = col.ceiling(c.y) ?: return null
        return Coord(c.x, y)
    }

    private fun fetch(c: Coord): Object? = map[c.x]?.get(c.y)

    private fun fetchOrFloor(c: Coord): Object? {
        if (c.y >= floor) return Object.Rock
        return map[c.x]?.get(c.y)
    }

    private fun drawRockLine(left: Coord, right: Coord) {
        val xRange = intRangeFrom(left.x, right.x)
        val yRange = intRangeFrom(left.y, right.y)
        val coords = when {
            xRange.count() == 1 -> yRange.map { y -> Coord(xRange.single(), y) }
            yRange.count() == 1 -> xRange.map { x -> Coord(x, yRange.single()) }
            else -> error("Unexpected line $left $right")
        }
        for (coord in coords) {
            insert(coord, Object.Rock)
        }
    }

    init {
        File("14.txt").readLines().map { line ->
            line.split(" -> ").map { part ->
                val (x, y) = part.split(",").map { it.toInt() }
                Coord(x, y)
            }
        }.forEach { path ->
            path.zipWithNext().forEach { (left, right) ->
                drawRockLine(left, right)
            }
        }
        xRange = map.keys.let { intRangeFrom(it.min(), it.max()) }
        yRange = map.values.flatMap { it.keys }.let { intRangeFrom(it.min(), it.max()) }
        floor = yRange.last + 2
    }

    @Test
    fun part1() {
        fun dropFrom(c: Coord): Boolean {
            val toRest = nextItemBelow(c)?.up(1) ?: return false
            if (fetch(toRest.left(1).down(1)) == null) {
                return dropFrom(toRest.left(1).down(1))
            } else if (fetch(toRest.right(1).down(1)) == null) {
                return dropFrom(toRest.right(1).down(1))
            } else {
                insert(toRest, Object.Sand)
                return true
            }
        }

        var i = 0
        while (true) {
            if(!dropFrom(entrance)) break
            i ++
        }

        println("took $i")
    }

    private fun nextItemOrFloor(c: Coord): Coord =
        (nextItemBelow(c) ?: Coord(c.x, floor)).up(1)

    private fun draw() {
        for (y in yRange) {
            for (x in xRange) {
                val c = when(fetch(Coord(x, y))) {
                    Object.Rock -> "#"
                    Object.Sand -> "O"
                    null -> "."
                }
                print(c)
            }
            println()
        }
        println(xRange)
        println(yRange)
    }

    @Test
    fun part2() {
        fun dropFrom(c: Coord): Boolean {
            val toRest = nextItemOrFloor(c)

            if (fetchOrFloor(toRest.left(1).down(1)) == null) {
                return dropFrom(toRest.left(1).down(1))
            } else if (fetchOrFloor(toRest.right(1).down(1)) == null) {
                return dropFrom(toRest.right(1).down(1))
            } else {
                insert(toRest, Object.Sand)
                return toRest == entrance
            }
        }

        draw()
        return
        var i = 0
        while (true) {
            if(dropFrom(entrance)) break
            i ++
        }

        draw()

        println("took ${i + 1}")
    }
}
