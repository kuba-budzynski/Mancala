package engine.controllers

import engine.game.Board
import engine.player.PlayerInterface

class Silent: Notifier {
    override fun notifyNewMove(board: Board, move: Int, player: PlayerInterface, moveCount: String) {
    }

    override fun notifyGameEnd(board: Board, winner: PlayerInterface, score: Double) {
    }

    override fun notifyScore(board: Board, score: Double, winner: PlayerInterface) {
    }

    override fun notifyStart(board: Board, playerOne: PlayerInterface, playerTwo: PlayerInterface) {
    }
}