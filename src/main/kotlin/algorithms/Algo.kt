package algorithms

import game.Board
import heuristics.Heuristic
import player.Player

interface Algo {
    fun run(board: Board, player: Player, fakeMove: Boolean = false): Pair<Double, Int>
}