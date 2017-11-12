package com.mygdx.game.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.*
import com.mygdx.game.actions.*
import com.mygdx.game.actions.ExtActions.*
import com.mygdx.game.anim.JsonAnimLoader
import ktx.actors.*

import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mygdx.game.anim.ViewUnit
import com.mygdx.game.IsoTest

import com.mygdx.game.basics.Dir
import com.mygdx.game.command.*
import com.mygdx.game.model.ModelController
import com.mygdx.game.model.Model
import java.util.Comparator

class ViewController constructor() {
    companion object {
        const internal val TILE_HEIGHT = 16
        const internal val TILE_WIDTH = 32
        const internal val INITIAL_X=160
        const internal val INITIAL_Y=64
        const internal val TILE_Z = 8

        // move to viewController
        fun toX(xx: Int, yy: Int): Float {
            return TILE_WIDTH * .5f * (xx - yy) + INITIAL_X
        }
        fun toY(xx: Int, yy: Int): Float {
            return TILE_HEIGHT * .5f * (xx + yy) + INITIAL_Y
        }
        fun toXX(x: Float, y: Float): Int {
            return Math.round((x- INITIAL_X)/ TILE_WIDTH +(y - INITIAL_Y)/ TILE_HEIGHT)
        }
        fun toYY(x: Float, y: Float): Int {
            return Math.round((y- INITIAL_Y)/ TILE_HEIGHT -(x- INITIAL_X)/ TILE_WIDTH)
        }
    }

    internal var currentCommand: Command?=null
    internal var stage :Stage=Stage(StretchViewport(320f, 240f))
    internal var ui_stage :Stage=Stage(StretchViewport(320f, 240f))
    internal var skin=Skin(Gdx.files.internal("uiskin.json"))
    internal var status_change_label:Label
    internal var status_change_label2:Label

    lateinit internal var indicator:ViewUnit

    internal var batch = SpriteBatch()
    val fbo = FrameBuffer(Pixmap.Format.RGBA8888, 320, 240, false)
    val fboBatch=SpriteBatch()

    var wholeViewUnits: Array<ViewUnit>
    lateinit internal var cursorActor : ObjActor
    internal var subCommandActing=false

    lateinit internal var viewArray:com.badlogic.gdx.utils.Array<ViewActor>
    internal var lastIndex=0

    lateinit internal var modelController: ModelController
    val obj_comparator = ObjComparator()

    //internal var mv_pair_array: Array<Pair<Model, ObjActor>> =Array()

    init {
        fbo.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        status_change_label = Label("", skin)
        status_change_label.setSize(300f, 60f)
        status_change_label.setFontScale(1.0f)
        status_change_label2 = Label("", skin)
        status_change_label2.setSize(300f, 60f)
        status_change_label2.setFontScale(1.0f)

    }

    fun addView(obj : ViewActor) {
        viewArray.add(obj)
        obj.id=lastIndex++
    }

    fun sortObj() {
        stage.root.children.sort(obj_comparator)
    }

    class ObjComparator : Comparator<Actor> {
        override fun compare(obj1: Actor, obj2: Actor): Int {
            if (obj1 is Label) return 1
            else if (obj2 is Label) return -1
            val o1= obj1 as ViewActor
            val o2= obj2 as ViewActor
            val d= o2.d-o1.d
            val comp=o1.compareType-o2.compareType
            val dz= obj2.z-obj1.z
            return when {
                d!=0 -> d
                comp!=0 -> comp
                dz >0 -> -1
                dz <0 -> 1
                else -> 0
            }
        }

    }
    init {

        viewArray= com.badlogic.gdx.utils.Array<ViewActor>()
        val atlas= TextureAtlas("basic.atlas")
        val atlas2= TextureAtlas("weapons.atlas")

        wholeViewUnits= arrayOf(
                ViewUnit(16f,8f,8f,atlas.findRegion("head"),atlas.findRegion("feet_ur"))
                , ViewUnit(16f,8f,8f,atlas2.findRegion("armor1"),atlas2.findRegion("armor1"))
                , ViewUnit(8f,4f,4f,atlas.findRegion("hand"))
                , ViewUnit(8f,4f,4f,atlas.findRegion("knight_feet"),atlas.findRegion("knight_feet_ur"))
                , ViewUnit(32f,16f,8f,atlas2.findRegion("sword1"),atlas2.findRegion("sword1"))
                , ViewUnit(32f,16f,8f,atlas2.findRegion("sword2"),atlas2.findRegion("sword2_ur"))
                , ViewUnit(32f,16f,12f,atlas2.findRegion("shield1"),atlas2.findRegion("shield1_ur"))
                , ViewUnit(32f,16f,12f,atlas2.findRegion("shield2"),atlas2.findRegion("shield2"))
                , ViewUnit(48f,24f,12f,atlas2.findRegion("spear1"),atlas2.findRegion("spear1"))
                , ViewUnit(16f,8f,8f,atlas2.findRegion("helm1'"),atlas2.findRegion("helm1'_ur"))
                , ViewUnit(16f,8f,8f,atlas2.findRegion("helm2'"),atlas2.findRegion("helm2'_ur"))
                ,ViewUnit(16f,8f,8f,atlas.findRegion("skeleton_head"),atlas.findRegion("skeleton_head_ur"))
                , ViewUnit(16f,8f,8f,atlas.findRegion("skeleton_body"),atlas.findRegion("skeleton_body_ur"))
                , ViewUnit(8f,4f,4f,atlas.findRegion("skeleton_hand"))
                , ViewUnit(8f,4f,4f,atlas.findRegion("skeleton_feet"),atlas.findRegion("skeleton_feet_ur"))
                , ViewUnit(20f,10f,12f,atlas2.findRegion("robe1"),atlas2.findRegion("robe1_ur"))
                , ViewUnit(20f,10f,8f,atlas2.findRegion("mage_hat2"),atlas2.findRegion("mage_hat2_ur"))
                , ViewUnit(32f,16f,11f,atlas2.findRegion("wand1"),atlas2.findRegion("wand1"))
        )
        Indicator.indicator=ViewUnit(12f,6f,6f,atlas2.findRegion("inidicator"))
        ui_stage.addActor(Indicator)
    }

    fun constructViews(tile_textures_map : ObjectMap<Char, IsoTest.AnchoredTextureRegion>,modelController:ModelController=this.modelController) {
        currentCommand=null
        stage.clear()
        //batch=SpriteBatch()
        viewArray.clear()
        lastIndex=0
        subCommandActing=false
        modelController.modelArray.forEach{ model ->
            val x= toX(model.pos.xx, model.pos.yy)
            val y= toY(model.pos.xx, model.pos.yy)
            val z=(model.pos.zz * TILE_Z).toFloat()
            when (model.type) {
                'E' -> CharActor(x, y, z)
                        .apply { compareType = 2
                            viewUnits[0]=wholeViewUnits[0]
                            viewUnits[1]=wholeViewUnits[1]
                            viewUnits[2]=wholeViewUnits[2]
                            viewUnits[3]=wholeViewUnits[3]
                            viewUnits[4]=wholeViewUnits[8]
                            //viewUnits[6]=wholeViewUnits[6]
                            viewUnits[5]=wholeViewUnits[9]

                            dir=model.dir
                            when (modelController.getAdjacentModel(model, Dir.BL)?.type){
                                'W','w'-> is_on_water=true
                            }
                        }
                'P' -> CharActor(x, y, z)
                        .apply { compareType = 2
                            viewUnits[0]=wholeViewUnits[0]
                            viewUnits[1]=wholeViewUnits[1]
                            viewUnits[2]=wholeViewUnits[2]
                            viewUnits[3]=wholeViewUnits[3]
                            viewUnits[4]=wholeViewUnits[4]
                            viewUnits[6]=wholeViewUnits[6]
                            viewUnits[5]=wholeViewUnits[10]

                            dir=model.dir
                            when (modelController.getAdjacentModel(model, Dir.BL)?.type){
                                'W','w'-> is_on_water=true
                            }
                        }
                'F' -> CharActor(x, y, z)
                        .apply { compareType = 2
                            viewUnits[0]=wholeViewUnits[0]
                            viewUnits[1]=wholeViewUnits[15]
                            viewUnits[2]=wholeViewUnits[2]
                            viewUnits[3]=wholeViewUnits[3]
                            viewUnits[4]=wholeViewUnits[17]
                            //viewUnits[6]=wholeViewUnits[7]
                            viewUnits[5]=wholeViewUnits[16]

                            dir=model.dir
                            when (modelController.getAdjacentModel(model, Dir.BL)?.type){
                                'W','w'-> is_on_water=true
                            }
                        }

                's' ->CharActor(x, y, z)
                        .apply { compareType = 2
                            viewUnits[0]=wholeViewUnits[11]
                            viewUnits[1]=wholeViewUnits[12]
                            viewUnits[2]=wholeViewUnits[13]
                            viewUnits[3]=wholeViewUnits[14]
                            viewUnits[4]=wholeViewUnits[5]
                            viewUnits[6]=wholeViewUnits[7]

                            dir=model.dir
                            /*dir = when((Math.random()*4).toInt()) {
                                0-> Dir.DL
                                1-> Dir.DR
                                2-> Dir.UR
                                3-> Dir.UL
                                else-> Dir.UR
                            }*/
                            when (modelController.getAdjacentModel(model, Dir.BL)?.type){
                                'W','w'-> is_on_water=true
                            }
                        }

                'C' -> ObjActor(x, y, z
                        , IsoTest.AnchoredTextureRegion(15f, 8f, 0f, 4f
                        , TextureRegion(Texture(Gdx.files.internal("sword1.png"))))
                )
                        .apply { compareType = 2
                                cursorActor=this
                        }

                else -> ObjActor(x, y, z, tile_textures_map[model.type])
                        ?.apply {
                            compareType = 1
                            if (modelController.getAdjacentModel(model, Dir.UL) == null) is_bdry_lu = true
                            if (modelController.getAdjacentModel(model, Dir.UR) == null) is_bdry_ru = true


                        }
            }?.let {
                stage.addActor(it)
                addView(it)
            }
        }


    }
    fun processInput() :Int{
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> return Input.Keys.UP
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> return Input.Keys.DOWN
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> return Input.Keys.LEFT
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> return Input.Keys.RIGHT
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> {
                showBigMessage("MOVE")
                return Input.Keys.SPACE
            }
            Gdx.input.isKeyJustPressed(Input.Keys.U) ->{
                showBigMessage("UNDO")
                return Input.Keys.U
            }
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> {
                showBigMessage("REDO")
                return Input.Keys.R
            }
            Gdx.input.isKeyJustPressed(Input.Keys.S) -> {
                showBigMessage("SAVE")
                return Input.Keys.S
            }
            Gdx.input.isKeyJustPressed(Input.Keys.L) -> return Input.Keys.L
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) -> {
                showBigMessage("ATTCK")
                return Input.Keys.ENTER
            }
        }
       return -1
    }
    fun cursorMoveCommand(command: SC_MOVE){
        cursorActor+cursorActor.walkToAction(command.to.xx, command.to.yy, command.to.zz)
    }


    fun endActionCommand(command: EndSC) {
        val playerView = viewArray[command.model.id] as CharActor
        playerView.setAnim("IDLE")
    }
    fun changeDirReverse(command: SC_DIR) {
        val playerView = viewArray[command.model.id] as CharActor
        playerView.dir=command.dir_before
    }
    fun changeDirCommand(command: SC_DIR) {
        val playerView = viewArray[command.model.id] as CharActor
        playerView.dir=command.dir
    }
    fun deadCommand(command: SC_DEAD) {
        val playerView = viewArray[command.model.id] as CharActor
        playerView.reserveViewUnit=playerView.viewUnits[0]
        playerView.viewUnits[0]=wholeViewUnits[11]
    }
    fun deadReverse(command: SC_DEAD) {
        val playerView = viewArray[command.model.id] as CharActor
        playerView.viewUnits[0]=playerView.reserveViewUnit
        stage+playerView
        playerView.setAnim("IDLE")
    }

    fun animCommand(command: SC_ANIM) {
        subCommandActing=true
        val playerView = viewArray[command.model.id] as CharActor
        playerView.deltaTime=0f
        val animReserve=playerView.current_anim
        playerView.current_anim=playerView.anims[command.animName]
        playerView + (
                delay(playerView?.current_anim?.get(playerView.dir.v ?:0)?.animationDuration ?:0f) WITH
                execute{
                    if (command.restore) playerView.current_anim=animReserve
                    subCommandActing=false
                }
        )
    }
    fun moveReverse(command: SC_MOVE) {
        subCommandActing=true
        val playerView = viewArray[command.model.id] as CharActor
        val x= toX(command.from.xx,command.from.yy)
        val y= toY(command.from.xx,command.from.yy)
        playerView.setPosition(x,y)
        playerView.z=command.from.zz* TILE_Z.toFloat()
        playerView.addAction(
                Actions.sequence(
                    Actions.delay(.1f)
                    ,Actions.run{subCommandActing=false}
                )
        )
    }
    fun isOnLiquid(model: Model):Boolean {
        return when(model.type) {
            'W', 'w', 'L', 'l' -> true
            else -> false
        }
    }
    fun moveCommand(command: SC_MOVE) {
        val modelFrom=command.tileBefore
        val modelTo=command.tileToMove
        subCommandActing=true
        val playerView = viewArray[command.model.id] as CharActor
        val dzz= command.to.zz-command.from.zz
        when {
            isOnLiquid(modelFrom) xor isOnLiquid(modelTo) -> {
                if (isOnLiquid(modelFrom)) {
                    playerView + (
                                delay(.13f) then
                                execute{playerView.is_on_water=false} then
                                playerView.jumpToAction(command.to.xx, command.to.yy, command.to.zz)
                        )
                } else {
                    playerView + (
                            playerView.jumpToAction(command.to.xx, command.to.yy, command.to.zz) then
                            execute{playerView.is_on_water=true}
                    )
                }
            }
            else -> when{
                dzz>0 -> playerView + (playerView.jumpToAction(command.to.xx, command.to.yy, command.to.zz))
                dzz==0 -> playerView + (playerView.walkToAction(command.to.xx, command.to.yy, command.to.zz))
                else -> playerView + (playerView.fallToAction(command.to.xx, command.to.yy, command.to.zz))
            }
        }
        playerView + after(execute{subCommandActing=false})
    }

    fun receiveCommand(command: Command?) {
        if (command!=null) {
            this.currentCommand = command
        }
    }
    fun subCommandAct():Boolean{
        if (subCommandActing) {
            return true
        }
        else {
            val subCommand=currentCommand?.getNextSubCommand()
            if (subCommand!=null){
                if ((currentCommand?.undoing) ?:false) {
                    when (subCommand.name ?: "") {
                        "MOVE" -> moveReverse(subCommand as SC_MOVE)
                        "CHANGE_DIR" -> changeDirReverse(subCommand as SC_DIR)
                        "DEAD" ->deadReverse((subCommand as SC_DEAD))
                        "END_ACTION" -> {/* Do nothing */}
                    }
                } else {
                    when (subCommand.name ?: "") {
                        "MOVE" -> moveCommand(subCommand as SC_MOVE)
                        "ANIM" -> animCommand(subCommand as SC_ANIM)
                        "CHANGE_DIR" -> changeDirCommand(subCommand as SC_DIR)
                        "DEAD" ->deadCommand(subCommand as SC_DEAD)
                        "END_ACTION" -> endActionCommand(subCommand as EndSC)
                    }
                }
                return true
            }
            else {

                return false
            }
        }

        return false
    }
    fun act(mc:ModelController) {
        stage.act()
        sortObj()
       mc.currentPlayer?.let{
            val playerView = viewArray[mc.currentPlayer!!.id]
            Indicator.setPosition(playerView.x, playerView.y +playerView.z+ 30f)
        }
        if (mc.thinking && !status_change_label2.isVisible) showToggleMessage("THINKING")
        if (!mc.thinking) hideToggleMessage()
        ui_stage.act()

        //fbo.begin()
        //fboBatch.begin()
        Gdx.gl.glClearColor(0f, 0f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
        ui_stage.draw()
        //fboBatch.end()
        //fbo.end()


        //batch.begin()
        //batch.draw(fbo.colorBufferTexture,0f,0f,Gdx.graphics.getWidth().toFloat(),Gdx.graphics.getHeight().toFloat(),0f,0f,1f,1f)
        //batch.end()
    }

    fun receiveCursorCommand(subCommand: SubCommand?) {
        when (subCommand?.name ?: "") {
            "MOVE" -> cursorMoveCommand(subCommand as SC_MOVE)
        }
    }
    fun dispose() {
        batch.dispose()
    }
    fun showToggleMessage(message:String) {
        status_change_label2.setText(message)
        status_change_label2.pack()
        status_change_label2.setVisible(true)
        ui_stage + status_change_label2
        status_change_label2.setPosition(ui_stage.width - status_change_label.width - 20f, ui_stage.height - status_change_label.getHeight() - 10f)
        status_change_label2.addAction(A.fadeIn(.2f))
    }
    fun hideToggleMessage() {
        status_change_label2.setVisible(false)
        status_change_label2.remove()
    }

    fun showBigMessage(message: String) {
        status_change_label.setText(message)
        status_change_label.pack()
        status_change_label.isVisible = true
        ui_stage + status_change_label
        status_change_label.setPosition(ui_stage.width -status_change_label.width-20f,status_change_label.height +10f)
        status_change_label+(
                        fadeIn(.2f) NEXT
                        delay(1.2f) NEXT
                        fadeOut(.1f) NEXT
                        removeActor()
                )


    }
    object Indicator:Actor() {
        lateinit var indicator:ViewUnit
        override fun draw(batch: Batch, parentAlpha:Float) {
            indicator.draw(Dir.DL,batch,x,y,0f)
        }

    }

}