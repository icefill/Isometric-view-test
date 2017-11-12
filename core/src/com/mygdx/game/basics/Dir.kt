package com.mygdx.game.basics


enum class Dir(val v: Int) {

    DL(0),DR(1),UR(2),UL(3),AB(4),BL(5);
    companion object {
        fun dirToModelPosition(dir: Dir): com.mygdx.game.model.ModelPosition = when (dir) {
            DL -> com.mygdx.game.model.ModelPosition(-1, 0, 0)
            DR -> com.mygdx.game.model.ModelPosition(0, -1, 0)
            UR -> com.mygdx.game.model.ModelPosition(1, 0, 0)
            UL -> com.mygdx.game.model.ModelPosition(0, 1, 0)
            AB -> com.mygdx.game.model.ModelPosition(0, 0, 1)
            BL -> com.mygdx.game.model.ModelPosition(0, 0, -1)
        }

        fun getRandomHeadingPosition()= toDir((Math.random() * 4).toInt())

        fun toDir(i:Int) = when(i) {
            0 -> DL
            1 -> DR
            2 -> UR
            else -> UL
        }
    }
    fun opposite()= when (this) {
        DL -> UR
        DR -> UL
        UR -> DL
        UL -> DR
        AB -> BL
        BL -> AB
    }

}