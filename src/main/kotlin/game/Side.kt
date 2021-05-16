package game

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import player.Player
import java.lang.StringBuilder

class Side(private val player: Player): KoinComponent, Cloneable {

    private val logger = KotlinLogging.logger{}
    private val gameRules by inject<GameRules>()
    private var buckets = MutableList(gameRules.buckets){ _ -> gameRules.stones}
    private var well: Int = 0

    fun allNotEmpty(): List<Int>{
        return buckets.mapIndexed{ index, value -> if(value != 0) index else null}.filterNotNull()
    }

    fun deposit(stones: Int, start: Int = 0, mySide: Boolean = true): Triple<Int, Int, Boolean>{ // <Leftovers, Index to steal, Bonus move>

        if(stones <= 0) return Triple(0, -1, false)
        var stealStones = false
        if(mySide && (start + stones) < gameRules.buckets){
            if(buckets[start + stones] == 0) stealStones = true
        }

        var innerStones = stones
        if(start == buckets.size-1){
            well++
            return Triple(innerStones - 1, if(stealStones) start + 1 + stones else -1, stones == 1)
        }
        var i = start
        for(bucket in buckets.subList(start + 1 , (buckets.size).coerceAtMost(start + 1 + innerStones))){
            buckets[++i] = bucket + 1
            innerStones--
        }

        return if(innerStones > 0 && mySide){
            well++
            Triple(innerStones - 1, if(stealStones) i else -1, innerStones == 1)
        } else Triple(innerStones, if(stealStones) i else -1, false)
    }

    fun getBuckets(): MutableList<Int>{
        return buckets
    }

    fun getWell(): Int{
        return well
    }

    fun setBuckets(new: MutableList<Int>){
        buckets = new
    }

    fun setWell(stones: Int){
        well = stones
    }

    fun hasNextMove(): Boolean{
        return buckets.any { it -> it != 0 }
    }

    fun printRow(): String{
        val builder = StringBuilder()
        for(h in buckets){
            builder.append("|${StringUtils.center("$h", 5)}")
        }
        builder.append("|")
        return builder.toString()
    }

    fun printRowReverse(): String{
        val builder = StringBuilder()
        builder.append("|")
        for(h in buckets.reversed()) {
            builder.append("${StringUtils.center("$h", 5)}|")
        }
        return builder.toString()
    }

    fun printColor(msg: String): String{
        return when(player){
            Player.MAX -> gameRules.playerMax + msg + gameRules.reset
            Player.MIN -> gameRules.playerMin + msg + gameRules.reset
        }
    }
}