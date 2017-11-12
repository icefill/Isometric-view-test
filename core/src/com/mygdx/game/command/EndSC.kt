package com.mygdx.game.command

import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController

class EndSC(): SubCommand {
    override var name="END_ACTION"
    lateinit var model:Model

    constructor(model: Model):this() {
        this.model=model
    }

    override fun act(modelController: ModelController):Boolean {
        return true
    }
    override fun undo(modelController: ModelController):Boolean {
        return true
    }



}