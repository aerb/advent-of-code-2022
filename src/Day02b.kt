import java.io.File


fun main() {

    val rules =
        File("2.p1.txt").readLines().map {
            it.split(" ").let {
                    (a, b) -> a.toType() to b.toOutcome()
            }
        }

    val total = rules.map {
        val play = shouldPlay(it)
        println("$it $play")
        it.second.score + play.ordinal + 1
    }.sum()
    println(total)
}

fun shouldPlay(it: Pair<Type, Outcome>): Type {
    return when (it.second) {
        Outcome.Lose -> {
            val i = (it.first.ordinal - 1) % 3
            if (i < 0) i + 3 else i
        }
        Outcome.Draw -> it.first.ordinal
        Outcome.Win -> (it.first.ordinal + 1) % 3
    }.let { Type.values()[it] }
}

private fun String.toType(): Type = when (this) {
    "A" -> Type.Rock
    "B" -> Type.Paper
    "C" -> Type.Scissors
    else -> error(this)
}

enum class Outcome(val score: Int) {
    Win(6),
    Draw(3),
    Lose(0)
}

private fun String.toOutcome(): Outcome =
    when(this) {
        "X" -> Outcome.Lose
        "Y" -> Outcome.Draw
        "Z" -> Outcome.Win
        else -> error(this)

    }
