import java.io.File


fun main() {
    File("1.p1.txt").readText().split("\n\n")
        .map { group ->
            group.reader().readLines().sumOf { line -> line.toInt() }
        }
        .sorted()
        .takeLast(3)
        .sum()
        .also { println(it) }
}
