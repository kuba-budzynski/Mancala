package engine.controllers

import engine.game.Board
import engine.player.PlayerInterface

class ConsoleNotifier: Notifier {

    override fun notifyNewMove(board: Board, move: Int, player: PlayerInterface, moveCount: String) {
        print("\n------------| move: $moveCount |------------")
        print("\nPlayer: " + board.getSide(player.getPlayerType()).printColor(player.getPlayerType().name) + " chose bucket -> " + move)
        print(board)
        print("\n\n")
    }

    override fun notifyGameEnd(board: Board, winner: PlayerInterface, score: Double) {
        print("\n------------| FINAL BOARD |------------")
        print(board)
        print(board.getSide(winner.getPlayerType()).printColor("The winner is ${winner.getPlayerType()} with score: $score"))
        print("\n\n")
    }

    override fun notifyScore(board: Board, score: Double, winner: PlayerInterface) {
        print(board.getSide(winner.getPlayerType()).printColor("The winner is ${winner.getPlayerType()} with score: $score"))
    }

    override fun notifyStart(board: Board, playerOne: PlayerInterface, playerTwo: PlayerInterface) {
        print("\n------------| STARTING BOARD |------------")
        print(board)
        print("\n")
    }
}