import java.io.FileReader
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

class Day2 {
    class Part1 {
        fun isSorted(a: List<Int>): Boolean {
            return a.sorted() == a || a.sorted().reversed() == a
        }

        fun solve(sample: Boolean) {
            val fin = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))

            var cnt = 0
            while (fin.hasNext()) {
                val a = fin.nextLine().split(' ').map { it.toInt() }
                if (isSorted(a)) {
                    var good = true
                    for (i in 1 until a.size) {
                        val diff = abs(a[i] - a[i - 1])
                        good = good && diff in 1..3
                    }
                    if (good) cnt++
                }
            }
            println(cnt)
        }
    }

    class Part2 {
        fun check(a: List<Int>, maxError: Int): Boolean {
            var errors = 0
            var last = a[0]
            var sign = 0
            for (i in 1 until a.size) {
                val diff0 = a[i] - last
                val diff = abs(diff0)
                val ok = (sign == 0 || (sign == diff0.sign)) && diff in 1..3
                if (!ok) errors++
                else {
                    last = a[i]
                    if (sign == 0) sign = diff0.sign
                }
            }
            return errors <= maxError
        }

        fun solve(sample: Boolean) {
            val fin = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))

            var cnt = 0
            while (fin.hasNext()) {
                val a = fin.nextLine().split(' ').map { it.toInt() }
                val check1 = check(a, maxError = 1)
                val check2 = check(a.drop(1), maxError = 0)
                val check3 = check(a.take(1) + a.drop(2), maxError = 0)
                cnt += if (check1 || check2 || check3) 1 else 0
            }
            println(cnt)
        }
    }
}

fun main() {
    Day2.Part2().solve(sample = false)
}
