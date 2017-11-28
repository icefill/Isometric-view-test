package com.mygdx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.ObjectMap
import com.mygdx.game.Assets
import com.mygdx.game.IsoTest
import com.mygdx.game.SaveAndLoader
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController
import com.mygdx.game.view.AnchoredTextureRegion
import com.mygdx.game.view.ViewController

class GameScreen : Screen {
    lateinit var viewController: ViewController
    lateinit var modelController : ModelController
    val tile_textures_map = ObjectMap<Char, AnchoredTextureRegion>()
    var assets:Assets
    var game: IsoTest
    var saveAndLoader= SaveAndLoader()
    var isPaused=true

    constructor(game: IsoTest,assets: Assets) {
        this.assets=assets
        this.game=game
     }

    fun initialize(){
       val atlas = TextureAtlas(Gdx.files.internal("tiles.atlas"))
        val bdry_lu= AnchoredTextureRegion(16f, 24f, 8f, 0f, atlas.findRegion("bdry_lu"))
        val bdry_ru= AnchoredTextureRegion(16f, 24f, 8f, 0f, atlas.findRegion("bdry_ru"))

        tile_textures_map.put('S', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("stone")))
        tile_textures_map.put('D', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("dirt")))
        tile_textures_map.put('I', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("ice")))
        tile_textures_map.put('L', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("lava_d")))
        tile_textures_map.put('l', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("lava_s")))
        tile_textures_map.put('W', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("water_d")))
        tile_textures_map.put('w', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("water_s")))
        tile_textures_map.put('1', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("wall_ru")))
        tile_textures_map.put('2', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("wall_lu")))
        tile_textures_map.put('m', AnchoredTextureRegion(16f, 16f, 8f, 0f, atlas.findRegion("wall_m")))


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
        viewController = ViewController(assets)
        viewController.modelController=modelController
        viewController.constructViews(tile_textures_map)

    }
    override fun hide() {
       }

    override fun show() {
        if (isPaused)
            this.game.setScreen(game.loadingScreen)
    }

    override fun render(delta: Float) {
        // If modelctrler is not acting,
        // set a subcommand if has command
        // else get input
        // model controller act
          if (!viewController.subCommandAct()) {
            val key=viewController.processInput()
            when (key) {
                Input.Keys.S -> saveState()
                Input.Keys.L -> loadState()
                else -> {
                    modelController.receiveInput(key)
                    viewController.receiveCommand(modelController.act())
                }
            }
        }
        viewController.receiveCursorCommand(modelController.cursorAct())
        viewController.act(modelController)

    }

    override fun pause() {
        isPaused=true
        saveState()
    }

    override fun resume() {
            this.game.setScreen(game.loadingScreen)
    }

    override fun resize(width: Int, height: Int) {
    }
    override fun dispose() {
        viewController.dispose()

    }
    fun saveState() {
        try {
            saveAndLoader.saveGame(modelController)
        } catch(e:Exception) {
            viewController.showBigMessage(("SAVE FAILED !!"))
        } finally {
            viewController.showBigMessage("STATE SAVED.")
        }
        //val fileHandle =Gdx.files.local("save.json")
        //json.toJson(modelController,fileHandle)
    }

    fun loadState() {
        try {
            saveAndLoader.loadGame(this)
        } catch(e:Exception) {
            viewController.showBigMessage("LOAD FAILED !!")
        } finally {
            viewController.showBigMessage("STATE LOADED.")
        }
        //modelController=json.fromJson(ModelController::class.java, Gdx.files.local("save.json"))
        //viewController.modelController=modelController
        //viewController.constructViews(tile_textures_map)
    }

}
