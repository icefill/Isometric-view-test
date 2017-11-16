package com.mygdx.game.command

import com.mygdx.game.model.*

class C_ATTACK(): Command(){
    constructor(obj: Model, target:Model?, mc: ModelController):this(){
        obj.doToNearTops { nearModel ->
            (Model OBJ_ABOVE nearModel)?.let{ nearObj ->
               if (nearObj==target) {
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