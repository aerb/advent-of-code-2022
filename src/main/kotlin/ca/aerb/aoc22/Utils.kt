package ca.aerb.aoc22

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun <T> T.alsoPrintln(block: (T) -> Any? = { it }): T {
    println(block(this))
    return this
}

fun <T, C : Iterable<T>> C.onEachPrintln(f: (T) -> Any? = { it }): C =
    onEach { println(f(it)) }

fun IntRange.partiallyContains(other: IntRange): Boolean =
    other.first in first..last || other.last in first..last

fun IntRange.fullyContains(other: IntRange): Boolean =
    other.first >= first && other.last <= last
