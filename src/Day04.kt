import java.io.File

fun main() {
//    (('a' .. 'z') + ('A' .. 'Z')).forEach {
//        println("$it ${it.toPriority()}")
//    }

    File("3.p1.txt").readLines().map { line ->
        require(line.length % 2 == 0)
        val a = line.substring(0, line.length / 2)
        val b = line.substring(line.length / 2)

        a.toList().intersect(b.toList()).map { it.toPriority() }.sum()
    }.sum().also { println(it) }
}

private fun Char.toPriority(): Int = if (isLowerCase()) code - 96
else code - 38
