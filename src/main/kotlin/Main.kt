
import engine.algorithms.Minimax
import engine.algorithms.MinimaxWithAlphaBeta
import engine.controllers.GameController
import engine.controllers.Silent
import engine.excel.Results
import engine.game.*
import engine.heuristics.BoardValue
import engine.heuristics.BonusMoves
import engine.heuristics.Steals
import engine.heuristics.combo.BoardState
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.dsl.module
import engine.player.AI
import engine.player.ConsolePlayer
import engine.player.Moves
import engine.player.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

val logger = KotlinLogging.logger{}
val modules =  module{
    single { GameRules() }
    single { Settings() }
    single { Moves() }
    single { Results() }
}

fun main(){

    logger.debug { "App has been started" }
    startKoin{
        modules(modules)
    }
    val gr = GameSettings()
    gr.settings.heuristic = BoardState()

    val depth1 = 6
    val algo1 = MinimaxWithAlphaBeta(depth1)

    val depth2 = 6
    val algo2 = MinimaxWithAlphaBeta(depth2)

    val player1 = AI(algo1)
    val player2 = AI(algo2)

//    Generate statistics for n-games

    val n = 20
    for(i in 1..n){
        val board = Board()
        val game = GameController(player1, player2, notifier = Silent(), verbose = false)

        val stats = game.play(board)
        gr.excel.addStat(stats)
        println(stats)
        logger.info {
            "Test ${gr.gameRules.playerMax + i + gr.gameRules.reset}/${n} \t Time: ${stats.toSeconds(stats.totalTime)} s"
        }
    }

    gr.excel.generateResults(
            listOf(
                Triple(depth1, algo1.name(), gr.settings.heuristic.name()),
                Triple(depth2, algo2.name(), gr.settings.heuristic.name())
            )
    )
    logger.debug { "Excel file has been generated" }

//    val game = GameController(player1, player2)
//    game.play(Board())
}

class GameSettings: KoinComponent{
    val settings by inject<Settings>()
    val excel by inject<Results>()
    val gameRules by inject<GameRules>()
}
