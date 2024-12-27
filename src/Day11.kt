import java.io.FileReader
import java.math.BigInteger
import java.util.*

class Day11 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun nextStones(x: Long): List<Long> {
            if (x == 0L) return listOf(1)
            val s = x.toString()
            if (s.length % 2 == 0) {
                val a = s.take(s.length / 2).toLong()
                val b = s.drop(s.length / 2).toLong()
                return listOf(a, b)
            }
            return listOf(x * 2024)
        }

        fun solve() {
            var a = fin.nextLine().split(' ').map { it.toLong() }
            repeat(25) {
                a = a.flatMap { x -> nextStones(x) }
            }
            println(a.size)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        val cache = hashMapOf<Pair<BigInteger, Int>, BigInteger>() // (x, times to apply) -> number of stones
        fun nextStones(x: BigInteger, timesToApply: Int): BigInteger {
            val key = x to timesToApply
            cache[key]?.let { return it }

            val s = x.toString()
            val res = when {
                timesToApply == 0 -> BigInteger.ONE
                x == BigInteger.ZERO -> nextStones(BigInteger.ONE, timesToApply - 1)
                s.length % 2 == 0 -> {
                    val a = s.take(s.length / 2).toBigInteger()
                    val b = s.drop(s.length / 2).toBigInteger()
                    nextStones(a, timesToApply - 1) + nextStones(b, timesToApply - 1)
                }
                else -> nextStones(x * 2024.toBigInteger(), timesToApply - 1)
            }

            cache[key] = res
            return res
        }

        fun solve() {
            val a = fin.nextLine().split(' ').map { it.toBigInteger() }
            val ans = a.sumOf { nextStones(it, 75) }
            println(ans)
        }
    }
}

fun main() {
    Day11.Part1(sample = true).solve()
    Day11.Part1(sample = false).solve()
    Day11.Part2(sample = true).solve()
    Day11.Part2(sample = false).solve()
}
