package player

import algorithms.Algo
import game.Board
import game.GameRules
import heuristics.Heuristic
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AI(val boardHeuristic: Heuristic, private val algo: Algo): PlayerInterface, KoinComponent {

    private val gameRules by inject<GameRules>()
    private lateinit var player: Player

    override fun nextMove(board: Board, fakeMove: Boolean): Int {
        return algo.run(board,player, fakeMove).second
    }

    override fun setPlayer(player: Player) {
        this.player = player
    }

    override fun getPlayerType(): Player {
        return player
    }
}