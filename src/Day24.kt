import java.io.FileReader
import java.util.*

class Day24 {
    companion object {
        fun fin(sample: Boolean) = Scanner(FileReader(if (sample) "sample.txt" else "input.txt"))

        @JvmStatic
        fun main(args: Array<String>) {
//            Part1(sample = true).solve()
//            Part1(sample = false).solve()
//            Part2(sample = true).solve()
            Part2(sample = false).solve()
        }
    }

    class Part1(sample: Boolean) {
        val fin = fin(sample)

        data class Rule(val x: String, val y: String, val op: String) {
            fun apply(xValue: Boolean, yValue: Boolean): Boolean {
                return when (op) {
                    "AND" -> xValue && yValue
                    "OR" -> xValue || yValue
                    "XOR" -> xValue xor yValue
                    else -> error("wrong op")
                }
            }
        }

        fun solve() {
            val values = hashMapOf<String, Boolean>()
            while (fin.hasNext()) {
                val s = fin.nextLine()
                if (s == "") break
                val (x, y) = s.split(": ")
                values[x] = y == "1"
            }

            val rules = hashMapOf<String, Rule>()
            while (fin.hasNext()) {
                val line = fin.nextLine().split(" ")
                val x = line[0]
                val op = line[1]
                val y = line[2]
                val z = line[4]
                rules[z] = Rule(x, y, op)
            }

            fun calc(z: String): Boolean {
                values[z]?.let { return it }
                val rule = rules[z]!!
                return rule.apply(calc(rule.x), calc(rule.y))
            }

            val sorted = rules.keys.filter { it.startsWith("z") }.sortedDescending()
            val map = sorted.map { key ->
                calc(key)
            }
            val ans = map.fold(0L) { acc, x -> (acc shl 1) + if (x) 1 else 0 }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Rule(val x: String, val y: String, val op: String) {
            fun apply(xValue: Boolean, yValue: Boolean): Boolean {
                return when (op) {
                    "AND" -> xValue && yValue
                    "OR" -> xValue || yValue
                    "XOR" -> xValue xor yValue
                    else -> error("wrong op")
                }
            }
        }

        fun printGraph(g: Map<String, Rule>) {
            buildString {
                appendLine("digraph {")
                appendLine("rankdir=LR;")
                appendLine("node [shape=ellipse];")
                appendLine()
                for (e in g) {
                    val color = when (e.value.op) {
                        "OR" -> "green"
                        "XOR" -> "blue"
                        "AND" -> "red"
                        else -> error("no such op")
                    }
                    appendLine("\"${e.value.x}\" -> \"${e.key}\";")
                    appendLine("\"${e.value.y}\" -> \"${e.key}\";")
                    appendLine("\"${e.key}\" [color=$color];")
                }
                appendLine("}")
            }.let(::println)
        }

        fun solve() {
            val values = hashMapOf<String, Boolean>()
            while (fin.hasNext()) {
                val s = fin.nextLine()
                if (s == "") break
                val (x, y) = s.split(": ")
                values[x] = y == "1"
            }

            val rules = hashMapOf<String, Rule>()
            while (fin.hasNext()) {
                val line = fin.nextLine().split(" ")
                val x = line[0]
                val op = line[1]
                val y = line[2]
                val z = line[4]
                rules[z] = Rule(x, y, op)
            }

            fun findNodes(name: String) =
                (rules.keys + rules.values.flatMap { listOf(it.x, it.y) }).distinct().filter { it.startsWith(name) }
                    .sorted()

            val xNodes = findNodes("x")
            val yNodes = findNodes("y")
            val zNodes = findNodes("z")
            println("xNodes size = ${xNodes.size}, $xNodes")
            println("yNodes size = ${yNodes.size}, $yNodes")
            println("zNodes size = ${zNodes.size}, $zNodes")

            fun findRule(x: String, y: String, op: String): Pair<Rule, String> {
                return rules.entries.singleOrNull { (target, rule) ->
                    val inputs = listOf(rule.x, rule.y)
                    x in inputs && y in inputs && rule.op == op
                }?.let { it.value to it.key } ?: error("Not found rule $op for $x and $y")
            }

            fun findRuleByTarget(target: String): Pair<Rule, String> {
                return rules.entries.single { (ruleTarget, rule) ->
                    ruleTarget == target
                }.let { it.value to it.key }
            }

            // DO SWAPS HERE
            val replacements = listOf(
                listOf("jnn", "wpr", "XOR", "z09"), // (x, y, rule, where should go)
                listOf("stj", "qkq", "XOR", "z20"),
                listOf("kkp", "tnm", "XOR", "z24"),
                listOf("x31", "y31", "AND", "rvc")
            )
            val ans = sortedSetOf<String>()

            fun doSwap(key1: String, key2: String) {
                val value1 = rules[key1]!!
                val value2 = rules[key2]!!
                rules[key1] = value2
                rules[key2] = value1
                ans += key1
                ans += key2
            }
            for ((x, y, rule, shouldGoTo) in replacements) {
                val wrongRule = findRule(x, y, rule)
                val secondRule = findRuleByTarget(shouldGoTo)
                doSwap(wrongRule.second, secondRule.second)
            }

            var carryNodeName = "mjh"
            for (i in 1 until xNodes.size) {
                val xNodeName = xNodes[i]
                val yNodeName = yNodes[i]
                val xyAnd = findRule(xNodeName, yNodeName, "AND")
                val xyXor = findRule(xNodeName, yNodeName, "XOR")
                val curBit = findRule(carryNodeName, xyXor.second, "XOR")
                require(curBit.second == zNodes[i]) {
                    "carry $carryNodeName XOR ${xyXor.second} should go to ${zNodes[i]} but goes to ${curBit.second} instead"
                }
                val carry1 = findRule(carryNodeName, xyXor.second, "AND")
                val newCarry = findRule(carry1.second, xyAnd.second, "OR")
                carryNodeName = newCarry.second
            }

            ans.joinToString(",").let(::println)
        }
    }
}
