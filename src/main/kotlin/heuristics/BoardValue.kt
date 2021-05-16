package heuristics

import game.Board
import player.Player

class BoardValue: Heuristic {
    override fun apply(board: Board): Double {

        val playerOneStones = board.getSide(Player.MAX).getBuckets().stream().reduce { t, u -> u + t  }
        val playerTwoStones = board.getSide(Player.MIN).getBuckets().stream().reduce { t, u -> u + t  }

        val playerOneZeros = board.getSide(Player.MAX).getBuckets().count { it -> it == 0 }
        val playerTwoZeros = board.getSide(Player.MIN).getBuckets().count { it -> it == 0}

        val playerOneWell = board.getSide(Player.MAX).getWell()
        val playerTwoWell = board.getSide(Player.MIN).getWell()

        return (playerOneStones.get() + (playerOneWell * 2.2) + (playerTwoZeros * 1.35)).minus(
            (playerTwoStones.get() + (playerTwoWell * 2.2) + (playerOneZeros * 1.35))
        )
    }
}