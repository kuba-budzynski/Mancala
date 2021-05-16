package controllers

import player.Player
import player.PlayerInterface
import java.util.*

class CoinTosser {
    companion object{
        fun toss(player1: PlayerInterface, player2: PlayerInterface): Map<Player, PlayerInterface> {
            val random = Random()
            return if(random.nextDouble() < 0.5){
                player1.setPlayer(Player.MAX)
                player2.setPlayer(Player.MIN)
                mapOf(Player.MAX to player1, Player.MIN to player2)
            }else{
                player1.setPlayer(Player.MIN)
                player2.setPlayer(Player.MAX)
                mapOf(Player.MAX to player2, Player.MIN to player1)
            }
        }
    }
}