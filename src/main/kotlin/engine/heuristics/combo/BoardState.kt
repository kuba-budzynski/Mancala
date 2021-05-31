package engine.heuristics.combo

import engine.game.Board
import engine.heuristics.*

class BoardState: Heuristic {

    private val steals = Steals()
    private val bonuses = BonusMoves()
    private val stones = StonesOnBoard()
    private val wells = WellDifference()

    override fun apply(board: Board): Double {
        return (2.9 * wells.apply(board)) + (2.1 * stones.apply(board)) + (1.7 * steals.apply(board)) + (0.9 * bonuses.apply(board))
    }

    override fun name(): List<String> {
        return wells.name() + steals.name() + bonuses.name() + stones.name()
    }
}