import java.io.FileReader
import java.math.BigInteger
import java.util.*

class Day10 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)

        fun solve() {
            val a = mutableListOf<String>()
            while (fin.hasNext()) {
                a += fin.next()
            }
            val n = a.size
            val m = a[0].length
            val ends = Array(n) { i -> Array(m) { mutableSetOf<Pair<Int, Int>>() } }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (a[i][j] == '9') {
                        ends[i][j].add(i to j)
                    }
                }
            }
            for (x in 8 downTo 0) {
                for (i in 0 until n) {
                    for (j in 0 until m) {
                        if (a[i][j].digitToInt() == x) {
                            for (d in 0 until 4) {
                                val ni = i + dx[d]
                                val nj = j + dy[d]
                                if (!inside(ni, nj, n, m)) continue
                                if (a[ni][nj].digitToInt() != a[i][j].digitToInt() + 1) continue
                                ends[i][j].addAll(ends[ni][nj])
                            }
                        }
                    }
                }
            }
            var ans = 0
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (a[i][j] == '0') {
                        ans += ends[i][j].size
                    }
                }
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)

        fun solve() {
            val a = mutableListOf<String>()
            while (fin.hasNext()) {
                a += fin.next()
            }
            val n = a.size
            val m = a[0].length
            val ends = Array(n) { i -> Array(m) { 0 } }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (a[i][j] == '9') {
                        ends[i][j] = 1
                    }
                }
            }
            for (x in 8 downTo 0) {
                for (i in 0 until n) {
                    for (j in 0 until m) {
                        if (a[i][j].digitToInt() == x) {
                            for (d in 0 until 4) {
                                val ni = i + dx[d]
                                val nj = j + dy[d]
                                if (!inside(ni, nj, n, m)) continue
                                if (a[ni][nj].digitToInt() != a[i][j].digitToInt() + 1) continue
                                ends[i][j] += ends[ni][nj]
                            }
                        }
                    }
                }
            }
            var ans = 0L
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (a[i][j] == '0') {
                        ans += ends[i][j]
                    }
                }
            }
            println(ans)
        }
    }
}

fun main() {
    Day10.Part1(sample = true).solve()
    Day10.Part1(sample = false).solve()
    Day10.Part2(sample = true).solve()
    Day10.Part2(sample = false).solve()
}
