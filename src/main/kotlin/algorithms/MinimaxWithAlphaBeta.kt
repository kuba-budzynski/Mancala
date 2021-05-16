package algorithms

import game.Board
import heuristics.Heuristic
import mu.KotlinLogging
import player.Player

class MinimaxWithAlphaBeta(private val heuristic: Heuristic, private val searchDepth: Int = 8): Algo {

    private val logger = KotlinLogging.logger{}

    override fun run(board: Board, player: Player, fakeMove: Boolean): Pair<Double, Int> {
        return minimaxalphabeta(board, searchDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, player, fakeMove)
    }

    private fun minimaxalphabeta(board: Board, depth: Int, alpha: Double, beta: Double, player: Player, fakeMove: Boolean): Pair<Double, Int>{

        if(fakeMove) logger.warn { "Fake move made by $player" }
        if(board.isInFinalState() || depth <= 0){
            return Pair(heuristic.apply(board), -1)
        }

        var bestScore = if(player == Player.MAX) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY
        val shouldReplace: (Double) -> Boolean = if(player == Player.MAX) { x -> x > bestScore} else { x -> x < bestScore}
        var moveForBestScore = -1

        var innerAlpha = alpha
        var innerBeta = beta

        val children = if(fakeMove) listOf(Pair(-1, board)) else board.children(player)
        for(child in children){
            val res = minimaxalphabeta(child.second, depth - 1, innerAlpha, innerBeta, Player.swap(player), false)

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
}