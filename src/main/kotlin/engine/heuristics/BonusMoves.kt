package engine.heuristics

import engine.game.Board
import engine.game.GameRules
import engine.player.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BonusMoves: Heuristic, KoinComponent {

    private val gameOptions by inject<GameRules>()

    override fun apply(board: Board): Double {
        val maxSide = board.getSide(Player.MAX)
        val minSide = board.getSide(Player.MIN)

        var maxPossibleBonuses = 0
        for(i in 0 until maxSide.getBuckets().size){
            val stones = maxSide.getBuckets()[i]
            val nextIndex = i + stones
            if(nextIndex == gameOptions.buckets)  maxPossibleBonuses++
        }

        var minPossibleBonuses = 0
        for(i in 0 until minSide.getBuckets().size){
            val stones = minSide.getBuckets()[i]
            val nextIndex = i + stones
            if(nextIndex == gameOptions.buckets)  minPossibleBonuses++
        }

        return maxPossibleBonuses.toDouble() - minPossibleBonuses.toDouble()
    }

    override fun name(): List<String> {
        return listOf("Bonus moves")
    }
}