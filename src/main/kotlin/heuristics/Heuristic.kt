package heuristics

import game.Board

fun interface Heuristic {
    fun apply(board: Board): Double
}