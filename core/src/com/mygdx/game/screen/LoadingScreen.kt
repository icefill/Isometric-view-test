package com.mygdx.game.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.mygdx.game.Assets
import com.mygdx.game.IsoTest

class LoadingScreen: Screen {

    var assets:Assets
    var game: IsoTest
    constructor(game: IsoTest,assets: Assets) {
        this.assets=assets
        this.game=game
    }
    override fun hide() {
      }

    override fun show() {
        assets.QueueingAssets()

    }

    override fun render(delta: Float) {
        if (assets.update()) {
            this.game.gameScreen.isPaused=false
            this.game.gameScreen.initialize()
            //game.gameScreen.loadState()
            this.game.setScreen(game.gameScreen)

        }
           }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }


}