package com.mygdx.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ObjectMap
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController
import com.mygdx.game.view.ViewController

class IsoTest : ApplicationAdapter() {
    lateinit var viewController: ViewController
    lateinit var modelController : ModelController
    val tile_textures_map = ObjectMap<Char, AnchoredTextureRegion>()
    //var json:Json=Json()
    var saveAndLoader=SaveAndLoader()

    override fun create() {
        initialize()
    }

    fun initialize() {
        Gdx.app.logLevel= Application.LOG_DEBUG
        val atlas = TextureAtlas(Gdx.files.internal("tiles.atlas"))
        val bdry_lu= AnchoredTextureRegion(16f,24f,8f,0f,atlas.findRegion("bdry_lu"))
        val bdry_ru= AnchoredTextureRegion(16f,24f,8f,0f,atlas.findRegion("bdry_ru"))

        tile_textures_map.put('S', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("stone")))
        tile_textures_map.put('D', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("dirt")))
        tile_textures_map.put('I', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("ice")))
        tile_textures_map.put('L', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("lava_d")))
        tile_textures_map.put('l', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("lava_s")))
        tile_textures_map.put('W', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("water_d")))
        tile_textures_map.put('w', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("water_s")))
        tile_textures_map.put('1', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("wall_ru")))
        tile_textures_map.put('2', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("wall_lu")))
        tile_textures_map.put('m', AnchoredTextureRegion(16f, 16f,8f,0f, atlas.findRegion("wall_m")))


        var map_data =
                """S1  S2  S2  S1  S1  S1P S4  S3
                   S1  S1  W1  W1  S1  S1  S1s S2
                   S1  S1  W1  W1s S1  S1  S1  S1
                   S1  S1  W1  W1  S1  S1E S1  S1  S1  S1
                   S1  S1  S1s S3  S2  S1  S1  S1F X0  S4E
                   S1  S2  S1  S3  S4  S4  S1  S2  S1  S1
                   m7  m7  m7  m7  m7  m7  m7  m7  m7  m7
                """
        modelController = ModelController(map_data)
        Model.mc=modelController
        viewController = ViewController()
        viewController.modelController=modelController
        viewController.constructViews(tile_textures_map)
    }
    class GameManager () {
        //CommandQueue
        //GameManager
        //ViewManager(Changeble)


    }

    override fun render() {
        // If modelctrler is not acting,
        // set a subcommand if has command
        // else get input
        // model controller act
        if (!viewController.subCommandAct()) {
            val key=viewController.processInput()
            if (key==Input.Keys.S) {saveState()}
            else if (key== Input.Keys.L) {loadState()}
            else {
                modelController.receiveInput(key)
                viewController.receiveCommand(modelController.act())
            }
        }
        viewController.receiveCursorCommand(modelController.cursorAct())
        viewController.act(modelController)

    }


    override fun dispose() {
        viewController.dispose()
    }


    class AnchoredTextureRegion : TextureRegion {
        internal var anchor_x: Float = 0f
        internal var anchor_y: Float = 0f
        internal var offset_x:Float
        internal var offset_y:Float
        internal var modifier: Float = 0f
        constructor(anchor_x: Float, anchor_y: Float,offset_x:Float,offset_y:Float, region: TextureRegion) : super(region) {
            this.anchor_x = anchor_x
            this.anchor_y = anchor_y
            this.offset_x=offset_x
            this.offset_y=offset_y

        }
    }
    interface Subject {
        fun inform(dxx: Int,dyy: Int,dzz: Int)
    }
    fun saveState() {
        saveAndLoader.saveGame(modelController)
        //val fileHandle =Gdx.files.local("save.json")
        //json.toJson(modelController,fileHandle)
    }

    fun loadState() {
        saveAndLoader.loadGame(this)
        viewController.showBigMessage("LOAD")
        //modelController=json.fromJson(ModelController::class.java, Gdx.files.local("save.json"))
        //viewController.modelController=modelController
        //viewController.constructViews(tile_textures_map)
    }



}


