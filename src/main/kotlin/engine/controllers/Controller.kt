package engine.controllers

import engine.game.Board
import engine.game.Stats

interface Controller {
    fun play(initialBoard: Board): Stats
}