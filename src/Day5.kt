import java.io.FileReader
import java.util.*

class Day5 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)
        fun solve() {
            val rules = mutableListOf<Pair<Int, Int>>()
            var s = ""
            while (true) {
                s = fin.nextLine()
                if (s == "") break
                rules += s.split('|').let { (x, y) -> x.toInt() to y.toInt() }
            }

            var sum = 0
            while (fin.hasNext()) {
                val numbers = fin.nextLine().split(',').map { it.toInt() }
                val have = hashSetOf<Int>()
                var fail = false
                for (x in numbers) {
                    for (r in rules) {
                        if (x == r.first && r.second in have) {
                            fail = true
                        }
                        have += x
                    }
                }
                if (!fail) sum += numbers[numbers.size / 2]
            }

            println(sum)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val rules = mutableListOf<Pair<Int, Int>>()
            var s = ""
            while (true) {
                s = fin.nextLine()
                if (s == "") break
                rules += s.split('|').let { (x, y) -> x.toInt() to y.toInt() }
            }

            var sum = 0
            while (fin.hasNext()) {
                val numbers = fin.nextLine().split(',').map { it.toInt() }
                val have = hashSetOf<Int>()
                var fail = false
                for (x in numbers) {
                    for (r in rules) {
                        if (x == r.first && r.second in have) {
                            fail = true
                        }
                        have += x
                    }
                }
                if (fail) {
                    // incorrectly ordered, try to reorder
                    var numbersLeft = numbers.toMutableList()
                    val requirements = hashMapOf<Int, Int>()
                    for (r in rules) {
                        if (r.first in numbersLeft && r.second in numbersLeft) {
                            requirements.merge(r.second, 1, Int::plus)
                        }
                    }
                    var seq = mutableListOf<Int>()
                    while (numbersLeft.isNotEmpty()) {
                        var any = false
                        for (x in numbersLeft) {
                            if (requirements.getOrDefault(x, 0) == 0) {
                                any = true
                                seq.add(x)
                                numbersLeft.remove(x)
                                for (r in rules) {
                                    if (r.first == x) {
                                        requirements.merge(r.second, -1, Int::plus)
                                    }
                                }
                                break
                            }
                        }
                        if (!any) break
                    }
                    if (numbersLeft.isEmpty()) {
                        sum += seq[seq.size / 2]
                    }
                }
            }

            println(sum)
        }
    }
}

fun main() {
    Day5.Part1(sample = true).solve()
    Day5.Part1(sample = false).solve()
    Day5.Part2(sample = true).solve()
    Day5.Part2(sample = false).solve()
}
