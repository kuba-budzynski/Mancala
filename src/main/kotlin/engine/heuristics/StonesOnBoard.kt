package engine.heuristics

import engine.game.Board
import engine.player.Player

class StonesOnBoard: Heuristic {
    override fun apply(board: Board): Double {
        val playerOneStones = board.getSide(Player.MAX).getBuckets().stream().reduce { t, u -> u + t  }.get()
        val playerTwoStones = board.getSide(Player.MIN).getBuckets().stream().reduce { t, u -> u + t  }.get()

        val playerOneZeros = board.getSide(Player.MAX).getBuckets().count { it == 0 }
        val playerTwoZeros = board.getSide(Player.MIN).getBuckets().count { it == 0}

        return (playerOneStones.div(playerOneZeros.coerceAtLeast(1))).toDouble()
            .minus(playerTwoStones.div(playerTwoZeros.coerceAtLeast(1)))
    }

    override fun name(): List<String> {
        return listOf("Stones on board")
    }
}