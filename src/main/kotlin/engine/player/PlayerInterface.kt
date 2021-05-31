package engine.player

import engine.game.Board

interface PlayerInterface {
    fun nextMove(board: Board, fakeMove: Boolean = false): Int
    fun setPlayer(player: Player)
    fun getPlayerType(): Player
}