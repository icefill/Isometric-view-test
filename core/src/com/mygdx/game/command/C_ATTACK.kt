package com.mygdx.game.command

import com.mygdx.game.model.*
import ktx.log.debug

class C_ATTACK(): Command(){
    constructor(obj: Model, target:Model?, mc: ModelController):this(){
        obj.doToNearTops { nearModel ->
            (Model aboveOf nearModel)?.let{ nearObj ->
               if (nearObj==target
                   //&& (target.pos.zz-nearObj.pos.zz) in -1..1
                       ) {
                   debug{"ATTACK: target is ${target.pos.zz} , ${nearObj.pos.zz} , ${target.pos.zz-nearObj.pos.zz}"}
                   addSC(
                           SC_DIR(obj, obj.pos dirTo nearObj.pos)
                           , SC_ANIM(obj, "ATTACK")
                           , SC_ANIM(nearObj, "HIT")
                           , SC_ANIM(nearObj, "DEAD", false)
                           , SC_DEAD(nearObj)
                   )
                   false
               }
           }
            true
        }

    }
}