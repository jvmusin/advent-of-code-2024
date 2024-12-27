import java.io.FileReader
import java.util.*

class Day7 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            var cnt = 0L
            while (fin.hasNext()) {
                val s = fin.nextLine()
                val (needSum, nums) = s.split(": ")
                val needSumInt = needSum.toLong()
                val numsInts = nums.split(' ').map { it.toLong() }
                fun dfs(curSum: Long, at: Int): Boolean {
                    if (at == numsInts.size) return curSum == needSumInt
                    if (curSum > needSumInt) return false
                    val x = numsInts[at]
                    return dfs(curSum + x, at + 1) || dfs(curSum * x, at + 1)
                }
                if (dfs(numsInts[0], 1)) cnt += needSumInt
            }
            println(cnt)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            var cnt = 0L
            while (fin.hasNext()) {
                val s = fin.nextLine()
                val (needSum, nums) = s.split(": ")
                val needSumInt = needSum.toLong()
                val numsInts = nums.split(' ').map { it.toLong() }
                fun dfs(curSum: Long, at: Int): Boolean {
                    if (at == numsInts.size) return curSum == needSumInt
                    if (curSum > needSumInt) return false
                    val x = numsInts[at]
                    if (curSum != 0L && dfs(curSum * x, at + 1)) return true
                    return dfs(curSum + x, at + 1) || dfs(if (curSum == 0L) x else (curSum.toString() + x).toLong(), at + 1)
                }
                if (dfs(0, 0)) {
                    cnt += needSumInt
                }
            }
            println(cnt)
        }
    }
}

fun main() {
    Day7.Part1(sample = true).solve()
    Day7.Part1(sample = false).solve()
    Day7.Part2(sample = true).solve()
    Day7.Part2(sample = false).solve()
}
