import java.io.FileReader
import java.util.*
import kotlin.math.abs

class Day1 {
    class Part1 {
        fun solve() {
            val fin = Scanner(FileReader("input.txt"))

            val a = mutableListOf<Int>()
            val b = mutableListOf<Int>()
            while (fin.hasNext()) {
                val x = fin.nextInt()
                val y = fin.nextInt()
                a += x
                b += y
            }
            a.sort()
            b.sort()
            var sum = 0L
            for (i in 0 until a.size) sum += abs(a[i] - b[i])
            println(sum)
        }
    }

    class Part2 {
        fun solve() {
            val fin = Scanner(FileReader("input.txt"))

            val a = mutableListOf<Int>()
            val appearances = hashMapOf<Int, Int>()
            while (fin.hasNext()) {
                val x = fin.nextInt()
                val y = fin.nextInt()
                a += x
                appearances.merge(y, 1, Int::plus)
            }
            var sum = 0L
            for (i in 0 until a.size) sum += a[i].toLong() * (appearances[a[i]] ?: 0)
            println(sum)
        }
    }
}

fun main() {
    Day1.Part2().solve()
}
