package game

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import player.Player

class Board(): KoinComponent {

    private val logger = KotlinLogging.logger{}
    private val gameRules by inject<GameRules>()
    private var sides = mutableMapOf(Player.MAX to Side(Player.MAX), Player.MIN to Side(Player.MIN))

    constructor(other: Board) : this() {
        sides.clear()
        sides = mutableMapOf()
        val copyPlayerOne = other.getSide(Player.MAX).getBuckets().toCollection(mutableListOf())
        val copyPlayerTwo = other.getSide(Player.MIN).getBuckets().toCollection(mutableListOf())

        val newSidePlayerOne = Side(Player.MAX)
        newSidePlayerOne.setBuckets(copyPlayerOne)
        newSidePlayerOne.setWell(other.getSide(Player.MAX).getWell())

        val newSidePlayerTwo = Side(Player.MIN)
        newSidePlayerTwo.setBuckets(copyPlayerTwo)
        newSidePlayerTwo.setWell(other.getSide(Player.MIN).getWell())

        sides[Player.MAX] = newSidePlayerOne
        sides[Player.MIN] = newSidePlayerTwo
    }

    fun getSide(player: Player): Side{
        return sides[player]!!
    }

    fun children(player: Player): MutableList<Pair<Int, Board>>{
        val list = mutableListOf<Pair<Int, Board>>()

        val moves = getSide(player).allNotEmpty()
        for (move in moves) {
            val copy = Board(this)
            copy.makeMove(player, move)
            list.add(Pair(move, copy))
        }
        return list
    }

    fun isInFinalState(): Boolean{
        val res1 = sides[Player.MAX]?.getBuckets()?.all { it -> it == 0 }!!
        val res2 = sides[Player.MIN]?.getBuckets()?.all { it -> it == 0 }!!
        return res1.or(res2)
    }

    fun makeMove(player: Player, bucket: Int, fakeMove: Boolean = false): Boolean{
        if(!sides[player]?.allNotEmpty()?.contains(bucket)!!)
            return false

        val stonesInBucket = sides[player]?.getBuckets()?.get(bucket)!!
        sides[player]?.getBuckets()?.set(bucket, 0)

        return distribute(stonesInBucket, bucket, player)
    }

    private fun distribute(stones: Int, start: Int, player: Player): Boolean{
        var toPass = stones
        var anotherOne = false
        var morePasses = false
        while (toPass > 0){
            val x = getSide(player).deposit(toPass, if(morePasses) -1 else start)
            anotherOne = anotherOne.or(x.third) // Last one in my well = bonus move
            // Stealing
            if(x.second != -1){
                val stolen = getSide(Player.swap(player)).getBuckets()[gameRules.buckets -1 - x.second]
                if(stolen != 0){
                    anotherOne = false // Stealing always ends round for that player
                    getSide(player).getBuckets()[x.second] = 0
                    getSide(Player.swap(player)).getBuckets()[gameRules.buckets - 1 - x.second] = 0
                    getSide(player).setWell(getSide(player).getWell() + stolen + 1)
                }
            }
            toPass = getSide(Player.swap(player)).deposit(x.first, -1, false).first
            morePasses = true
        }
        return anotherOne
    }

    fun moveRemaining(player: Player){
        var other = 0
        getSide(player).allNotEmpty().forEach{
            other += getSide(player).getBuckets()[it]
            getSide(player).getBuckets()[it] = 0
        }
        getSide(player).setWell(getSide(player).getWell() + other)
    }

    fun score(): Double{
        return getSide(Player.MAX).getWell().toDouble() - getSide(Player.MIN).getWell().toDouble()
    }

    override fun toString(): String {
        val max = sides[Player.MAX]?.printRow()?.length?.coerceAtLeast(sides[Player.MIN]?.printRow()?.length!!)
        val firstWell = "  | ${sides[Player.MAX]?.printColor(sides[Player.MAX]?.getWell().toString())} |  "
        val secondWell = "  | ${sides[Player.MIN]?.printColor(sides[Player.MIN]?.getWell().toString())} |  "
        val firstWellLength = "  | ${sides[Player.MAX]?.getWell()} |  "
        val secondWellLength = "  | ${sides[Player.MIN]?.getWell()} |  "
        val length = max!! + firstWellLength.length + secondWellLength.length;

        val builder = StringBuilder()
        builder.append("\nPlayer: " + sides[Player.MAX]?.printColor("MAX") + " Player: " + sides[Player.MIN]?.printColor("MIN") + " \n")
        builder.append(StringUtils.center("-".repeat(max), length))
        builder.append("\n")
        builder.append(StringUtils.center(sides[Player.MIN]?.printRowReverse(), length))
        builder.append("\n")
        builder.append(StringUtils.center(secondWell + "-".repeat(max) + firstWell, length))
        builder.append("\n")
        builder.append(StringUtils.center(sides[Player.MAX]?.printRow(), length))
        builder.append("\n")
        builder.append(StringUtils.center("-".repeat(max), length))
        builder.append("\n")
        return builder.toString()
    }
}