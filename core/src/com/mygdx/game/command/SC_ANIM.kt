package com.mygdx.game.command

import com.mygdx.game.anim.Anim
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController

class SC_ANIM(): SubCommand {
    override var name="ANIM"
    lateinit var model:Model
    lateinit var animName:String
    lateinit var animBefore:Array<Anim>
    var restore:Boolean=true

    constructor(model: Model, anim:String,restore:Boolean=true):this(){
        this.model=model
        this.animName =anim
        this.restore=restore
    }
    override fun act(modelController: ModelController):Boolean {
        return true

    }
    override fun undo(modelController: ModelController):Boolean {
        return true
    }



}