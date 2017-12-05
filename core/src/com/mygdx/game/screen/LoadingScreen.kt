package com.mygdx.game.screen

import com.badlogic.gdx.Screen
import com.mygdx.game.Assets
import com.mygdx.game.IsoTest

class LoadingScreen: Screen {

    var assets:Assets
    var game: IsoTest
    var onStartup=true
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
            this.game.gameScreen.initialize()
             this.game.setScreen(game.gameScreen)
            if (onStartup) {
                onStartup=false
            }
            else {
                this.game.gameScreen.loadState("temp.sav")
            }

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