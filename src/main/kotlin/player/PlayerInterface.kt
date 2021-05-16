package player

import game.Board
import heuristics.Heuristic

interface PlayerInterface {
    fun nextMove(board: Board, fakeMove: Boolean = false): Int
    fun setPlayer(player: Player)
    fun getPlayerType(): Player
}