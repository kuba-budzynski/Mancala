package engine.player

import engine.algorithms.Algo
import engine.game.Board
import engine.game.GameRules
import engine.heuristics.Heuristic
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AI(private val algo: Algo): PlayerInterface, KoinComponent {

    private val gameRules by inject<GameRules>()
    private lateinit var player: Player

    override fun nextMove(board: Board, fakeMove: Boolean): Int {
        return algo.run(board, player, board.children(player, fakeMove)).second
    }

    override fun setPlayer(player: Player) {
        this.player = player
    }

    override fun getPlayerType(): Player {
        return player
    }
}