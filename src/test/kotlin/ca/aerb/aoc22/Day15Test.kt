package ca.aerb.aoc22

import ca.aerb.aoc22.CoordinateMap.Coord
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun distanceBetween(sensor: Coord, beacon: Coord): Int =
    abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)

class Day15Test {


    data class Sensor(
        val center: Coord,
        val distance: Int,
        val minX: Coord,
        val maxX: Coord,
        val minY: Coord,
        val maxY: Coord,
    ) {
        fun seesY(y: Int): Boolean = y >= minY.y && y <= maxY.y

        fun ycross(y: Int): IntRange {
            val remaining =
                distance - distanceBetween(center, Coord(center.x, y))
            val leftEdge = center.x - remaining
            val rightEdge = center.x + remaining
            return IntRange(leftEdge, rightEdge)
        }
    }


    private fun String.parseCoord(): Coord {
        return split(", ")
            .map { it.removePrefix("x=").removePrefix("y=").toInt() }
            .let { (x, y) -> Coord(x, y) }
    }


    @Test
    fun part2() {

        val map = CoordinateMap()

        val lines = File("15.txt").readLines()
        val input = lines.dropLast(1).map { line ->
            val (sensor, beacon) = line.split(": ")
            sensor.removePrefix("Sensor at ").parseCoord() to
                    beacon.removePrefix("closest beacon is at ").parseCoord()
        }

        val (qline, max) = lines.last().split(",").map { it.toInt() }

        val sensors = input
            //.filter { it.first == Coord(8,7) }
            .map { (sensor, beacon) ->
                val md = distanceBetween(sensor, beacon)
                Sensor(
                    sensor,
                    md,
                    sensor.plus(-md, 0),
                    sensor.plus(+md, 0),
                    sensor.plus(0, -md),
                    sensor.plus(0, +md),
                )
            }

        input.forEach { (sensor, beacon) ->
            map.insert(sensor, "S")
            map.insert(beacon, "B")
        }


        val sections = sensors.filter { it.seesY(qline) }
            .map { sensor -> sensor.ycross(qline) }
            .toMutableList()

        mergeLoop@ while (true) {
            for (i in 0 until sections.size) {
                for (j in i + 1 until sections.size) {
                    val a = sections[i]
                    val b = sections[j]
                    if (a.intersect(b) != null) {
                        val c = IntRange(min(a.first, b.first), max(a.last, b.last))
                        sections.removeAt(j)
                        sections.removeAt(i)
                        sections += c
                        continue@mergeLoop
                    }
                }
            }
            break@mergeLoop
        }

        val beacons = map.row(qline).count { it.second == "B" }

        sections.onEachPrintln()
        sections.sumOf { section -> section.size() }
            .minus(beacons)
            .alsoPrintln { "size = $it" }
    }

    @Test
    fun part1() {

        val map = CoordinateMap()

        val lines = File("15.txt").readLines()
        val input = lines.dropLast(1).map { line ->
            val (sensor, beacon) = line.split(": ")
            sensor.removePrefix("Sensor at ").parseCoord() to
                    beacon.removePrefix("closest beacon is at ").parseCoord()
        }

        val qline = lines.last().toInt()

        val sensors = input
            //.filter { it.first == Coord(8,7) }
            .map { (sensor, beacon) ->
                val md = distanceBetween(sensor, beacon)
                Sensor(
                    sensor,
                    md,
                    sensor.plus(-md, 0),
                    sensor.plus(+md, 0),
                    sensor.plus(0, -md),
                    sensor.plus(0, +md),
                )
            }

        input.forEach { (sensor, beacon) ->
            map.insert(sensor, "S")
            map.insert(beacon, "B")
        }


        val sections = sensors.filter { it.seesY(qline) }
            .map { sensor -> sensor.ycross(qline) }
            .toMutableList()

        mergeLoop@ while (true) {
            for (i in 0 until sections.size) {
                for (j in i + 1 until sections.size) {
                    val a = sections[i]
                    val b = sections[j]
                    if (a.intersect(b) != null) {
                        val c = IntRange(min(a.first, b.first), max(a.last, b.last))
                        sections.removeAt(j)
                        sections.removeAt(i)
                        sections += c
                        continue@mergeLoop
                    }
                }
            }
            break@mergeLoop
        }

        val beacons = map.row(qline).count { it.second == "B" }

        sections.onEachPrintln()
        sections.sumOf { section -> section.size() }
            .minus(beacons)
            .alsoPrintln { "size = $it" }
    }


    @Test
    fun intersectWorks() {
        IntRange(0, 1).intersect(IntRange(2, 3)).alsoPrintln()
        IntRange(2, 3).intersect(IntRange(0, 1)).alsoPrintln()
        IntRange(0, 1).intersect(IntRange(1, 3)).alsoPrintln()
        IntRange(1, 3).intersect(IntRange(0, 1)).alsoPrintln()
        IntRange(1, 3).intersect(IntRange(2, 2)).alsoPrintln()
        IntRange(1, 5).intersect(IntRange(2, 3)).alsoPrintln()
    }
}

private fun IntRange.size(): Int = (endInclusive + 1) - start

fun IntRange.intersect(other: IntRange): IntRange? {
    val start = max(first, other.first)
    val endInclusive = min(last, other.last)
    return if (start <= endInclusive) IntRange(start, endInclusive) else null
}

fun <E> List<E>.allCombos(): List<Pair<E, E>> {
    val result = ArrayList<Pair<E, E>>()
    for (i in 0 until size) {
        for (j in i + 1 until size) {
            result += this[i] to this[j]
        }
    }
    return result
}
