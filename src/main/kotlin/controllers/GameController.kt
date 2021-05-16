package controllers

import game.Board
import mu.KotlinLogging
import player.AI
import player.Player
import player.PlayerInterface
import java.util.*

class GameController(private val player1: PlayerInterface, private val player2: PlayerInterface, private val randomFirstMoveIfAI: Boolean =  true): Controller {

    private val logger = KotlinLogging.logger{}
    private val random = Random()

    override fun play(initialBoard: Board) {
        var move = 1
        val players = mutableMapOf(Player.MAX to player1, Player.MIN to player2)
        player1.setPlayer(Player.MAX)
        player2.setPlayer(Player.MIN)
        var current = Player.MAX

        // STARTING BOARD STATE
        print("\n------------| STARTING BOARD |------------")
        print(initialBoard)
        print("\n")

        if(players[current]!! is AI && randomFirstMoveIfAI){
            // FIRST RANDOM VALID MOVE IF NOT A PLAYER
            val randomFirstMove = random.nextInt(initialBoard.getSide(current).allNotEmpty().size)
            initialBoard.makeMove(current, randomFirstMove)
            print("\n------------| move: $move |------------")
            print("\nPlayer: " + initialBoard.getSide(current).printColor(current.name) + " chose bucket -> " + randomFirstMove)
            print(initialBoard)
            print("\n\n")
            move++
            current = Player.swap(current)
        }

        // GAME ITSELF
        while (!initialBoard.isInFinalState()) {

            val res = players[current]?.nextMove(initialBoard)
            val anotherOne = initialBoard.makeMove(current, res!!)

            print("\n------------| move: $move |------------")
            print("\nPlayer: " + initialBoard.getSide(current).printColor(current.name) + " chose bucket -> " + res)
            print(initialBoard)
            print("\n\n")
            move++

            if(anotherOne){
                logger.debug { "${initialBoard.getSide(current).printColor(current.name)} gets another move" }
                players[Player.swap(current)]?.nextMove(initialBoard, true) // Mock a move to keep alternating min-max structure
            }
            else current = Player.swap(current)
        }

        // MOVE ALL UNUSED STONES TO OPPONENT
        initialBoard.moveRemaining(
            if(initialBoard.getSide(current).getBuckets().all { it -> it == 0 }) Player.swap(current) else current
        )
        print("\n------------| FINAL BOARD |------------")
        print(initialBoard)
        print("\n\n")

        // PRINT FINAL BOARD STATE
        if(initialBoard.score() != 0.0){
            val winner = if (initialBoard.score() > 0) Player.MAX else Player.MIN
            print("WINNER: ${initialBoard.getSide(winner).printColor(winner.name)}\n")
            print("FINAL SCORE: ${initialBoard.score()}\n")
        }
        else print("TIE with score ${initialBoard.score()}\n")
    }
}