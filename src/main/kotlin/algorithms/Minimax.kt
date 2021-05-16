package algorithms

import game.Board
import heuristics.Heuristic
import mu.KotlinLogging
import player.Player

class Minimax (private val heuristic: Heuristic, private val searchDepth: Int = 8): Algo {

    private val logger = KotlinLogging.logger{}

    override fun run(board: Board, player: Player, fakeMove: Boolean): Pair<Double, Int> {
        return minimax(board, searchDepth, player, fakeMove)
    }

    private fun minimax(board: Board, depth: Int, player: Player, fakeMove: Boolean): Pair<Double, Int>{

        if(fakeMove) logger.warn { "Fake move made by $player" }

        if(board.isInFinalState() || depth <= 0){
            return Pair(heuristic.apply(board), -1)
        }

        var bestScore = if(player == Player.MAX) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY
        val shouldReplace: (Double) -> Boolean = if(player == Player.MAX) { x -> x > bestScore} else { x -> x < bestScore}
        var moveForBestScore = -1

        val children = if(fakeMove) listOf(Pair(-1, board)) else board.children(player)
        for(child in children){
            val res = minimax(child.second, depth - 1, Player.swap(player), false)
            if(shouldReplace(res.first)){
                bestScore = res.first
                moveForBestScore = child.first
            }
        }
        return Pair(bestScore, moveForBestScore)
    }
}