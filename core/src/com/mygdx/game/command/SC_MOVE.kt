package com.mygdx.game.command

import com.mygdx.game.basics.Dir
import com.mygdx.game.model.ModelPosition
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController

class SC_MOVE() : SubCommand {
    override var name="MOVE"
    lateinit var tileBefore: Model
    lateinit var tileToMove: Model
    lateinit var model: Model
    lateinit var from:ModelPosition
    lateinit var to:ModelPosition

    constructor(model: Model, xx:Int, yy:Int):this() {
        this.model=model
        to=ModelPosition(xx,yy)
    }
    constructor(model: Model, pos:ModelPosition):this(model,pos.xx,pos.yy)

    override fun act(modelController: ModelController) :Boolean {
        from=ModelPosition(model.pos.xx,model.pos.yy,model.pos.zz)
        modelController.getAdjacentModel(model, Dir.BL)?.let {
            tileBefore = it
            modelController.getModelXXYYs(to.xx, to.yy)?.let {
                        if (it.size > 0) {
                            to = ModelPosition(to.xx, to.yy, it.size)
                            tileToMove=modelController.getModel(to.xx,to.yy,to.zz-1) ?: tileBefore
                            model.setPosition(to,modelController)
                            return true
                        }
            }
        }
        return false
    }
    override fun undo(modelController: ModelController) :Boolean{
        model.setPosition(from,modelController)
        return true
    }
}