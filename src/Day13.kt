import java.io.FileReader
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool

class Day13 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun times(k: Int) = Pos(x * k, y * k)
        }

        fun readPos(): Pos {
            val line = fin.nextLine()
            require('-' !in line)
            val s = line.split(": ")[1].split(", ")
            val dx = s[0].substring(2).toInt()
            val dy = s[1].substring(2).toInt()
            return Pos(dx, dy)
        }

        fun solve() {
            var ans = 0
            var first = true
            while (fin.hasNext()) {
                if (!first) require(fin.nextLine() == "")
                else first = false
                val buttonA = readPos()
                val buttonB = readPos()
                val prize = readPos()
                var min = Int.MAX_VALUE
                for (i in 0..100) {
                    for (j in 0..100) {
                        if (buttonA * i + buttonB * j == prize) {
                            min = minOf(i * 3 + j, min)
                        }
                    }
                }
                if (min != Int.MAX_VALUE) ans += min
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Long, val y: Long) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun minus(p: Pos) = Pos(x - p.x, y - p.y)
            operator fun times(k: Long) = Pos(x * k, y * k)
        }

        fun readPos(): Pos {
            val line = fin.nextLine()
            require('-' !in line)
            val s = line.split(": ")[1].split(", ")
            val dx = s[0].substring(2).toLong()
            val dy = s[1].substring(2).toLong()
            return Pos(dx, dy)
        }

        fun solve() {
            var first = true
            val inputs = mutableListOf<Array<Pos>>()
            while (fin.hasNext()) {
                if (!first) require(fin.nextLine() == "")
                else first = false
                val buttonA = readPos()
                val buttonB = readPos()
                val prize = readPos().let { Pos(it.x + 10000000000000L, it.y + 10000000000000L) }
                inputs += arrayOf(buttonA, buttonB, prize)
            }

            fun findZeroAt(a: Pos, b: Pos, prize: Pos, extract: Pos.() -> Long): Pair<Long, Long>? {
                val foundRems = hashSetOf<Long>()
                var iters = 0L
                var zeroAt = -1L
                while (true) {
                    val curA = a * iters
                    val rem = (prize.extract() - curA.extract()) % b.extract()
                    if (!foundRems.add(rem)) {
                        break
                    }
                    if (rem == 0L) zeroAt = iters

                    iters++
                }
                if (zeroAt == -1L) return null
                return foundRems.size.toLong() to zeroAt
            }

            fun find(a: Pos, b: Pos, prize: Pos): Long? {
                val zeroAtPair = findZeroAt(a, b, prize) { x } ?: return null
                findZeroAt(b, a, prize) { x } ?: return null
                findZeroAt(a, b, prize) { y } ?: return null
                findZeroAt(b, a, prize) { y } ?: return null
                val (cycleLen, zeroAt) = zeroAtPair

                var it = 0L
                var res = Long.MAX_VALUE
                while (true) {
                    val takeA = zeroAt + cycleLen * it
                    val curA = a * takeA
                    if (curA.x > prize.x || curA.y > prize.y) return if (res == Long.MAX_VALUE) null else res
                    val rem = prize - curA
                    require(rem.x % b.x == 0L)
//                    require(rem.y % b.y == 0L)
                    val takeB = rem.x / b.x
                    if (curA + b * takeB == prize) {
                        res = minOf(res, 3 * takeA + takeB)
                    }
                    it++
                }
            }

            val start = LocalDateTime.now()
            val pool = ForkJoinPool(ForkJoinPool.getCommonPoolParallelism() - 1)
            val answer = inputs.mapIndexed { i, inp ->
                val (a, b, prize) = inp
                pool.submit(Callable {
                    val curTime = Duration.between(
                        start,
                        LocalDateTime.now()
                    )
                    System.err.println("($curTime) Running on input ${i + 1}/${inputs.size}")
                    find(a, b, prize).also {
                        val curTime = Duration.between(start, LocalDateTime.now())
                        System.err.println("($curTime) Input ${i + 1} finished with result $it")
                    }
                })
            }.sumOf { it.join() ?: 0L }
            println(answer)
        }
    }
}

fun main() {
    Day13.Part1(sample = true).solve()
    Day13.Part1(sample = false).solve()
    Day13.Part2(sample = true).solve()
    Day13.Part2(sample = false).solve()
}
