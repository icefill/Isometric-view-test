package com.mygdx.game.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Action
import com.mygdx.game.actions.ExtActions
import com.mygdx.game.IsoTest

open class ObjActor : ViewActor ,IsoTest.Subject {
    lateinit internal var region: IsoTest.AnchoredTextureRegion
    lateinit internal var bdry_lu: IsoTest.AnchoredTextureRegion
    lateinit internal var bdry_ru: IsoTest.AnchoredTextureRegion
    internal var is_bdry_lu = false
    internal var is_bdry_ru = false
    internal var wave: Texture = Texture(Gdx.files.internal("wave.png"))

        //lateinit internal var model: Model

    constructor (x: Float, y: Float, z: Float, region: IsoTest.AnchoredTextureRegion):super() {
        this.x = x
        this.y = y
        this.z = z
        //this.left_most=left_most
        //this.down_most=down_most
       // this.model = model
        this.region = region
        val atlas = TextureAtlas(Gdx.files.internal("tiles.atlas"))
        bdry_lu = IsoTest.AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("bdry_lu"))
        bdry_ru = IsoTest.AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("bdry_ru"))
        //fbo.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        setXX()
        setYY()
        d=xx+yy
    }
    override fun setXX() {
        region?.run {
            xx = ViewController.toXX(x + offset_x, y - offset_y)
        }
    }
    override fun setYY() {
        region?.run {
            yy = ViewController.toYY(x + offset_x, y - offset_y)
        }
    }
    fun walkToAction (xx: Int, yy:Int,zz:Int) : Action {
        return ExtActions.moveTo(ViewController.toX(xx, yy)
             , ViewController.toY(xx, yy)
             , zz * 8f,
                .3f)
    }
    fun jumpToAction (xx: Int, yy:Int,zz:Int) : Action {
        return ExtActions.jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                .5f)
    }
    fun jumpToVoidAction (xx: Int, yy:Int,zz:Int) : Action {
        return ExtActions.jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                1.1f)
    }
    fun fallToAction (xx: Int, yy:Int,zz:Int) : Action {
        return ExtActions.jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                .4f)
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
        setXX()
        setYY()
        d=xx+yy
    }
    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(region, x - region.anchor_x, y - region.anchor_y + z)

        if (is_bdry_lu){
            batch.draw(bdry_lu, x - bdry_lu.anchor_x, y - bdry_lu.anchor_y + z)
        }
        if (is_bdry_ru){
            batch.draw(bdry_ru, x - bdry_ru.anchor_x, y - bdry_ru.anchor_y + z)
        }

    }



    override fun setBounds(left_most: Float, down_most: Float, width: Float, height: Float) {
        TODO()
    }

    constructor()


}