package controllers

import game.Board
import player.Player

interface Controller {
    fun play(initialBoard: Board)
}