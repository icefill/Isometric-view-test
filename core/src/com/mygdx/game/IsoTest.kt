package com.mygdx.game

import com.badlogic.gdx.*
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
import com.mygdx.game.screen.GameScreen
import com.mygdx.game.screen.LoadingScreen
import com.mygdx.game.view.ViewController

class IsoTest : Game(){

    lateinit var gameScreen:GameScreen
    lateinit var loadingScreen: LoadingScreen
    var assets= Assets()

    override fun create() {
        loadingScreen=LoadingScreen(this,assets)
        gameScreen=GameScreen(this,assets)

        Gdx.app.logLevel= Application.LOG_DEBUG
        this.setScreen(gameScreen)
    }


    interface Subject {
        fun inform(dxx: Int,dyy: Int,dzz: Int)
    }


}


