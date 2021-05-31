package engine.game

import engine.algorithms.Algo
import engine.algorithms.Minimax
import engine.algorithms.MinimaxWithAlphaBeta
import engine.heuristics.BoardValue
import engine.heuristics.Heuristic
import engine.player.AI
import engine.player.ConsolePlayer
import engine.player.Player
import engine.player.PlayerInterface

data class Settings(
    var heuristic: Heuristic = BoardValue(),
    var heuristics: List<Heuristic> = listOf(),
    var algo: Algo = Minimax(),
    var depth: Int = 4,
    var algoList: MutableList<String> = mutableListOf("Minimax", "Alpha beta"),
    var playersList: MutableList<String> = mutableListOf("Player", "AI"),
    var maxDepth: Int = 12,
    var ai: PlayerInterface = AI(algo)
){
    fun setAlgo(a: String){
        algo = when(a){
            "Minimax" -> Minimax()
            "Alpha beta" -> MinimaxWithAlphaBeta()
            else -> Minimax()
        }
    }

    fun ai(): PlayerInterface{
        return AI(algo)
    }

    fun player(): PlayerInterface{
        return ConsolePlayer(algo)
    }

}