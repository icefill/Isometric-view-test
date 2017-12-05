package com.mygdx.game.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.IsoTest

open class ViewActor : Actor, IsoTest.Subject {
    internal var xx=0
    internal var yy=0
    internal var d=0
    internal var compareType=0
    internal var id=0
    internal var deltaTime=Math.random().toFloat()
    var z: Float = 0f
        get() = field
    //internal var offset_x: Float = 0f
    //internal var offset_y: Float = 0f
    internal var order: Float = 0f

    companion object{

    }

    //lateinit internal var model: Model

    constructor (x: Float, y: Float, z: Float) {
        Gdx.app.log("MYTAG","WALK COMMAND INITIATED")
        this.x = x
        this.y = y
        this.z = z
        //this.left_most=left_most
        //this.down_most=down_most
       // this.model = model
        //fbo.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        setXX()
        setYY()
        d=xx+yy
    }
    open fun setXX() {
            xx = ViewController.toXX(x, y)
    }
    open fun setYY() {
            yy = ViewController.toYY(x, y)
    }

    override fun inform(dxx:Int, dyy:Int, dzz:Int) {
    }
    override fun positionChanged() {
        //setXX()
        //setYY()
        //d=xx+yy
    }
    override fun act(delta:Float) {
    super.act(delta)
        deltaTime+=Gdx.graphics.getDeltaTime()
        setXX()
        setYY()
        d=xx+yy
    }




    constructor()


}