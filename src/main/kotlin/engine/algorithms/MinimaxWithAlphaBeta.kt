package engine.algorithms

import engine.game.Board
import engine.game.Settings
import engine.heuristics.Heuristic
import mu.KotlinLogging
import engine.player.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MinimaxWithAlphaBeta(private val depth: Int = 8): Algo, KoinComponent {

    private val logger = KotlinLogging.logger{}
    private val settings by inject<Settings>()

    override fun run(board: Board, player: Player, children: List<Pair<Int,Board>>): Pair<Double, Int> {
        return minimaxalphabeta(board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, player, children)
    }

    private fun minimaxalphabeta(board: Board, depth: Int, alpha: Double, beta: Double, player: Player, children: List<Pair<Int,Board>>): Pair<Double, Int>{

        if(board.isInFinalState() || depth <= 0){
            return Pair(settings.heuristic.apply(board), -1)
        }

        var bestScore = if(player == Player.MAX) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY
        val shouldReplace: (Double) -> Boolean = if(player == Player.MAX) { x -> x > bestScore} else { x -> x < bestScore}
        var moveForBestScore = -1

        var innerAlpha = alpha
        var innerBeta = beta

        for(child in children){
            val res = minimaxalphabeta(child.second, depth - 1, innerAlpha, innerBeta, Player.swap(player), child.second.children(Player.swap(player), false))

            if(player == Player.MAX) innerAlpha = alpha.coerceAtLeast(res.first)
            else if(player == Player.MIN) innerBeta = beta.coerceAtMost(res.first)

            if(shouldReplace(res.first)){
                bestScore = res.first
                moveForBestScore = child.first
            }
            if(innerBeta <= innerAlpha) break
        }
        return Pair(bestScore, moveForBestScore)
    }

    override fun name(): String {
        return "Alpha beta"
    }
}