import java.io.FileReader
import java.util.*

class Day17 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        fun solve() {
            var regA = fin.nextLine().split(" ").last().toInt()
            var regB = fin.nextLine().split(" ").last().toInt()
            var regC = fin.nextLine().split(" ").last().toInt()
            var ptr = 0
            fin.nextLine()
            val program = fin.nextLine().split(" ").last().split(",").map { it.toInt() }

            fun combo(literal: Int): Int =
                when (literal) {
                    in 0..3 -> literal
                    4 -> regA
                    5 -> regB
                    6 -> regC
                    else -> error("error")
                }

            fun adv(literal: Int) {
                val x = regA
                val y = 1 shl combo(literal)
                regA = x / y
            }

            fun bxl(literal: Int) {
                regB = regB xor literal
            }

            fun bst(literal: Int) {
                regB = combo(literal) % 8
            }

            fun jnz(literal: Int) {
                if (regA == 0) return
                ptr = literal - 2
            }

            fun bxc(literal: Int) {
                regB = regB xor regC
            }

            fun out(literal: Int) {
                print("" + combo(literal).mod(8) + ",")
            }

            fun bdv(literal: Int) {
                val x = regA
                val y = 1 shl combo(literal)
                regB = x / y
            }

            fun cdv(literal: Int) {
                val x = regA
                val y = 1 shl combo(literal)
                regC = x / y
            }

            val ops = arrayOf(
                ::adv, ::bxl, ::bst, ::jnz, ::bxc, ::out, ::bdv, ::cdv
            )
            while (ptr < program.size) {
                val op = program[ptr]
                val literal = program[ptr + 1]
                ops[op](literal)
                ptr += 2
            }
            println()
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        class Bits(val values: List<Int>, val fixed: List<Boolean>) {
            fun expand(size: Int, values: MutableList<Int>, fixed: MutableList<Boolean>) {
                while (values.size < size) values += 0
                while (fixed.size < size) fixed += false
            }

            fun isZeroAfter(at: Int) : Boolean {
                for (i in at until values.size) if (values[i] != 0) return false
                return true
            }

            fun fixAt(at: Int, x: Int): Bits? {
                val bits = bits3(x)
                val newValues = values.toMutableList()
                val newFixed = fixed.toMutableList()
                expand(at + 3, newValues, newFixed)
                for (i in 0 until 3) {
                    if (newFixed[at + i] && bits[i] != newValues[at + i]) return null
                    newFixed[at + i] = true
                    newValues[at + i] = bits[i]
                }
                return Bits(newValues, newFixed)
            }

            fun bits3(x: Int): List<Int> {
                val bits = mutableListOf<Int>()
                repeat(3) {
                    bits += (x shr it) and 1
                }
                return bits
            }

            fun toNumber(): Long {
                var res = 0L
                for (x in values.reversed()) {
                    res = res shl 1
                    res += x
                }
                return res
            }
        }

        /*
        B=A%8 XOR 3
        C=A / 2^B
        A=A / 8
        B=B XOR C
        B=B XOR 5
        PRINT B % 8
        IF A>0: REPEAT ALL
         */
        val program = intArrayOf(2, 4, 1, 3, 7, 5, 0, 3, 4, 1, 1, 5, 5, 5, 3, 0)
//        val programSmall = program.indices.filter { it % 2 == 0 }.map { program[it] }

        var minCandidate = Long.MAX_VALUE
        fun gen(bits: Bits, at: Int) {
            if (at == program.size) {
                if (bits.isZeroAfter(at * 3) || true) {
                    val n = bits.toNumber()
                    if (n < minCandidate) {
                        minCandidate = n
                    }
                }
                return
            }

            val target = program[at]
            for (last3 in 0..7) {
                val b = last3 xor 3
                val shiftC = b
                val c = b xor 5 xor target
                val newBits = bits.fixAt(at * 3, last3)?.fixAt(at * 3 + shiftC, c) ?: continue
                gen(newBits, at + 1)
            }
        }

        fun solve() {
            gen(Bits(emptyList(), emptyList()), 0)
            println(minCandidate)
        }
    }
}

fun main() {
    Day17.Part1(sample = true).solve()
    Day17.Part1(sample = false).solve()
//    Day17.Part2(sample = true).solve()
    Day17.Part2(sample = false).solve()
}
