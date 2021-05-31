package engine.controllers

import engine.excel.Results
import engine.game.Board
import engine.game.Move
import engine.game.Stats
import mu.KotlinLogging
import engine.player.AI
import engine.player.Moves
import engine.player.Player
import engine.player.PlayerInterface
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

class GameController(private val player1: PlayerInterface,
                     private val player2: PlayerInterface,
                     private val notifier: Notifier = ConsoleNotifier(),
                     private val randomFirstMoveIfAI: Boolean =  true,
                     private val verbose: Boolean = true)
    : Controller, KoinComponent {

    private val logger = KotlinLogging.logger{}
    private val random = Random()
    private val moves by inject<Moves>()
    private val excel by inject<Results>()

    override fun play(initialBoard: Board): Stats {

        val stats = mutableMapOf(Player.MAX to Stats(player = Player.MAX.name), Player.MIN to Stats(player = Player.MIN.name))

        var move = 1
        val players = mutableMapOf(Player.MAX to player1, Player.MIN to player2)
        player1.setPlayer(Player.MAX)
        player2.setPlayer(Player.MIN)
        var current = Player.MAX

        val playersFirstBonus = mutableMapOf(Player.MAX to false, Player.MIN to false)
        val playersFirstSteal = mutableMapOf(Player.MAX to false, Player.MIN to false)

        notifier.notifyStart(initialBoard, player1, player2)

        if(players[current]!! is AI && randomFirstMoveIfAI){
            var randomFirstMove: Int
            val firstMove: Pair<Boolean, Boolean>
            var stones: Int
            val time = measureTimeMillis {
                randomFirstMove = random.nextInt(initialBoard.getSide(current).allNotEmpty().size)
                stones = initialBoard.getSide(current).getBuckets()[randomFirstMove]
                firstMove = initialBoard.makeMove(current, randomFirstMove)
            }

            stats[current]!!.totalTime += BigInteger.valueOf(time)
            stats[current]!!.totalMoves += 1

            excel.addMove(gameIndex, Move(current, randomFirstMove, stones, time, firstMove.first, false))
            notifier.notifyNewMove(initialBoard, randomFirstMove, players[current]!!, move.toString())
            moves.makeMove(current, randomFirstMove, initialBoard.getSide(current).getBuckets()[randomFirstMove])

            move++
            if(!firstMove.first){
                current = Player.swap(current)
            }
            else{
                stats[current]!!.bonusMoves += 1
            }
        }

        while (!initialBoard.isInFinalState()) {

            val moveMade: Pair<Boolean, Boolean>
            var res: Int
            var stones: Int
            val time = measureTimeMillis {
                res = players[current]?.nextMove(initialBoard)!!
                stones = initialBoard.getSide(current).getBuckets()[res]
                moveMade = initialBoard.makeMove(current, res)
            }

            stats[current]!!.totalTime += BigInteger.valueOf(time)
            stats[current]!!.totalMoves += 1

            notifier.notifyNewMove(initialBoard, res, players[current]!!, move.toString())
            moves.makeMove(current, res, initialBoard.getSide(current).getBuckets()[res])
            excel.addMove(gameIndex, Move(current, res, stones, time, moveMade.first, moveMade.second))
            move++

            if(moveMade.second){
                stats[current]!!.steals += 1
                if(!playersFirstSteal[current]!!){
                    stats[current]!!.movesToFirstSteal = stats[current]!!.totalMoves
                    stats[current]!!.timeToFirstSteal = stats[current]!!.totalTime
                    playersFirstSteal[current] = true
                }
            }

            if(moveMade.first){
                if(verbose) logger.debug { "${initialBoard.getSide(current).printColor(current.name)} gets another move" }
                players[Player.swap(current)]?.nextMove(initialBoard, true) // Mock a move to keep alternating min-max structure
                stats[current]!!.bonusMoves += 1
                if(!playersFirstBonus[current]!!){
                    stats[current]!!.movesToFirstBonus = stats[current]!!.totalMoves
                    stats[current]!!.timeToFirstBonus = stats[current]!!.totalTime
                    playersFirstBonus[current] = true
                }
            }
            else current = Player.swap(current)
        }

        initialBoard.moveRemaining(
            if(initialBoard.getSide(current).getBuckets().all { it -> it == 0 }) Player.swap(current) else current
        )

        val winner = if (initialBoard.score() > 0) Player.MAX else Player.MIN
        notifier.notifyGameEnd(initialBoard, players[winner]!!, initialBoard.score())
        stats[winner]!!.score = initialBoard.score()
        if(initialBoard.score() != 0.0){
            if(verbose) logger.info {"WINNER: ${initialBoard.getSide(winner).printColor(winner.name)}"}
            if(verbose) logger.debug { "FINAL SCORE: ${initialBoard.score()}" }
        }
        else{
            if(verbose) logger.info { "TIE with score ${initialBoard.score()}" }
            stats[Player.MIN]!!.player = "TIE"
            stats[Player.MAX]!!.player = "TIE"
        }
        gameIndex++;
        return stats[winner]!!
    }

    companion object{
        var gameIndex = 0
    }
}