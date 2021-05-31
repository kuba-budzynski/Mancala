package engine.game
import engine.player.Player

data class Move(
    val player: Player,
    val bucket: Int,
    val stones: Int,
    val time: Long,
    val wasBonus: Boolean = false,
    val steals: Boolean = false
)
