import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayDeque

class Day18 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int)

        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)

        fun solve() {
            val positions = mutableListOf<Pos>()
            while (fin.hasNext()) {
                val s = fin.nextLine().split(',').map { it.toInt() }
                positions += Pos(s[0], s[1])
            }
            while (positions.size > 1024) positions.removeLast()
            val n = 71

            val dist = Array(n) { IntArray(n) { -1 } }
            val q = ArrayDeque<Pos>()
            q.add(Pos(0, 0))
            dist[0][0] = 0
            while (q.isNotEmpty()) {
                val (x, y) = q.removeFirst()
                for (d in 0 until 4) {
                    val nx = x + dx[d]
                    val ny = y + dy[d]
                    val inside = nx in 0 until n && ny in 0 until n
                    if (!inside || Pos(nx, ny) in positions || dist[nx][ny] != -1) continue
                    dist[nx][ny] = dist[x][y] + 1
                    q += Pos(nx, ny)
                }
            }
            println(dist[n - 1][n - 1])
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int)

        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(1, 0, -1, 0)

        fun solve() {
            val positions = mutableListOf<Pos>()
            while (fin.hasNext()) {
                val s = fin.nextLine().split(',').map { it.toInt() }
                positions += Pos(s[0], s[1])
            }

            for (i in positions.indices) {
                val poss = positions.take(i)
                val n = 71

                val dist = Array(n) { IntArray(n) { -1 } }
                val q = ArrayDeque<Pos>()
                q.add(Pos(0, 0))
                dist[0][0] = 0
                while (q.isNotEmpty()) {
                    val (x, y) = q.removeFirst()
                    for (d in 0 until 4) {
                        val nx = x + dx[d]
                        val ny = y + dy[d]
                        val inside = nx in 0 until n && ny in 0 until n
                        if (!inside || Pos(nx, ny) in poss || dist[nx][ny] != -1) continue
                        dist[nx][ny] = dist[x][y] + 1
                        q += Pos(nx, ny)
                    }
                }
                if (dist[n-1][n-1]==-1) {
                    println("${poss.last().x}, ${poss.last().y}")
                }
//                if (dist[n - 1][n - 1] != -1) l = m else r = m
            }

//            println("${positions[r].x},${positions[r].y}")
        }
    }
}

fun main() {
    Day18.Part1(sample = true).solve()
    Day18.Part1(sample = false).solve()
//    Day18.Part2(sample = true).solve()
    Day18.Part2(sample = false).solve()
}
