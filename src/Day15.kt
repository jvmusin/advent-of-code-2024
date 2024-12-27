import java.awt.image.BufferedImage
import java.io.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool
import javax.imageio.ImageIO

class Day15 {
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
        val dirs = ">v<^"

        val WALL = 1
        val BOX = 2
        val ROBOT = 3
        val EMPTY = 0

        fun solve() {
            val field = mutableListOf<IntArray>()
            while (true) {
                val s = fin.nextLine()
                if (s == "") break
                field.add(s.map { c ->
                    when (c) {
                        '#' -> WALL
                        'O' -> BOX
                        '@' -> ROBOT
                        '.' -> EMPTY
                        else -> error("Unknown symbol: $c")
                    }
                }.toIntArray())
            }
            val n = field.size
            val m = field[0].size
            var robotPos = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == ROBOT }

            fun move(dir: Int) {
                var onlyBoxes = true
                var anyBox = false
                var emptyPos = robotPos
                while (true) {
                    emptyPos += Pos(dx[dir], dy[dir])
                    if (field[emptyPos.x][emptyPos.y] == WALL) {
                        onlyBoxes = false
                        break
                    }
                    if (field[emptyPos.x][emptyPos.y] == BOX) {
                        anyBox = true
                        continue
                    }
                    if (field[emptyPos.x][emptyPos.y] == EMPTY) {
                        break
                    }
                    error("Unknown symbol: ${field[emptyPos.x][emptyPos.y]}")
                }

                if (!onlyBoxes) return

                field[robotPos.x][robotPos.y] = EMPTY
                robotPos += Pos(dx[dir], dy[dir])
                field[robotPos.x][robotPos.y] = ROBOT
                if (anyBox)
                    field[emptyPos.x][emptyPos.y] = BOX
            }

            while (fin.hasNext())
                for (move in fin.next()) {
                    val d = dirs.indexOf(move)
                    move(d)
                }
            var ans = 0
            for (r in 0 until n) {
                for (c in 0 until m) {
                    if (field[r][c] == BOX) ans += r * 100 + c
                }
            }
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
        val dirs = ">v<^"

        val WALL = '#'
        val BOX_0 = '['
        val BOX_1 = ']'
        val ROBOT = '@'
        val EMPTY = '.'

        fun solve() {
            val field = mutableListOf<CharArray>()
            while (true) {
                val s = fin.nextLine()
                if (s == "") break
                field.add(s.map { c ->
                    when (c) {
                        '#' -> listOf(WALL, WALL)
                        'O' -> listOf(BOX_0, BOX_1)
                        '@' -> listOf(ROBOT, EMPTY)
                        '.' -> listOf(EMPTY, EMPTY)
                        else -> error("Unknown symbol: $c")
                    }
                }.flatten().toCharArray())
            }
            val n = field.size
            val m = field[0].size
            var robotPos = (0 until n).flatMap { i -> (0 until m).map { j -> Pos(i, j) } }
                .single { field[it.x][it.y] == ROBOT }

            fun nei(p: Pos): Pos {
                if (field[p.x][p.y] == BOX_0) return Pos(p.x, p.y + 1)
                if (field[p.x][p.y] == BOX_1) return Pos(p.x, p.y - 1)
                error("Unknown symbol: ${field[p.x][p.y]}")
            }

            fun push(pos: Pos, d: Int): Boolean {
                val allDependencies = mutableListOf<Pos>()
                allDependencies += pos
                var i = 0
                while (i < allDependencies.size) {
                    val p = allDependencies[i]
                    if (p in allDependencies.take(i)) {
                        allDependencies.removeAt(i)
                        continue
                    }
                    val nextP = p + Pos(dx[d], dy[d])
                    if (field[nextP.x][nextP.y] == WALL) return false
                    if (field[nextP.x][nextP.y] == EMPTY) {
                        i++
                        continue
                    }
                    if (field[nextP.x][nextP.y] == BOX_0 || field[nextP.x][nextP.y] == BOX_1) {
                        allDependencies += nextP
                        allDependencies += nei(nextP)
                        i++
                        continue
                    }
                    error("Unknown symbol: ${field[nextP.x][nextP.y]}")
                }
                for (pos in allDependencies.distinct().reversed()) {
                    val nextPos = pos + Pos(dx[d], dy[d])
                    field[nextPos.x][nextPos.y] = field[pos.x][pos.y]
                    field[pos.x][pos.y] = EMPTY
                }
                return true
            }

            while (fin.hasNext())
                for (move in fin.next()) {
                    val d = dirs.indexOf(move)
                    if (push(robotPos, d)) {
                        robotPos += Pos(dx[d], dy[d])
                    }
                }
            var ans = 0
            for (r in 0 until n) {
                for (c in 0 until m) {
                    if (field[r][c] == BOX_0) ans += r * 100 + c
                }
            }
            println(ans)
        }
    }
}

fun main() {
    Day15.Part1(sample = true).solve()
    Day15.Part1(sample = false).solve()
    Day15.Part2(sample = true).solve()
    Day15.Part2(sample = false).solve()
}
