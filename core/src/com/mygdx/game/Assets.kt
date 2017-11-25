package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Json
import com.mygdx.game.anim.Anim
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.assets.AssetDescriptor



class Assets : AssetManager(){

    fun QueueingAssets() {
       load("uiskin.json", Skin::class.java)
        load("wave.png",Texture::class.java)
        load("mask.png",Texture::class.java)
        load("basic.atlas", TextureAtlas::class.java)
        load("weapons.atlas",TextureAtlas::class.java)

    }


}
/*
class PixmapLoader(resolver: FileHandleResolver) : AsynchronousAssetLoader<Pixmap, PixmapLoader.PixmapParameter>(resolver) {

    internal var pixmap: Pixmap? = null

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: PixmapParameter) {
        pixmap = null
        pixmap = Pixmap(file)
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: PixmapParameter): Pixmap? {
        val pixmap = this.pixmap
        this.pixmap = null
        return pixmap
    }

    override fun getDependencies(fileName: String, file: FileHandle, parameter: PixmapParameter): Array<AssetDescriptor<*>>? {
        return null
    }

    class PixmapParameter : AssetLoaderParameters<Pixmap>()
}
        */