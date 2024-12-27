import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs

class Day20 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun times(k: Int) = Pos(x * k, y * k)
        }

        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)
        fun neighbours(pos: Pos, n: Int, m: Int) =
            (0 until 4).map { d -> pos + Pos(dx[d], dy[d]) }.filter { inside(it.x, it.y, n, m) }


        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.nextLine()
            val n = field.size
            val m = field[0].length

            fun find(c: Char): Pos {
                for (i in 0 until n) for (j in 0 until m) if (field[i][j] == c) return Pos(i, j)
                error("not found")
            }

            val start = find('S')
            val end = find('E')

            fun findPath(from: Pos): Array<IntArray> {
                val dist = Array(n) { IntArray(m) { -1 } }
                dist[from.x][from.y] = 0
                val q = ArrayDeque<Pos>()
                q.add(from)
                while (q.isNotEmpty()) {
                    val p = q.removeFirst()
                    for ((nx, ny) in neighbours(p, n, m)) {
                        if (dist[nx][ny] == -1 && field[nx][ny] != '#') {
                            dist[nx][ny] = dist[p.x][p.y] + 1
                            q += Pos(nx, ny)
                        }
                    }
                }
                return dist
            }

            val fromStart = findPath(start)
            val fromEnd = findPath(end)
            val initialLen = fromStart[end.x][end.y]
            var cnt = 0
            for (x0 in 0 until n) {
                for (y0 in 0 until m) {
                    if (field[x0][y0] != '#') {
                        for (d in 0 until 4) {
                            val x1 = x0 + dx[d]
                            val y1 = y0 + dy[d]
                            if (inside(x1, y1, n, m) && field[x1][y1] == '#') {
                                val x2 = x1 + dx[d]
                                val y2 = y1 + dy[d]
                                if (inside(x2, y2, n, m) && field[x2][y2] != '#') {
                                    val dist1 = fromStart[x0][y0]
                                    val dist2 = fromEnd[x2][y2]
                                    val total = dist1 + dist2 + 2
                                    if (dist1 != -1 && dist2 != -1 && initialLen - total >= 100) {
                                        cnt++
                                    }
                                }
                            }
                        }
                    }
                }
            }
            println(cnt)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun times(k: Int) = Pos(x * k, y * k)
        }

        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)
        fun inside(x: Int, n: Int) = x in 0 until n
        fun inside(x: Int, y: Int, n: Int, m: Int) = inside(x, n) && inside(y, m)
        fun neighbours(pos: Pos, n: Int, m: Int) =
            (0 until 4).map { d -> pos + Pos(dx[d], dy[d]) }.filter { inside(it.x, it.y, n, m) }


        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) field += fin.nextLine()
            val n = field.size
            val m = field[0].length

            fun find(c: Char): Pos {
                for (i in 0 until n) for (j in 0 until m) if (field[i][j] == c) return Pos(i, j)
                error("not found")
            }

            val start = find('S')
            val end = find('E')

            fun findPath(from: Pos): Array<IntArray> {
                val dist = Array(n) { IntArray(m) { -1 } }
                dist[from.x][from.y] = 0
                val q = ArrayDeque<Pos>()
                q.add(from)
                while (q.isNotEmpty()) {
                    val p = q.removeFirst()
                    for ((nx, ny) in neighbours(p, n, m)) {
                        if (dist[nx][ny] == -1 && field[nx][ny] != '#') {
                            dist[nx][ny] = dist[p.x][p.y] + 1
                            q += Pos(nx, ny)
                        }
                    }
                }
                return dist
            }

            val fromStart = findPath(start)
            val fromEnd = findPath(end)
            val initialLen = fromStart[end.x][end.y]
            var cnt = 0
            for (x0 in 0 until n) {
                for (y0 in 0 until m) {
                    for (x1 in 0 until n) {
                        for (y1 in 0 until m) {
                            val dCheat = abs(x1 - x0) + abs(y1 - y0)
                            if (dCheat <= 20) {
                                val d1 = fromStart[x0][y0]
                                val d2 = fromEnd[x1][y1]
                                if (d1 != -1 && d2 != -1 && initialLen - (d1 + d2 + dCheat) >= 100) {
                                    cnt++
                                }
                            }
                        }
                    }
                }
            }
            println(cnt)
        }
    }
}

fun main() {
    Day20.Part1(sample = true).solve()
    Day20.Part1(sample = false).solve()
    Day20.Part2(sample = true).solve()
    Day20.Part2(sample = false).solve()
}
