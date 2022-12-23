package ca.aerb.aoc22

class CoordinateMap() {

    data class Coord(val x: Int, val y: Int) {
        fun plus(x: Int, y: Int): Coord = Coord(this.x + x, this.y + y)
    }

    val map = HashMap<Int, HashMap<Int, String>>()

    fun insert(c: Coord, s: String) {
        map.getOrPut(c.y, ::HashMap)[c.x] = s
    }

    fun fetch(x: Int, y: Int): String? = map[y]?.get(x)

    fun print() {
        val minY = map.keys.min()
        val maxY = map.keys.max()

        val minX = map.values.flatMap { it.keys }.min()
        val maxX = map.values.flatMap { it.keys }.max()

        println("$minX $maxX")
        println("$minY $maxY")

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(fetch(x, y) ?: " ")
            }
            println()
        }
    }

    fun row(y: Int): List<Pair<Int, String>> = map[y]?.toList() ?: emptyList()
}
