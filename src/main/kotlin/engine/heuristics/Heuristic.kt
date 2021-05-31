package engine.heuristics

import engine.game.Board

interface Heuristic {
    fun apply(board: Board): Double
    fun name(): List<String>
}