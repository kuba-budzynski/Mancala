package engine.player

data class Moves(var moves: MutableList<String> = mutableListOf()) {
    fun makeMove(player: Player, bucket: Int, stones: Int){
        moves.add("${player.name} has chosen $bucket with $stones stones")
//        moves = moves().toMutableList()
        moves = moves().toMutableList()
    }

    fun moves(): MutableList<String>{
        return moves.reversed().toMutableList();
    }

}