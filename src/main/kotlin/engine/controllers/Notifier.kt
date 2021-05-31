package engine.controllers

import engine.game.Board
import engine.player.PlayerInterface

interface Notifier {
    fun notifyNewMove(board: Board, move: Int, player: PlayerInterface, moveCount: String = "0")
    fun notifyGameEnd(board: Board, winner: PlayerInterface, score: Double)
    fun notifyScore(board: Board, score: Double, winner: PlayerInterface)
    fun notifyStart(board: Board, playerOne: PlayerInterface, playerTwo: PlayerInterface)
}