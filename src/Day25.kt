import java.io.FileReader
import java.util.*

class Day25 {
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
        val n = 7
        val m = 5

        fun calc(a: List<String>): Pair<List<Int>, Int> {
            val res = mutableListOf<Int>()
            for (j in 0 until m) {
                var cnt = 0
                for (i in 0 until n) if (a[i][j] == '#') cnt++
                res += cnt - 1
            }
            return res to if (a[0][0] == '.') 0 else 1
        }

        fun solve() {
                val typeToLocks = Array(2) { mutableListOf<List<Int>>() }
            while (fin.hasNext()) {
                val curLock = mutableListOf<String>()
                repeat(n) {
                    curLock += fin.next()
                }
                val (lock, type) = calc(curLock)
                typeToLocks[type].add(lock)
            }
            var ans = 0
            for (lock1 in typeToLocks[0]) {
                for (lock2 in typeToLocks[1]) {
                    val sums = lock1.zip(lock2).map { (x,y) -> x + y }
                    if (sums.all { it < 6 }) ans++
                }
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
        }
    }
}
