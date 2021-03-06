package com.mygdx.game.view

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.GL30.*
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Action
import com.mygdx.game.Assets
import com.mygdx.game.anim.Anim
import com.mygdx.game.basics.Dir
import com.mygdx.game.anim.ViewUnit
import com.mygdx.game.IsoTest
import com.mygdx.game.actions.*
import com.mygdx.game.actions.ExtActions.*
import com.mygdx.game.anim.JsonAnimLoader
import ktx.log.debug

open class CharActor : ViewActor ,IsoTest.Subject {
    lateinit internal var wave: Texture
    internal var is_on_water = false
    internal var offsetX=0
    internal var offsetY=4
    internal var actions= ExtActions()

    lateinit internal var anims:MutableMap<String,Array<Anim>>
    internal var currentAnim:Array<Anim>?=null
    lateinit internal var viewUnits:Array<ViewUnit?>
    internal var dir= Dir.DL
    var reserveViewUnit:ViewUnit?=null
    val USE_SHADER=true


    companion object {
        lateinit var mask_shader: ShaderProgram
        internal var fboBatch = SpriteBatch()
        internal var mask: Texture = Texture(Gdx.files.internal("mask.png"))


        //val jsonAnimLoader= JsonAnimLoader()


        val fbo = FrameBuffer(Pixmap.Format.RGBA4444, 40, 60, false)

        init {

            fbo.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            val vert = """
            attribute vec4 ${ShaderProgram.POSITION_ATTRIBUTE};
            attribute vec4 ${ShaderProgram.COLOR_ATTRIBUTE};
            attribute vec2 ${ShaderProgram.TEXCOORD_ATTRIBUTE}0;

            uniform mat4 u_projTrans;
            varying vec4 vColor;
            varying vec2 vTexCoord;

            void main() {
                vColor= ${ShaderProgram.COLOR_ATTRIBUTE};
                vTexCoord=${ShaderProgram.TEXCOORD_ATTRIBUTE}0;
                gl_Position= u_projTrans* ${ShaderProgram.POSITION_ATTRIBUTE};
            }
        """
            val frag = """
            #ifdef GL_ES
                precision mediump float;
            #endif
            varying vec4 v_color;
            varying vec2 vTexCoord;
            uniform sampler2D u_texture;
            uniform sampler2D u_mask;

            void main(void) {
                vec4 texColor0=texture2D(u_texture,vTexCoord);
                float mask=texture2D(u_mask,vTexCoord).a;
                if (mask>0.5) {
                    gl_FragColor = texColor0;
                }
                else {
                    gl_FragColor= vec4(0.0,0.0,0.0,0.0);
                }
            }
        """
            ShaderProgram.pedantic = false

            mask_shader = ShaderProgram(vert, frag)
            if (!mask_shader.isCompiled()) {
                System.exit(0)
            }
            if (mask_shader.getLog().length != 0)
                debug{mask_shader.getLog()}

            mask_shader.begin()
            mask_shader.setUniformi("u_mask", 1)
            mask_shader.end()
            mask.bind(1)

            Gdx.gl.glActiveTexture(GL_TEXTURE0)
            val projectionMatrix = Matrix4().setToOrtho2D(0f,0f,40.0f,60.0f)
            fboBatch.projectionMatrix=projectionMatrix

        }


    }

    //lateinit internal var model: Model

    constructor (x: Float, y: Float, z: Float,assets: Assets):super() {
        this.x = x
        this.y = y
        this.z = z

        wave= assets.get("wave.png",Texture::class.java)

        viewUnits= arrayOfNulls<ViewUnit?>(15)
        setXX()
        setYY()
        d=xx+yy
        val idle= arrayOf(
                assets.get(Assets.ANIM_PATH+"idle_dl.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"idle_dr.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"idle_ur.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"idle_ul.json",Anim::class.java)
        )
        val walk= arrayOf(
                assets.get(Assets.ANIM_PATH+"walk_dl.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"walk_dr.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"walk_ur.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"walk_ul.json",Anim::class.java)
        )
        val attack=arrayOf(
                assets.get(Assets.ANIM_PATH+"slash_dl.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"slash_dr.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"slash_ur.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"slash_ul.json",Anim::class.java).apply{UNREPEAT=true}
        )

        val poke=arrayOf(
                assets.get(Assets.ANIM_PATH+"poke_dl.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"poke_dr.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"poke_dr.json",Anim::class.java)
                ,assets.get(Assets.ANIM_PATH+"poke_dl.json",Anim::class.java)
        )

        val hit=arrayOf(
                assets.get(Assets.ANIM_PATH+"hit_dl2.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"hit_dr2.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"hit_ur2.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"hit_ul2.json",Anim::class.java).apply{UNREPEAT=true}
        )
        val dead=arrayOf(
                assets.get(Assets.ANIM_PATH+"dead_dl.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"dead_dr.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"dead_dr.json",Anim::class.java).apply{UNREPEAT=true}
                ,assets.get(Assets.ANIM_PATH+"dead_dl.json",Anim::class.java).apply{UNREPEAT=true}

        )

        anims=mutableMapOf("WALK" to walk, "IDLE" to idle,"ATTACK" to attack, "HIT" to hit, "DEAD" to dead)
        currentAnim =anims["IDLE"]
    }
    override fun setXX() {
        xx = ViewController.toXX(x+offsetX, y-offsetY )

    }
    override fun setYY() {
        yy = ViewController.toYY(x+offsetX , y-offsetY)
    }
    fun setAnim(animName: String) {
        this.currentAnim =anims[animName]?: currentAnim
    }
    fun walkToAction (xx: Int, yy:Int,zz:Int) : Action {
        return execute{this.setAnim("WALK")} WITH
                moveTo(ViewController.toX(xx, yy)
                        , ViewController.toY(xx, yy), zz * 8f,.3f)

    }
    fun jumpToAction (xx: Int, yy:Int,zz:Int) : Action {
        return  execute{this.setAnim("IDLE")} WITH
                jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                .4f)

    }
    fun jumpToVoidAction (xx: Int, yy:Int,zz:Int) : Action {
        return jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                1.1f)
    }
    fun fallToAction (xx: Int, yy:Int,zz:Int) : Action {
        return  execute{this.setAnim("IDLE")} WITH
                jumpTo(ViewController.toX(xx, yy)
                , ViewController.toY(xx, yy)
                , zz * 8f,
                .3f) NEXT
                delay(.15f)

    }

    override fun inform(dxx:Int, dyy:Int, dzz:Int) {
    }
    override fun positionChanged() {
        setXX()
        setYY()
        d=xx+yy
    }
    override fun act(delta:Float) {
        super.act(delta)
        //setXX()
        //setYY()
        //d=xx+yy
    }
    override fun draw(batch: Batch, parentAlpha: Float) {
//        currentAnim?.get(dir.v)?.draw(batch,x,y+z,deltaTime,viewUnits)

        if (is_on_water) {
            batch.setColor(1f, 1f, 1f, .7f)
            batch.draw(wave, x - 15f, y + z - 12f, 30f, 30f)
            batch.setColor(1f, 1f, 1f, 1f)
        }
            batch.end()

            fbo.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        fboBatch.begin()
            currentAnim?.get(dir.v)?.draw(fboBatch,20f,12f,deltaTime,viewUnits)
            fboBatch.end()
            fbo.end()

            val fboTexture= TextureRegion(fbo.colorBufferTexture)
            fboTexture.flip(false,true)
            batch.begin()
        if (is_on_water) {
            batch.shader = mask_shader
            batch.draw(fboTexture, x - 20, y + z - 20)
        } else {
            batch.shader=null
            batch.draw(fboTexture, x - 20, y + z - 12)
        }
        batch.shader=null

          //  fbo.dispose()
    }



}