import java.io.File

fun main() {
    File("3.p1.txt").readLines().map { it.toList() }.chunked(3).map {
        it[0].intersect(it[1]).intersect(it[2])
    }.map {
        it.map { it.toPriority() }.sum()
    }.sum()
        .also { println(it) }
}

private fun Char.toPriority(): Int = if (isLowerCase()) code - 96
else code - 38
