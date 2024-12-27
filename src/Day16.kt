import java.awt.image.BufferedImage
import java.io.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool
import javax.imageio.ImageIO

class Day16 {
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

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) {
                field += fin.nextLine()
            }
            val n = field.size
            val m = field[0].length
            val start = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == 'S' }
            val end = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == 'E' }
            val dist = Array(n) { Array(m) { IntArray(4) { -1 } } }
            val q = ArrayDeque<Pair<Pos, Int>>()
            dist[start.x][start.y][0] = 0
            q.add(start to 0)
            while (!q.isEmpty()) {
                val (cur, dir) = q.poll()
                for (i in 0 until 4) {
                    val newPos = cur + Pos(dx[i], dy[i])
                    val inside = newPos.x in 0 until n && newPos.y in 0 until m
                    if (!inside || field[newPos.x][newPos.y] == '#') continue
                    val newScore = dist[cur.x][cur.y][dir] + 1 + if (dir == i) 0 else 1000
                    if (dist[newPos.x][newPos.y][i] == -1 || dist[newPos.x][newPos.y][i] > newScore) {
                        dist[newPos.x][newPos.y][i] = newScore
                        q.add(newPos to i)
                    }
                }
            }
            val ans = (0 until 4).map { dist[end.x][end.y][it] }.filter { it != -1 }.min()
            println(ans)
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

        fun findDistances(start: Pos, startDir: Int, field: List<String>): Array<Array<IntArray>> {
            val n = field.size
            val m = field[0].length
            val dist = Array(n) { Array(m) { IntArray(4) { -1 } } }
            val q = ArrayDeque<Pair<Pos, Int>>()
            dist[start.x][start.y][startDir] = 0
            q.add(start to startDir)
            while (!q.isEmpty()) {
                val (cur, dir) = q.poll()
                val newPos = cur + Pos(dx[dir], dy[dir])
                val inside = newPos.x in 0 until n && newPos.y in 0 until m
                if (inside && field[newPos.x][newPos.y] != '#') {
                    val newScore = dist[cur.x][cur.y][dir] + 1
                    if (dist[newPos.x][newPos.y][dir] == -1 || dist[newPos.x][newPos.y][dir] > newScore) {
                        dist[newPos.x][newPos.y][dir] = newScore
                        q.add(newPos to dir)
                    }
                }
                for (newDir in 0 until 4) {
                    if (newDir == dir) continue
                    val newScore = dist[cur.x][cur.y][dir] + 1000
                    if (dist[cur.x][cur.y][newDir] == -1 || dist[cur.x][cur.y][newDir] > newScore) {
                        dist[cur.x][cur.y][newDir] = newScore
                        q.add(cur to newDir)
                    }
                }
            }
            return dist
        }

        fun solve() {
            val field = mutableListOf<String>()
            while (fin.hasNext()) {
                field += fin.nextLine()
            }
            val n = field.size
            val m = field[0].length
            val start = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == 'S' }
            val end = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == 'E' }
            val distFromStart = findDistances(start, 0, field)
            val distsFromEnd = (0 until 4).map { findDistances(end, it, field) }
            val minDist = (0 until 4).map { distFromStart[end.x][end.y][it] }.filter { it != -1 }.min()
            var ans = 0
            for (i in 0 until n) {
                for (j in 0 until m) {
                    var any = false
                    for (midDir in 0 until 4) {
                        val d1 = distFromStart[i][j][midDir]
                        if (d1 == -1) continue
                        for (endDir in 0 until 4) {
                            val d2 = distsFromEnd[endDir][i][j][midDir xor 2]
                            if (d2 == -1) continue
                            if (d1 + d2 != minDist) continue
                            any = true
                        }
                    }
                    if (any) {
                        ans++
                    }
                }
            }
            println(ans)
        }
    }
}

fun main() {
    Day16.Part1(sample = true).solve()
    Day16.Part1(sample = false).solve()
    Day16.Part2(sample = true).solve()
    Day16.Part2(sample = false).solve()
}
