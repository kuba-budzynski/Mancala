package engine.heuristics

import engine.game.Board
import engine.game.GameRules
import engine.player.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Steals : Heuristic, KoinComponent {

    private val gameOptions by inject<GameRules>()

    override fun apply(board: Board): Double {

        val maxSide = board.getSide(Player.MAX)
        val minSide = board.getSide(Player.MIN)

        var maxPossibleSteals = 0
        var maxStonesSteals = 0
        for(i in 0 until maxSide.getBuckets().size){
            val stones = maxSide.getBuckets()[i]
            val nextIndex = i + stones
            if(nextIndex < gameOptions.buckets){
                if(maxSide.getBuckets()[nextIndex] == 0 && minSide.getBuckets()[gameOptions.buckets - 1 - nextIndex] != 0){
                    maxPossibleSteals++
                    maxStonesSteals += minSide.getBuckets()[gameOptions.buckets - 1 - nextIndex]
                }
            }
        }

        var minPossibleSteals = 0
        var minStonesSteals = 0
        for(i in 0 until minSide.getBuckets().size){
            val stones = minSide.getBuckets()[i]
            val nextIndex = i + stones
            if(nextIndex < gameOptions.buckets){
                if(minSide.getBuckets()[nextIndex] == 0 && maxSide.getBuckets()[gameOptions.buckets - 1 - nextIndex] != 0){
                    minPossibleSteals++
                    minStonesSteals += maxSide.getBuckets()[gameOptions.buckets - 1 - nextIndex]
                }
            }
        }

        val max = if(maxPossibleSteals <= 0) 0.0 else (maxStonesSteals / maxPossibleSteals.toDouble())
        val min = if(minPossibleSteals <= 0) 0.0 else (minStonesSteals / minPossibleSteals.toDouble())
        return maxPossibleSteals.toDouble() - minPossibleSteals.toDouble()
    }

    override fun name(): List<String> {
        return  listOf("Possible steals")
    }
}