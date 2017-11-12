package com.mygdx.game.command

import com.badlogic.gdx.Gdx
import com.mygdx.game.model.*
import java.util.*

class C_WALK_TO(): Command(){
    lateinit var initialNode:ModelNode
    lateinit var initialModel:Model
    val MAX_DIST=10

    constructor(obj: Model, toXX:Int, toYY:Int, modelController: ModelController):this(){
        Gdx.app.log("LOG1","LOGLOG")
        initialNode= ModelNode(Model BELOW_OF obj ?: obj)
        initialModel=initialNode.model

        val lookupSet= TreeSet<Int>()
        val dest= Model[toXX,toYY] ?:null
        var current=initialNode
        val queue= LinkedList<ModelNode>()

        queue.add(current)
        lookupSet.add(current.model.pos.xx*10+current.model.pos.yy)
        while (!queue.isEmpty()){
            current=queue.pollFirst()
            if (current.model==dest) break
            current.model.doToNearTops { near->
                if (near.pos.zz-current.model.pos.zz in -2..1
                        && !lookupSet.contains(near.pos.xx*10+near.pos.yy)
                        && (Model OBJ_ABOVE near ==null)
                        ) {
                    queue.add(ModelNode(near, current))
                    lookupSet.add(near.pos.xx*10+near.pos.yy)
                }
                true
            }
        }
        current.reverse(null)
        current=initialNode

        constructCommand(obj,current)
        addSC(EndSC(obj))
    }
    private tailrec fun constructCommand(obj:Model, current:ModelNode) {
        val parent=current.parent
        if (parent!=null) {
            addSC(
                    SC_DIR(obj,current.model.pos dirTo parent.model.pos)
                    ,SC_MOVE(obj,parent.model.pos)
            )
            constructCommand(obj,parent)
        }
    }
}