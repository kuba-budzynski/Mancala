import algorithms.Algo
import algorithms.Minimax
import algorithms.MinimaxWithAlphaBeta
import controllers.GameController
import game.Board
import game.GameRules
import heuristics.BoardValue
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.dsl.module
import player.AI
import player.ConsolePlayer
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

val logger = KotlinLogging.logger{}
val modules =  module{
    single { GameRules() }
}

fun main(){
    logger.info { "App has been started" }
    startKoin{
        modules(modules)
    }

    val board = Board()
    val heuristic = BoardValue()
    val algo: Algo = Minimax(heuristic)
//    val algo: Algo = MinimaxWithAlphaBeta(heuristic)

    val player1 = ConsolePlayer(heuristic, algo)
//    val player2 = ConsolePlayer(heuristic, algo)

//    val player1 = AI(heuristic, algo)
    val player2 = AI(heuristic, algo)

    val game = GameController(player1, player2)
    val time = measureTimeMillis {
        game.play(board)
    }
    println("TIME: ${((time / 1000.0) * 100).roundToInt() /100.0} s")
    logger.info { "FINAL SCORE: ${board.score()}" }
}
