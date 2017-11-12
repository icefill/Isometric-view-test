package com.mygdx.game.command

import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController

class SC_DEAD(): SubCommand {
    override var name="DEAD"
    lateinit var model: Model

    constructor(model: Model):this(){
        this.model=model
    }
    override fun act(modelController: ModelController):Boolean {
        model.kill(modelController)
        return true
    }
    override fun undo(modelController: ModelController):Boolean {
        model.revive(modelController)
       return true
    }



}