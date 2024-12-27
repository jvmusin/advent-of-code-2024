import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs

class Day21 {
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
            fun move(ch: Char): Pos {
                if (ch == '^') return Pos(r - 1, c)
                if (ch == 'v') return Pos(r + 1, c)
                if (ch == '<') return Pos(r, c - 1)
                if (ch == '>') return Pos(r, c + 1)
                error("wrong ch: $ch")
            }
        }

        data class State(
            val directionalPos1: Pos,
            val directionalPos2: Pos,
            val numericPos: Pos,
            val writtenCode: String,
        )

        fun solve() {
            val directionalMap = """
                 ^A
                <v>
            """.trimIndent().lines()
            val numericMap = """
                789
                456
                123
                 0A
            """.trimIndent().lines()

            fun isValidPos(pos: Pos, map: List<String>): Boolean {
                return pos.r in map.indices && pos.c in map[pos.r].indices && map[pos.r][pos.c] != ' '
            }

            val curMaps = listOf(
                directionalMap,
                directionalMap,
                numericMap,
            )

            fun State.getPos(index: Int) = when (index) {
                0 -> directionalPos1
                1 -> directionalPos2
                2 -> numericPos
                else -> error("out of bounds")
            }

            fun State.setPos(index: Int, pos: Pos) = when (index) {
                0 -> State(pos, directionalPos2, numericPos, writtenCode)
                1 -> State(directionalPos1, pos, numericPos, writtenCode)
                2 -> State(directionalPos1, directionalPos2, pos, writtenCode)
                else -> error("out of bounds")
            }

            fun pressButton(state: State): State? {
                val curPositions = mutableListOf(
                    state.directionalPos1,
                    state.directionalPos2,
                    state.numericPos
                )

                var written = ""
                for (index in curMaps.indices) {
                    val curMap = curMaps[index]
                    val curPosition = curPositions[index]
                    val curAction = curMap[curPosition.r][curPosition.c]
                    when {
                        curAction == 'A' -> {
                            if (index == curMaps.lastIndex) written = "A"
                            continue
                        }

                        curAction == ' ' -> return null
                        curAction in "<>^v" -> {
                            val nextPosition = curPositions[index + 1].move(curAction)
                            curPositions[index + 1] = nextPosition
                            val nextMap = curMaps[index + 1]
                            if (!isValidPos(nextPosition, nextMap)) {
                                return null
                            }
                            break
                        }

                        curAction.isDigit() -> {
                            written = curAction.toString()
                            break
                        }

                        else -> error("wrong pos")
                    }
                }

                return State(
                    curPositions[0],
                    curPositions[1],
                    curPositions[2],
                    state.writtenCode + written
                )
            }

            fun moveButton(state: State, index: Int, dir: Char): State? {
                val newPos = state.getPos(index).move(dir)
                if (!isValidPos(newPos, curMaps[index])) return null
                return state.setPos(index, newPos)
            }

            val initialState = State(
                Pos(0, 2),
                Pos(0, 2),
                Pos(3, 2),
                ""
            )
            val dp = hashMapOf<State, Int>()
            dp[initialState] = 0
            val q = ArrayDeque<State>()
            q.add(initialState)
            while (q.isNotEmpty()) {
                val curState = q.removeFirst()
                if (curState.writtenCode.length == 4) {
                    continue // we're done here
                }
                fun upd(p: State?) {
                    if (p != null && p !in dp) {
                        dp[p] = dp[curState]!! + 1
                        q += p
                    }
                }
                run {
                    val ifPress = pressButton(curState)
                    upd(ifPress)
                }
                run {
                    for (ch in "<>^v") {
                        val ifMove = moveButton(curState, 0, ch)
                        upd(ifMove)
                    }
                }
            }

            var ans = 0
            while (fin.hasNext()) {
                val code = fin.nextLine()
                var curMin = Int.MAX_VALUE
                for (e in dp) {
                    if (e.key.writtenCode == code) {
                        val curAns = code.filter { it.isDigit() }.toInt() * e.value
                        if (curAns < curMin) curMin = curAns
                    }
                }
                require(curMin != Int.MAX_VALUE)
                ans += curMin
            }
            println(ans)
        }
    }

    class Part2(sample: Boolean) {
        val fin = fin(sample)

        data class Pos(val r: Int, val c: Int) : Comparable<Pos> {
            override fun compareTo(other: Pos): Int {
                var cmp = r.compareTo(other.r)
                if (cmp == 0) cmp = c.compareTo(other.c)
                return cmp
            }

            operator fun plus(p: Pos) = Pos(r + p.r, c + p.c)
        }

        data class State(
            val skipHands: Int,
            val prevHandPos: Pos,
            val thisHandPos: Pos,
        ) : Comparable<State> {
            override fun compareTo(other: State): Int {
                var c = skipHands.compareTo(other.skipHands)
                if (c == 0) c = prevHandPos.compareTo(other.prevHandPos)
                if (c == 0) c = thisHandPos.compareTo(other.thisHandPos)
                return c
            }
        }

        companion object {
            val directionalAPos = Pos(0, 2)

            val dr = intArrayOf(0, 0, 1, -1)
            val dc = intArrayOf(-1, 1, 0, 0)
            val dirButtonPos = arrayOf(
                Pos(1, 0),
                Pos(1, 2),
                Pos(1, 1),
                Pos(0, 1)
            )

            const val DIRECTIONAL_PADS = 25
            val directionalMap = """
                 ^A
                <v>
            """.trimIndent().lines()
            val numericMap = """
                789
                456
                123
                 0A
            """.trimIndent().lines()

            fun isValidPos(pos: Pos, map: List<String>): Boolean {
                return pos.r in map.indices && pos.c in map[pos.r].indices && map[pos.r][pos.c] != ' '
            }

            fun List<String>.mapFind(c: Char): Pos {
                for (i in indices) {
                    for (j in this[i].indices) {
                        if (this[i][j] == c) {
                            return Pos(i, j)
                        }
                    }
                }
                error("not found: $c")
            }
        }

        class Queue2 {
            val dp = hashMapOf<Pair<State, State>, Long>()

            fun get(from: State, to: State): Long {
                require(from.skipHands == to.skipHands)
                if (from.skipHands == -1) return 0
                return dp[from to to]!!
            }

            fun saveAllDirections(state0: State) {
                data class Wrapper(
                    val state: State,
                    val result: Long
                )

                val innerDp = hashMapOf<State, Long>()
                val q = PriorityQueue<Wrapper>(compareBy({ it.state }, { it.result }))
                innerDp[state0] = 0
                q.add(Wrapper(state0, 0))

                fun nextState(): Pair<State, Long>? = q.poll()?.let { it.state to it.result }
                fun upd(state: State, dist: Long) {
                    val prevDist = innerDp[state]
                    if (prevDist == null || prevDist > dist) {
                        if (prevDist != null) q.remove(Wrapper(state, prevDist))
                        innerDp[state] = dist
                        q.add(Wrapper(state, dist))
                    }
                }

                while (true) {
                    val (state, dist) = nextState() ?: break
                    val thisHandMap = if (state.skipHands == DIRECTIONAL_PADS) numericMap else directionalMap
                    val dir = dirButtonPos.indexOf(state.prevHandPos)
                    if (dir != -1) {
                        // click on the button and see where we go
                        val nextThisHandPos = state.thisHandPos + Pos(dr[dir], dc[dir])
                        if (isValidPos(nextThisHandPos, thisHandMap)) {
                            upd(State(state.skipHands, state.prevHandPos, nextThisHandPos), dist + 1)
                        }
                    }
                    // now move prev hand
                    for (r in directionalMap.indices) {
                        for (c in directionalMap[r].indices) {
                            if (directionalMap[r][c] != ' ') {
                                val switch = get(
                                    State(state.skipHands - 1, directionalAPos, state.prevHandPos),
                                    State(state.skipHands - 1, directionalAPos, Pos(r, c))
                                )
                                upd(State(state.skipHands, Pos(r, c), state.thisHandPos), dist + switch)
                            }
                        }
                    }
                }

                for (e in innerDp) {
                    dp[state0 to e.key] = e.value
                }
            }

            fun generateAllStates() {
                fun List<String>.mapToPositions() = indices.flatMap { r -> this[r].indices.map { c -> Pos(r, c) } }
                    .filter { (r, c) -> this[r][c] != ' ' }

                val directionalPositions = directionalMap.mapToPositions()
                val numericPositions = numericMap.mapToPositions()
                for (skipHands in 0..DIRECTIONAL_PADS) {
                    val thisHandPositions =
                        if (skipHands == DIRECTIONAL_PADS) numericPositions
                        else directionalPositions
                    for (prevHand in directionalPositions) {
                        for (thisHand in thisHandPositions) {
                            val s = State(skipHands, prevHand, thisHand)
                            saveAllDirections(s)
                        }
                    }
                }
            }
        }

        fun test(q: Queue2) {
            if (DIRECTIONAL_PADS != 2) return
            q.get(
                State(DIRECTIONAL_PADS, Pos(0, 1), Pos(1, 1)),
                State(DIRECTIONAL_PADS, Pos(0, 1), Pos(0, 1))
            ).also { require(it == 1L) }
            q.get(
                State(DIRECTIONAL_PADS, Pos(0, 1), Pos(3, 1)),
                State(DIRECTIONAL_PADS, Pos(0, 1), Pos(0, 1))
            ).also { require(it == 3L) }

            // initial seq
            q.get(
                State(1, Pos(0, 2), Pos(0, 2)),
                State(1, Pos(1, 1), Pos(0, 2))
            ).also { require(it == 2L) }
            q.get(
                State(1, Pos(0, 2), Pos(0, 2)),
                State(1, Pos(1, 1), Pos(1, 2))
            ).also { require(it == 3L) }
            q.get(
                State(1, Pos(0, 2), Pos(0, 2)),
                State(1, Pos(1, 0), Pos(1, 1))
            ).also { require(it == 5L) }
            q.get(
                State(1, Pos(0, 2), Pos(0, 2)),
                State(1, Pos(1, 0), Pos(1, 0))
            ).also { require(it == 6L) }
            q.get(
                State(1, Pos(0, 2), Pos(0, 2)),
                State(1, Pos(0, 2), Pos(1, 0))
            ).also { require(it == 9L) }

            q.get(
                State(2, Pos(1, 0), Pos(3, 2)),
                State(2, Pos(1, 0), Pos(3, 1))
            ).also { require(it == 1L) }
            q.get(
                State(2, Pos(0, 2), Pos(3, 2)),
                State(2, Pos(1, 0), Pos(3, 2))
            ).also { require(it == 9L) } // FAILS WITH it=5

//            q.get(
//                State(DIRECTIONAL_PADS, Pos(0, 2), Pos(3, 2)),
//                State(DIRECTIONAL_PADS, Pos(0, 2), Pos(3, 1))
//            ).also { require(it == 18L) }

            println("Check finished")
        }

        fun solve() {
            val q2 = Queue2()
            q2.generateAllStates()

            test(q2)

            var ans = 0L
            while (fin.hasNext()) {
                val s = fin.next()
                var thisHandPos = numericMap.mapFind('A')
                val prevHandPos = directionalMap.mapFind('A')
                var totalDist = 0L
                for (c in s) {
                    val wantPos = numericMap.mapFind(c)
                    val dist = q2.get(
                        State(DIRECTIONAL_PADS, prevHandPos, thisHandPos),
                        State(DIRECTIONAL_PADS, prevHandPos, wantPos)
                    )
                    totalDist += dist + 1
                    thisHandPos = wantPos
                }
                ans += s.filter { it.isDigit() }.toLong() * totalDist
            }
            println(ans)
        }
    }
}
