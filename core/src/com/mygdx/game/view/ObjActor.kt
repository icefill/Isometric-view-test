package com.mygdx.game.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Action
import com.mygdx.game.actions.ExtActions
import com.mygdx.game.IsoTest
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.glutils.ShapeRenderer





open class ObjActor : ViewActor ,IsoTest.Subject {
    lateinit internal var region: AnchoredTextureRegion
    lateinit internal var bdry_lu: AnchoredTextureRegion
    lateinit internal var bdry_ru: AnchoredTextureRegion
    internal var is_bdry_lu = false
    internal var is_bdry_ru = false
    internal var dCenterX=0f
    internal var dCenterY=8f


    //internal var anchorX=16f
    //internal var anchorY=16f

        //lateinit internal var model: Model


    constructor (x: Float, y: Float, z: Float, region: AnchoredTextureRegion):super() {
        this.x = x
        this.y = y
        this.z = z
        //this.left_most=left_most
        //this.down_most=down_most
       // this.model = model

        this.region = region
        val atlas = TextureAtlas(Gdx.files.internal("tiles.atlas"))
        bdry_lu = AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("bdry_lu"))
        bdry_ru = AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("bdry_ru"))
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
        super.draw(batch,parentAlpha)
        batch.setColor(this.color)
        batch.draw(region, x - region.anchor_x, y - region.anchor_y + z)

        if (is_bdry_lu){
            batch.draw(bdry_lu, x - bdry_lu.anchor_x, y - bdry_lu.anchor_y + z)
        }
        if (is_bdry_ru){
            batch.draw(bdry_ru, x - bdry_ru.anchor_x, y - bdry_ru.anchor_y + z)
        }
        batch.setColor(1f,1f,1f,1f)

    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        if (touchable && this.touchable != Touchable.enabled) return null
        //return if (x >= dCenterX-width*.5f && x < dCenterX+width*.5&& y >= dCenterY-height*.5f && y < dCenterY+height*.5f) this else null
        return if ((Math.abs(x-dCenterX)/width+Math.abs(y-dCenterY)/height)<=1f) this else null
    }

    override fun setBounds(dCenterX:Float, dCenterY:Float, width: Float, height: Float) {
    if (this.dCenterX != dCenterX || this.dCenterY != dCenterY) {
        this.dCenterX = dCenterX
        this.dCenterY = dCenterY
    }
        if (this.width != width || this.height != height) {
            this.width = width
            this.height = height
            sizeChanged()
        }
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        if (!debug) return
        shapes.set(ShapeType.Line)
        shapes.color = stage.debugColor
        shapes.rect(x+dCenterX-width*.5f, y+dCenterY-height*.5f, originX, originY, width, height, scaleX, scaleY, rotation)
    }



}