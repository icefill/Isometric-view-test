package com.mygdx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.ObjectMap
import com.mygdx.game.Assets
import com.mygdx.game.IsoTest
import com.mygdx.game.SaveAndLoader
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController
import com.mygdx.game.view.AnchoredTextureRegion
import com.mygdx.game.view.ViewController
import java.io.FileNotFoundException
import com.badlogic.gdx.Application.ApplicationType
import com.mygdx.game.basics.InputType
import ktx.log.*

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
        debug{"Game screen init"}

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
        viewController.constructViews(modelController)

    }
    override fun hide() {
        debug{"Game screen hide"}
       }

    override fun show() {
        debug{"Game screen show"}
   }

    override fun render(delta: Float) {
         // If modelctrler is not acting,
        // set a subcommand if has command
        // else get input
        // model controller act
          if (!viewController.subCommandAct()) {
            val key=viewController.processInput()
            when (key) {
                InputType.S -> saveState("save.sav")
                InputType.L -> loadState("save.sav")
                else -> {
                    modelController.receiveInput(key)
                    viewController.receiveCommand(modelController.act())
                }
            }
        }
        viewController.receiveCursorCommand(modelController.cursorAct())
        viewController.act(modelController,delta)

    }

    override fun pause() {
        debug{"Game screen pause"}
        if (Gdx.app.type==ApplicationType.Android) saveState("temp.sav")

    }

    override fun resume() {
        debug{"Game screen resume"}
        if (Gdx.app.type==ApplicationType.Android) this.game.setScreen(game.loadingScreen)
    }

    override fun resize(width: Int, height: Int) {
        viewController.stage.getViewport().update(width, height, true)
        viewController.ui_stage.getViewport().update(width, height, true)

    }
    override fun dispose() {
        viewController.dispose()

    }

    fun saveState(name:String) {
        try {
            saveAndLoader.saveGame(modelController,name)
        } catch(e:Exception) {
            viewController.showBigMessage(("SAVE FAILED !!"))
            debug{"Save Failed"}
        } finally {
            viewController.showBigMessage("STATE SAVED AT ${Gdx.files.localStoragePath}$name")
            debug{"STATE SAVED AT ${Gdx.files.localStoragePath}$name"}
        }
    }

    fun loadState(name:String) {
        try {
            saveAndLoader.loadGame(this,name)
        } catch(e: FileNotFoundException) {
            viewController.showBigMessage("LOAD FAILED !!")
            debug{"Load failed"}
        } finally {
            viewController.showBigMessage("STATE LOADED.")
            debug{"State Loaded"}
        }
    }
    fun showMinimap() {
        var minimap=""
        val minimapArray=modelController.getMinimap()
        for (i in minimapArray.indices) {
            minimap+=minimapArray[i]
            if ((i+1) % 10 ==0) minimap+="\n"
        }
        viewController.showBigMessage(minimap)
    }

}
