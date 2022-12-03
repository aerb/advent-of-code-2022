import java.io.File

enum class Type {
    Rock,
    Paper,
    Scissors
}

fun main() {

    val rules =
        File("2.p1.txt").readLines().map {
            it.split(" ").let {
                    (a, b) -> a.toType() to b.toType()
            }
        }

    val total = rules.map {
        val score = outcome(it.first, it.second) + it.second.ordinal + 1
        println("$it $score")
        score
    }.sum()
    println(total)
}

fun outcome(first: Type, second: Type): Int = when {
    (first.ordinal + 1) % 3 == second.ordinal -> 6
    first.ordinal == second.ordinal -> 3
    else -> 0
}

private fun String.toType(): Type = when (this) {
    "A" -> Type.Rock
    "B" -> Type.Paper
    "C" -> Type.Scissors
    "X" -> Type.Rock
    "Y" -> Type.Paper
    "Z" -> Type.Scissors
    else -> error(this)
}
