import java.io.FileReader
import java.util.*

class Day22 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))

        @JvmStatic
        fun main(args: Array<String>) {
            Part1(sample = true).solve()
            Part1(sample = false).solve()
            Part2(sample = true).solve()
            Part2(sample = false).solve()
        }
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val mod = 16777216

            fun process(x: Long, times: Int): Long {
                var result = x
                repeat(times) {
                    result = result xor (result * 64)
                    result %= mod

                    result = result xor (result / 32)
                    result %= mod

                    result = result xor (result * 2048)
                    result %= mod
                }
                return result
            }

            var ans = 0L
            while (fin.hasNext()) {
                ans += process(fin.nextLong(), 2000)
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val mod = 16777216

            fun generateHistory(x: Long, times: Int): List<Long> {
                var result = x
                val history = mutableListOf<Long>()
                history += result % 10
                repeat(times) {
                    result = result xor (result * 64)
                    result %= mod

                    result = result xor (result / 32)
                    result %= mod

                    result = result xor (result * 2048)
                    result %= mod

                    history += result % 10
                }
                return history
            }

            val allHistory = mutableListOf<List<Long>>()
            while (fin.hasNext()) {
                allHistory += generateHistory(fin.nextLong(), 2000)
            }

            fun changes(history: List<Long>): MutableList<Long> {
                val res = mutableListOf<Long>()
                for (i in history.indices.drop(1)) {
                    res += history[i] - history[i - 1]
                }
                return res
            }

            fun getListToAns(history: List<Long>): HashMap<List<Long>, Long> {
                val changes = changes(history)
                val ans = hashMapOf<List<Long>, Long>()
                for (i in 0 until changes.size - 3) {
                    val c = changes.subList(i, i + 4)
                    require(c.size == 4)
                    if (c !in ans) ans[c] = history[i + 4]
                }
                return ans
            }

            val allListToAns = allHistory.map { getListToAns(it) }

            var ans = 0L
            for (seq in allListToAns.flatMap { it.keys }.distinct()) {
                var curAns = 0L
                for (h in allListToAns) {
                    h[seq]?.let { curAns += it }
                }
                if (curAns > ans) ans = curAns
            }
            println(ans)
        }
    }
}
