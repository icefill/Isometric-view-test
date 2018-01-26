package com.mygdx.game.basics

enum class InputType(v:Int) {
    LU(0),
    RU(1),
    LD(2),
    RD(3),
    O(4),
    SA(5),
    LO(6),
    U(7),
    R(8),
    S(9),
    L(10),
    AT(11),
    CLK(12),
    NONE(13);

    fun toDir()=when(this) {
        LU -> Dir.UL
        RU -> Dir.UR
        LD -> Dir.DL
        RD -> Dir.DR
        else -> throw Exception("Cannot convert Input type $this to Dir")
    }
}