import java.awt.image.BufferedImage
import java.io.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool
import javax.imageio.ImageIO

class Day14 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun times(k: Int) = Pos(x * k, y * k)
        }

        fun readPos(s: String): Pos {
            val (x, y) = s.split("=")[1].split(",").map { it.toInt() }
            return Pos(x, y)
        }

        fun mod(x: Int, m: Int) = (((x + m) % m) + m) % m

        fun solve() {
            val robots = mutableListOf<Pair<Pos, Pos>>()
            while (fin.hasNext()) {
                robots.add(readPos(fin.next()) to readPos(fin.next()))
            }
            var byQuadrant = IntArray(4)
            val n = 101
            val m = 103
            for (r in robots) {
                val np = r.first + r.second * 100
                val x = mod(np.x, n)
                val y = mod(np.y, m)
                if (x == n / 2 || y == m / 2) continue
                val r = if (x < n / 2) 0 else 1
                val c = if (y < (m / 2)) 0 else 1
                val q = r * 2 + c
                byQuadrant[q]++
            }
            val ans = byQuadrant.fold(1L) { acc, x -> acc * x }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val x: Int, val y: Int) {
            operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
            operator fun times(k: Int) = Pos(x * k, y * k)
        }

        fun readPos(s: String): Pos {
            val (x, y) = s.split("=")[1].split(",").map { it.toInt() }
            return Pos(x, y)
        }

        fun mod(x: Int, m: Int) = (((x + m) % m) + m) % m
        val n = 101
        val m = 103

        fun draw(robots: List<Pair<Pos, Pos>>, steps: Int) {
            val pic = Array(n) { BooleanArray(m) }
            for (r in robots) {
                val np = r.first + r.second * steps
                val x = mod(np.x, n)
                val y = mod(np.y, m)
                pic[x][y] = true
            }

            // drow an image to a file
            val img = BufferedImage(n, m, BufferedImage.TYPE_INT_RGB)
            for (i in 0 until n) {
                for (j in 0 until m) {
                    img.setRGB(i, j, if (pic[i][j]) 0x000000 else 0xFFFFFF)
                }
            }
            val file = File("images/out_${"%05d".format(steps)}.png")

            if (file.exists()) file.delete()
            file.createNewFile()
            ImageIO.write(img, "png", file)
        }

        fun solve() {
//            System.setOut(PrintStream(BufferedOutputStream(FileOutputStream("out.txt"))))
            val robots = mutableListOf<Pair<Pos, Pos>>()
            while (fin.hasNext()) {
                robots.add(readPos(fin.next()) to readPos(fin.next()))
            }
            for (i in 0..20000) {
                draw(robots, i)
            }
//            System.out.flush()
//            System.out.close()
        }
    }
}

fun main() {
    Day14.Part1(sample = true).solve()
    Day14.Part1(sample = false).solve()
//    Day14.Part2(sample = true).solve()
    Day14.Part2(sample = false).solve()
}
