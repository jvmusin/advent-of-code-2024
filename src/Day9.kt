import java.io.FileReader
import java.math.BigInteger
import java.util.*

class Day9 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun expand(a: List<Int>): MutableList<Int> {
            val res = mutableListOf<Int>()
            var id = 0
            var file = true
            for (x in a) {
                if (file) {
                    repeat(x) {
                        res += id
                    }
                    id++
                } else {
                    repeat(x) {
                        res += -1
                    }
                }
                file = !file
            }
            return res
        }

        fun solve() {
            val a = fin.next().map { it.digitToInt() }
            val expanded = expand(a)

            var emptyPos = 0
            for (lastFilePos in expanded.indices.reversed()) {
                if (expanded[lastFilePos] == -1) continue
                while (emptyPos <= lastFilePos && expanded[emptyPos] != -1) emptyPos++
                if (emptyPos > lastFilePos) break
                Collections.swap(expanded, emptyPos, lastFilePos)
            }

            var res = BigInteger.ZERO
            for (i in expanded.indices) {
                val id = expanded[i]
                if (id == -1) break
                res += id.toBigInteger() * i.toBigInteger()
            }
            println(res)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun expand(a: List<Int>): MutableList<Int> {
            val res = mutableListOf<Int>()
            var id = 0
            var file = true
            for (x in a) {
                if (file) {
                    repeat(x) {
                        res += id
                    }
                    id++
                } else {
                    repeat(x) {
                        res += -1
                    }
                }
                file = !file
            }
            return res
        }

        fun solve() {
            val a = fin.next().map { it.digitToInt() }
            val expanded = expand(a)

//            val filePositions = hashMapOf<Int, Pair<Int, Int>>()
//            for (id in expanded.max() downTo 0) {
//                val start = expanded.indexOfFirst { it == id }
//                val end = expanded.indexOfLast { it == id }
//                filePositions[id] = start to end
//            }
//
//            var firstEmpty = 0
//            while (firstEmpty < expanded.size) {
//
//            }

            for (id in expanded.max() downTo 0) {
                System.err.println("Working on id $id")
                val start = expanded.indexOfFirst { it == id }
                val end = expanded.indexOfLast { it == id }
                val len = end - start + 1

                var emptyStart = 0
                while (emptyStart <= start) {
                    while (emptyStart <= start && expanded[emptyStart] != -1) emptyStart++
                    if (emptyStart > start) break
                    var emptyEnd = emptyStart
                    while (expanded[emptyEnd + 1] == -1) emptyEnd++
                    val emptyLen = emptyEnd - emptyStart + 1
                    if (emptyLen >= len) {
                        repeat(len) { i ->
                            Collections.swap(expanded, start + i, emptyStart + i)
                        }
                        break
                    }
                    emptyStart = emptyEnd + 1
                }
            }

            var res = BigInteger.ZERO
            for (i in expanded.indices) {
                val id = expanded[i]
                if (id == -1) continue
                res += id.toBigInteger() * i.toBigInteger()
            }
            println(res)
        }
    }
}

fun main() {
    Day9.Part1(sample = true).solve()
    Day9.Part1(sample = false).solve()
    Day9.Part2(sample = true).solve()
    Day9.Part2(sample = false).solve()
}
