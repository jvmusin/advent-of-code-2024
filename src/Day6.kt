import java.io.FileReader
import java.util.*

class Day6 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, y: Int, n: Int, m: Int) = x in 0 until n && y in 0 until m

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            val n = field.size
            val m = field[0].length
            val start = (0 until n).flatMap { i -> (0 until m).map { j -> i to j } }.single { (i, j) -> field[i][j] == '^' }
            var d = 3
            var x = start.first
            var y = start.second
            val used = hashSetOf<Pair<Int, Int>>()
            while (true) {
                used += x to y
                while (true) {
                    val x1 = x + dx[d]
                    val y1 = y + dy[d]
                    if (!inside(x1, y1, n, m)) {
                        println(used.size)
                        return
                    }
                    if (field[x1][y1] == '#') {
                        d++
                        d %= 4
                    } else {
                        x = x1
                        y = y1
                        break
                    }
                }
            }
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, y: Int, n: Int, m: Int) = x in 0 until n && y in 0 until m

        fun solve0(field: List<String>): Int {
            val n = field.size
            val m = field[0].length
            val start = (0 until n).flatMap { i -> (0 until m).map { j -> i to j } }.single { (i, j) -> field[i][j] == '^' }
            var d = 3
            var x = start.first
            var y = start.second
            data class Pos(val x: Int, val y: Int, val d: Int)
            val used = hashSetOf<Pos>()
            while (true) {
                if (!used.add(Pos(x, y, d))) return -1
                used += Pos(x, y, d)
                while (true) {
                    val x1 = x + dx[d]
                    val y1 = y + dy[d]
                    if (!inside(x1, y1, n, m)) {
                        return used.size
                    }
                    if (field[x1][y1] == '#') {
                        d++
                        d %= 4
                    } else {
                        x = x1
                        y = y1
                        break
                    }
                }
            }
        }

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            var ans = 0
            for (i in field.indices) {
                val row = field[i]
                for (j in row.indices) {
                    if (row[j] != '.') continue
                    val newRow = row.take(j) + '#' + row.drop(j + 1)
                    val newField = field.toMutableList().apply {
                        this[i] = newRow
                    }
                    if (solve0(newField) == -1) {
                        ans++
                    }
                }
            }
            println(ans)
        }
    }
}

fun main() {
    Day6.Part1(sample = true).solve()
    Day6.Part1(sample = false).solve()
    Day6.Part2(sample = true).solve()
    Day6.Part2(sample = false).solve()
}
