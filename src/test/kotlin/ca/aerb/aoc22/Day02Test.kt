package ca.aerb.aoc22

import org.junit.jupiter.api.Test
import java.io.File

class Day02Test {
    enum class Type {
        Rock,
        Paper,
        Scissors
    }

    @Test
    fun part1() {

        val rules =
            File("2.txt").readLines().map {
                it.split(" ").let { (a, b) ->
                    a.toType() to b.toType()
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


    @Test
    fun part2() {
        val rules =
            File("2.txt").readLines().map {
                it.split(" ").let { (a, b) ->
                    a.toType() to b.toOutcome()
                }
            }

        val total = rules.map {
            val play = shouldPlay(it)
            println("$it $play")
            it.second.score + play.ordinal + 1
        }.sum()
        println(total)
    }

    private fun shouldPlay(it: Pair<Type, Outcome>): Type =
        when (it.second) {
            Outcome.Lose -> {
                val i = (it.first.ordinal - 1) % 3
                if (i < 0) i + 3 else i
            }
            Outcome.Draw -> it.first.ordinal
            Outcome.Win -> (it.first.ordinal + 1) % 3
        }.let { Type.values()[it] }


    enum class Outcome(val score: Int) {
        Win(6),
        Draw(3),
        Lose(0)
    }

    private fun String.toOutcome(): Outcome =
        when (this) {
            "X" -> Outcome.Lose
            "Y" -> Outcome.Draw
            "Z" -> Outcome.Win
            else -> error(this)

        }
}
