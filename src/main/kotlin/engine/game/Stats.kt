package engine.game

import engine.player.Player
import java.math.BigDecimal
import java.math.BigInteger

data class Stats(
    var player: String = "MAX",
    var totalMoves: Int = 0,
    var totalTime: BigInteger = BigInteger.ZERO,
    var bonusMoves: Int = 0,
    var steals: Int = 0,
    var movesToFirstBonus: Int = 0,
    var timeToFirstBonus: BigInteger = BigInteger.ZERO,
    var movesToFirstSteal: Int = 0,
    var timeToFirstSteal: BigInteger = BigInteger.ZERO,
    var score: Double = 0.0
){
    fun toSeconds(time: BigInteger): Double{
        return time.toBigDecimal().divide(BigDecimal.valueOf(1000)).toDouble()
    }
}
