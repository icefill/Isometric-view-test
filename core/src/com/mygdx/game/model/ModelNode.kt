package com.mygdx.game.model

class ModelNode () {
    lateinit internal var model: Model
    internal var parent: ModelNode? = null
    internal var distance=0


    constructor(model: Model, parentNode: ModelNode?=null) : this(){
        this.model=model
        parent=parentNode
    }

    tailrec fun initialize() {
        val temp=parent
        parent=null
        temp?.initialize()
    }
    tailrec fun reverse(child: ModelNode?) {
        val parent=this.parent
        this.parent=child
        if (parent!=null) {
            parent?.reverse(this)
        }
    }

}
