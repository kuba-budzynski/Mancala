package engine.algorithms

import engine.game.Board
import engine.game.Settings
import engine.heuristics.Heuristic
import mu.KotlinLogging
import engine.player.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Minimax (private val depth: Int = 8): Algo, KoinComponent {

    private val logger = KotlinLogging.logger{}
    private val settings by inject<Settings>()

    override fun run(board: Board, player: Player, children: List<Pair<Int,Board>>): Pair<Double, Int> {
        return minimax(board, depth, player, children)
    }

    private fun minimax(board: Board, depth: Int, player: Player, children: List<Pair<Int,Board>>): Pair<Double, Int>{

        if(board.isInFinalState() || depth <= 0){
            return Pair(settings.heuristic.apply(board), -1)
        }

        var bestScore = if(player == Player.MAX) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY
        val shouldReplace: (Double) -> Boolean = if(player == Player.MAX) { x -> x > bestScore} else { x -> x < bestScore}
        var moveForBestScore = -1

        for(child in children){
            val res = minimax(child.second, depth - 1, Player.swap(player), child.second.children(Player.swap(player), false))
            if(shouldReplace(res.first)){
                bestScore = res.first
                moveForBestScore = child.first
            }
        }
        return Pair(bestScore, moveForBestScore)
    }

    override fun name(): String {
        return "Minimax"
    }
}