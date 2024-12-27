import java.io.FileReader
import java.util.*

class Day4 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        val dx = intArrayOf(0, -1, -1, -1, 0, 1, 1, 1)
        val dy = intArrayOf(1, 1, 0, -1, -1, -1, 0, 1)
        fun inside(x: Int, y: Int, n: Int, m: Int) = x in 0 until n && y in 0 until m
        val XMAS = "XMAS"

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            val n = field.size
            val m = field[0].length
            fun check(x0: Int, y0: Int, dir: Int): Boolean {
                for (i in 0 until XMAS.length) {
                    val x = x0 + i * dx[dir]
                    val y = y0 + i * dy[dir]
                    if (!inside(x, y, n, m) || field[x][y] != XMAS[i]) return false
                }
                return true
            }

            var ans = 0
            for (x in 0 until n) for (y in 0 until m) for (dir in dx.indices) if (check(x, y, dir)) ans++
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        val words = arrayOf("MAS", "SAM")

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            val n = field.size
            val m = field[0].length
            fun check(x0: Int, y0: Int, dx: Int, dy: Int): Boolean {
                var word = ""
                repeat(3) { t ->
                    word += field[x0 + dx * t][y0 + dy * t]
                }
                return word in words
            }

            var ans = 0
            for (x in 1 until n - 1)
                for (y in 1 until m - 1)
                    if (check(x - 1, y - 1, 1, 1) && check(x - 1, y + 1, 1, -1))
                        ans++
            println(ans)
        }
    }
}

fun main() {
    Day4.Part1(sample = true).solve()
    Day4.Part1(sample = false).solve()
    Day4.Part2(sample = true).solve()
    Day4.Part2(sample = false).solve()
}
