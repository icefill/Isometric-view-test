package com.mygdx.game.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
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

open class CharActor : ViewActor ,IsoTest.Subject {
    internal var wave: Texture = Texture(Gdx.files.internal("wave.png"))
    internal var is_on_water = false
    internal var offsetX=0
    internal var offsetY=4
    internal var actions= ExtActions()

    lateinit internal var anims:MutableMap<String,Array<Anim>>
    internal var current_anim:Array<Anim>?=null
    lateinit internal var viewUnits:Array<ViewUnit?>
    internal var dir= Dir.DL
    var reserveViewUnit:ViewUnit?=null

    companion object{
        /*
        var mask_shader : ShaderProgram
        internal var fboBatch= SpriteBatch()
        internal var mask: Texture = Texture(Gdx.files.internal("mask.png"))
        */

        val jsonAnimLoader= JsonAnimLoader()

        /*
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
            };
        """
            val frag = """
            #ifdef GL_ES
            #define LOWP lowp
            #precision mediump float;
            #else
            #define LOWP
            #endif
            varying LOWP vec4 Color;
            varying vec2 vTexCoord;
            uniform sampler2D u_texture;
            uniform sampler2D u_mask;
            void main(void) {
                vec4 texColor0=texture2D(u_texture,vTexCoord);
                //vec4 texColor1=texture2D(u_mask,vTexCoord);
                float mask=texture2D(u_mask,vTexCoord).a;
                //gl_FragColor=texColor1;
                if (mask>0.5) {
                    gl_FragColor = texColor0;
                }
                else {
                    gl_FragColor= vec4(0.0,0.0,0.0,0.0);
                }

            };

        """
            ShaderProgram.pedantic = false
            mask_shader = ShaderProgram(vert, frag)
            if (!mask_shader.isCompiled()) {
                System.err.println(mask_shader.getLog())
                System.exit(0)
            }
            if (mask_shader.getLog().length != 0)
                println(mask_shader.getLog())
            mask_shader.begin()
            mask_shader.setUniformi("u_mask", 1)
            mask_shader.end()
            mask.bind(1)

            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
            val projectionMatrix = Matrix4().setToOrtho2D(0f,0f,40f,60f)
            fboBatch.projectionMatrix=projectionMatrix

        }
*/

    }

    //lateinit internal var model: Model

    constructor (x: Float, y: Float, z: Float,assets: Assets):super() {
        this.x = x
        this.y = y
        this.z = z

        viewUnits= arrayOfNulls<ViewUnit?>(15)
        setXX()
        setYY()
        d=xx+yy
        val idle= arrayOf(
                assets.get("idle_dl.json",Anim::class.java)
                //jsonAnimLoader.AnimFromJson("idle_dl.json")
                ,jsonAnimLoader.AnimFromJson("idle_dr.json")
                ,jsonAnimLoader.AnimFromJson("idle_ur.json")
                ,jsonAnimLoader.AnimFromJson("idle_ul.json")
        )
        val walk= arrayOf(
                jsonAnimLoader.AnimFromJson("walk_dl.json")
                ,jsonAnimLoader.AnimFromJson("walk_dr.json")
                ,jsonAnimLoader.AnimFromJson("walk_ur.json")
                ,jsonAnimLoader.AnimFromJson("walk_ul.json")
        )
        val attack=arrayOf(
                jsonAnimLoader.AnimFromJson("slash_dl.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("slash_dr.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("slash_ur.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("slash_ul.json").apply{UNREPEAT=true}

        )
        val poke=arrayOf(
                jsonAnimLoader.AnimFromJson("poke_dl.json")
                ,jsonAnimLoader.AnimFromJson("poke_dr.json")
                ,jsonAnimLoader.AnimFromJson("poke_dr.json")
                ,jsonAnimLoader.AnimFromJson("poke_dl.json")

        )

        val hit=arrayOf(
                jsonAnimLoader.AnimFromJson("hit_dl2.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("hit_dr2.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("hit_ur2.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("hit_ul2.json").apply{UNREPEAT=true}

        )
        val dead=arrayOf(
                jsonAnimLoader.AnimFromJson("dead_dl.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("dead_dr.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("dead_dr.json").apply{UNREPEAT=true}
                ,jsonAnimLoader.AnimFromJson("dead_dl.json").apply{UNREPEAT=true}

        )

        anims=mutableMapOf("WALK" to walk, "IDLE" to idle,"ATTACK" to attack, "HIT" to hit, "DEAD" to dead)
        current_anim=anims["IDLE"]
    }
    override fun setXX() {
        xx = ViewController.toXX(x+offsetX, y-offsetY )

    }
    override fun setYY() {
        yy = ViewController.toYY(x+offsetX , y-offsetY)
    }
    fun setAnim(animName: String) {
        this.current_anim=anims[animName]?:current_anim
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
        current_anim?.get(dir.v)?.draw(batch,x,y+z,deltaTime,viewUnits)
/*
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
            current_anim?.get(dir.v)?.draw(fboBatch,20f,12f,deltaTime,viewUnits)
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
*/
          //  fbo.dispose()
    }



    override fun setBounds(left_most: Float, down_most: Float, width: Float, height: Float) {
        TODO()
    }

    constructor()


}