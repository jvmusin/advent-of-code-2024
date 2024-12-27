import java.io.FileReader
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayDeque
import kotlin.collections.HashMap

class Day23 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))

        @JvmStatic
        fun main(args: Array<String>) {
            Part1(sample = true).solve()
            Part1(sample = false).solve()
            Part2(sample = true).solve()
            Part2(sample = false).solve()
        }
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val r: Int, val c: Int) {
            operator fun plus(p: Pos) = Pos(r + p.r, c + p.c)
        }

        data class Graph(val a: List<String>) {
            val n = a.size
            val m = a[0].length

            fun find(c: Char): Pos? {
                for (i in a.indices) {
                    for (j in a[i].indices) {
                        if (a[i][j] == c) {
                            return Pos(i, j)
                        }
                    }
                }
                return null
            }

            fun inside(pos: Pos) = pos.r in 0 until n && pos.c in 0 until m
            val dx4 = intArrayOf(0, 1, 0, -1)
            val dy4 = intArrayOf(1, 0, -1, 0)
            fun neighbours4(p: Pos) = dx4.indices.map { p + Pos(dx4[it], dy4[it]) }.filter(::inside)

            fun bfs(from: Pos, allowed: (Char) -> Boolean): Array<IntArray> {
                val dist = Array(n) { IntArray(m) { -1 } }
                dist[from.r][from.c] = 0
                val q = ArrayDeque<Pos>()
                q.add(from)
                while (q.isNotEmpty()) {
                    val curPos = q.removeFirst()
                    for (nextPos in neighbours4(curPos)) {
                        if (dist[nextPos.r][nextPos.c] == -1 && allowed(a[nextPos.r][nextPos.c])) {
                            dist[nextPos.r][nextPos.c] = dist[curPos.r][curPos.c] + 1
                            q.add(nextPos)
                        }
                    }
                }
                return dist
            }
        }

        fun solve() {
            val g = hashMapOf<String, MutableSet<String>>()
            while (fin.hasNext()) {
                val (x, y) = fin.next().split('-')
                g.computeIfAbsent(x) { mutableSetOf() }.add(y)
                g.computeIfAbsent(y) { mutableSetOf() }.add(x)
            }
            val keys = g.keys.toList()
            var ans = 0
            for (i in keys.indices) {
                for (j in i + 1 until keys.size) {
                    for (k in j + 1 until keys.size) {
                        val x = keys[i]
                        val y = keys[j]
                        val z = keys[k]
                        if (x.startsWith('t') || y.startsWith('t') || z.startsWith('t')) {
                            if (x in g[y]!! && x in g[z]!! && y in g[z]!!) {
                                ans++
                            }
                        }
                    }
                }
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            val g = hashMapOf<String, MutableSet<String>>()
            while (fin.hasNext()) {
                val (x, y) = fin.next().split('-')
                g.computeIfAbsent(x) { sortedSetOf() }.add(y)
                g.computeIfAbsent(y) { sortedSetOf() }.add(x)
            }
            val keys = g.keys.toList().sorted()
            var ans = setOf<String>()
            val curSet = sortedSetOf<String>()
            fun dfs(at: Int) {
                if (curSet.size > ans.size) ans = curSet.toSet()
                if (at == keys.size) return
                val x = keys[at]
                val ok = curSet.all { y -> x in g[y]!! }
                if (ok) {
                    curSet += x
                    dfs(at + 1)
                    curSet -= x
                }
                dfs(at + 1)
            }
            dfs(0)
            println(ans.joinToString(","))
        }
    }
}
