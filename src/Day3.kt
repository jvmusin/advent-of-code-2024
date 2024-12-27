import java.io.FileReader
import java.util.*

class Day3 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)
        fun solve() {
            var inp = ""
            while (fin.hasNext()) inp += fin.next() + " "
            val r = Regex("(mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\))")
            var enabled = true
            val res = r.findAll(inp).map { it.groupValues }.sumOf {
                if (it[0].startsWith("do")) {
                    enabled = it[0] == "do()"
                    0L
                } else {
                    val a = it[2]
                    val b = it[3]
                    var ok = true
                    ok = ok && (a == "0" || (a.length in 1..3 && a[0] != '0'))
                    ok = ok && (b == "0" || (b.length in 1..3 && b[0] != '0'))
                    if (ok && enabled) a.toLong() * b.toLong() else 0
                }
            }
            println(res)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
        }
    }
}

fun main() {
    Day3.Part1(sample = true).solve()
    Day3.Part1(sample = false).solve()
    Day3.Part2(sample = true).solve()
    Day3.Part2(sample = false).solve()
}
