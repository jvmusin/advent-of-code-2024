import java.io.FileReader
import java.util.*
import kotlin.math.abs

class Day8 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNextLine()) {
                field.add(fin.nextLine())
            }
            val n = field.size
            val m = field[0].length

            val positions = hashMapOf<Char, MutableList<Pair<Int, Int>>>()
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (field[i][j] != '.') {
                        positions.computeIfAbsent(field[i][j]) { mutableListOf() }.add(Pair(i, j))
                    }
                }
            }

            fun inside(p: Pair<Int, Int>) = p.first in 0 until n && p.second in 0 until m
            val antennas = hashSetOf<Pair<Int, Int>>()
            for (e in positions.entries) {
                val values = e.value
                for (i in 0 until values.size) {
                    for (j in i + 1 until values.size) {
                        val x = values[i]
                        val y = values[j]
                        val d = (y.first - x.first) to (y.second - x.second)
                        val p1 = (y.first + d.first) to (y.second + d.second)
                        val p2 = (x.first - d.first) to (x.second - d.second)
                        if (inside(p1)) antennas += p1
                        if (inside(p2)) antennas += p2
                    }
                }
            }

            println(antennas.size)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun gcd(a: Int, b: Int): Int {
            if (b == 0) return a
            return gcd(b, a % b)
        }

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNextLine()) {
                field.add(fin.nextLine())
            }
            val n = field.size
            val m = field[0].length

            val positions = hashMapOf<Char, MutableList<Pair<Int, Int>>>()
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (field[i][j] != '.') {
                        positions.computeIfAbsent(field[i][j]) { mutableListOf() }.add(Pair(i, j))
                    }
                }
            }

            fun inside(p: Pair<Int, Int>) = p.first in 0 until n && p.second in 0 until m
            val antennas = hashSetOf<Pair<Int, Int>>()
            for (e in positions.entries) {
                val values = e.value
                for (i in 0 until values.size) {
                    for (j in i + 1 until values.size) {
                        val x = values[i]
                        val y = values[j]
                        val g = gcd(abs(y.first - x.first), abs(y.second - x.second))
                        val d = (y.first - x.first) / g to (y.second - x.second) / g
                        val p1 = x
                        var p1New = p1
                        while (inside(p1New)) {
                            antennas += p1New
                            p1New = (p1New.first + d.first) to (p1New.second + d.second)
                        }
                        p1New = p1
                        while (inside(p1New)) {
                            antennas += p1New
                            p1New = (p1New.first - d.first) to (p1New.second - d.second)
                        }
                    }
                }
            }

            println(antennas.size)
        }
    }
}

fun main() {
    Day8.Part1(sample = true).solve()
    Day8.Part1(sample = false).solve()
    Day8.Part2(sample = true).solve()
    Day8.Part2(sample = false).solve()
}
