import java.io.FileReader
import java.util.*

class Day19 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val designs = fin.nextLine().split(", ")
            fin.nextLine()

            fun isPossible(s: String): Boolean {
                val dp = BooleanArray(s.length + 1)
                dp[0] = true
                for (i in 0 until s.length) {
                    if (!dp[i]) continue
                    for (p in designs) {
                        if (s.substring(i).startsWith(p)) {
                            dp[i + p.length] = true
                        }
                    }
                }
                return dp[s.length]
            }

            var cnt = 0
            while (fin.hasNext()) if (isPossible(fin.nextLine())) cnt++
            println(cnt)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val designs = fin.nextLine().split(", ")
            fin.nextLine()

            fun isPossible(s: String): Long {
                val dp = LongArray(s.length + 1)
                dp[0] = 1
                for (i in 0 until s.length) {
                    for (p in designs) {
                        if (s.substring(i).startsWith(p)) {
                            dp[i + p.length] += dp[i]
                        }
                    }
                }
                return dp[s.length]
            }

            var cnt = 0L
            while (fin.hasNext()) cnt += isPossible(fin.nextLine())
            println(cnt)
        }
    }
}

fun main() {
    Day19.Part1(sample = true).solve()
    Day19.Part1(sample = false).solve()
    Day19.Part2(sample = true).solve()
    Day19.Part2(sample = false).solve()
}
