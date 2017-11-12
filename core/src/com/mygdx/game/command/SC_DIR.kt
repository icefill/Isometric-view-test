package com.mygdx.game.command

import com.mygdx.game.basics.Dir
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController

class SC_DIR(): SubCommand {
    override var name="CHANGE_DIR"
    lateinit var model:Model
    lateinit var dir: Dir
    lateinit var dir_before: Dir

    constructor(model: Model, dir: Dir):this(){
        this.model=model
        this.dir=dir
    }
    override fun act(modelController: ModelController):Boolean {
        dir_before=model.dir
        if (model.dir!= dir) {
            model.dir = dir
            return true
        }
        else
            return false

    }
    override fun undo(modelController: ModelController):Boolean {
        model.dir=dir_before
        return true
    }



}