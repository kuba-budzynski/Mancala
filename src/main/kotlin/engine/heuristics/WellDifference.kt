package engine.heuristics

import engine.game.Board
import engine.player.Player

class WellDifference: Heuristic {
    override fun apply(board: Board): Double {
        return board.getSide(Player.MAX).getWell().toDouble() - board.getSide(Player.MIN).getWell().toDouble()
    }

    override fun name(): List<String> {
        return listOf("Well difference")
    }
}