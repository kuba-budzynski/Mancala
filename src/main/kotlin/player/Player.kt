package player

enum class Player(index: Int) {
    MAX(0),
    MIN(1);

    companion object{
        fun swap(p: Player): Player{
            return when(p){
                Player.MAX -> Player.MIN
                Player.MIN -> Player.MAX
            }
        }
    }
}