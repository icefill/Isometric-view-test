package com.mygdx.game.command

import com.mygdx.game.model.ModelController

interface SubCommand {
    var name: String
    fun act (modelController: ModelController):Boolean
    fun undo (modelController: ModelController):Boolean
}