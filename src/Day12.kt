import java.io.FileReader
import java.util.*

class Day12 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            val n = field.size
            val m = field[0].length

            val used = Array(n) { BooleanArray(m) }
            fun collect(x: Int, y: Int, cells: MutableSet<Pair<Int, Int>>) {
                if (used[x][y]) return
                used[x][y] = true
                cells += x to y
                for (d in 0 until 4) {
                    val nx = x + dx[d]
                    val ny = y + dy[d]
                    if (inside(nx, ny, n, m) && field[nx][ny] == field[x][y]) {
                        collect(nx, ny, cells)
                    }
                }
            }

            fun getPerimeter(cells: Set<Pair<Int, Int>>): Int {
                var perimeter = 0
                for (c in cells) {
                    val (x, y) = c
                    for (d in 0 until 4) {
                        val nx = x + dx[d]
                        val ny = y + dy[d]
                        if (!inside(nx, ny, n, m) || field[nx][ny] != field[x][y]) perimeter++
                    }
                }
                return perimeter
            }

            var ans = 0L
            for (i in 0 until n) {
                for (j in 0 until m) {
                    val cells = mutableSetOf<Pair<Int, Int>>()
                    collect(i, j, cells)
                    val area = cells.size
                    val perimeter = getPerimeter(cells)
                    ans += area * perimeter
                }
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)
        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.next()
            val n = field.size
            val m = field[0].length

            val used = Array(n) { BooleanArray(m) }
            fun collect(x: Int, y: Int, cells: MutableSet<Pair<Int, Int>>) {
                if (used[x][y]) return
                used[x][y] = true
                cells += x to y
                for (d in 0 until 4) {
                    val nx = x + dx[d]
                    val ny = y + dy[d]
                    if (inside(nx, ny, n, m) && field[nx][ny] == field[x][y]) {
                        collect(nx, ny, cells)
                    }
                }
            }

            fun getSidesCount(cells: Set<Pair<Int, Int>>): Int {
                val edges = mutableSetOf<Pair<Pair<Int, Int>, Int>>() // ((x, y), dir)
                for (c in cells) {
                    val (x, y) = c
                    for (d in 0 until 4) {
                        val nx = x + dx[d]
                        val ny = y + dy[d]
                        if (!inside(nx, ny, n, m) || field[nx][ny] != field[x][y]) edges += (x to y) to d
                    }
                }
                var sides = 0
                while (edges.isNotEmpty()) {
                    val e = edges.first()
                    edges.remove(e)
                    sides++
                    val (p, d) = e
                    val (x, y) = p

                    for (nd in intArrayOf((d - 1 + 4) % 4, (d + 1) % 4)) {
                        var nx = x
                        var ny = y
                        while (true) {
                            nx += dx[nd]
                            ny += dy[nd]
                            if (!edges.remove((nx to ny) to d)) break
                        }
                    }
                }

                return sides
            }

            var ans = 0L
            for (i in 0 until n) {
                for (j in 0 until m) {
                    val cells = mutableSetOf<Pair<Int, Int>>()
                    collect(i, j, cells)
                    val area = cells.size
                    val sides = getSidesCount(cells)
                    ans += area * sides
                }
            }
            println(ans)
        }
    }
}

fun main() {
    Day12.Part1(sample = true).solve()
    Day12.Part1(sample = false).solve()
    Day12.Part2(sample = true).solve()
    Day12.Part2(sample = false).solve()
}
