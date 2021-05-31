package engine.player

import engine.algorithms.Algo
import engine.game.Board
import engine.game.GameRules
import engine.heuristics.Heuristic
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ConsolePlayer(private val algo: Algo): PlayerInterface, KoinComponent {

    private val gameRules by inject<GameRules>()
    private lateinit var player: Player
    private val input = Scanner(System.`in`)

    override fun nextMove(board: Board, fakeMove: Boolean): Int {
        return if(!fakeMove){
            val moves = board.getSide(player).allNotEmpty()
            print("${board.getSide(player).printColor(player.name)} can make moves to ($moves): ")
            var move = input.nextInt()
            while(move !in moves){
                println("Bucket $move is not a valid option")
                print("${board.getSide(player).printColor(player.name)} make move ($moves): ")
                move = input.nextInt()
            }
            move
        } else -1
    }

    override fun setPlayer(player: Player) {
        this.player = player
    }

    override fun getPlayerType(): Player {
        return player
    }
}