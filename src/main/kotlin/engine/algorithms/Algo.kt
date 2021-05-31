package engine.algorithms

import engine.game.Board
import engine.player.Player

interface Algo {
    fun run(board: Board, player: Player, children: List<Pair<Int,Board>>): Pair<Double, Int>
    fun name(): String
}