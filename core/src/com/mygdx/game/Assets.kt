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
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.net.HttpRequestBuilder.json


typealias GdxArray<T> = com.badlogic.gdx.utils.Array<T>

class Assets : AssetManager(){
    var skin:Skin?=null

    init{
        val resolver= InternalFileHandleResolver()
        this.setLoader(Anim::class.java,AnimLoader(resolver))
    }

    fun QueueingAssets() {
        load("uiskin.atlas",TextureAtlas::class.java)
        load("wave.png", Texture::class.java)
        load("mask.png", Texture::class.java)
        load("basic.atlas", TextureAtlas::class.java)
        load("weapons.atlas", TextureAtlas::class.java)
        /*
        load("idle_dl.json", Anim::class.java)
        load("idle_dr.json", Anim::class.java)
        load("idle_ur.json", Anim::class.java)
        load("idle_ul.json", Anim::class.java)
        load("walk_dl.json", Anim::class.java)
        load("walk_dr.json", Anim::class.java)
        load("walk_ur.json", Anim::class.java)
        load("walk_ul.json", Anim::class.java)
        load("slash_dl.json", Anim::class.java)
        load("slash_dr.json", Anim::class.java)
        load("slash_ur.json", Anim::class.java)
        load("slash_ul.json", Anim::class.java)
        load("poke_dl.json", Anim::class.java)
        load("poke_dr.json", Anim::class.java)
        load("hit_dl2.json", Anim::class.java)
        load("hit_dr2.json", Anim::class.java)
        load("hit_ur2.json", Anim::class.java)
        load("hit_ul2.json", Anim::class.java)
        load("dead_dl.json", Anim::class.java)
        load("dead_dr.json", Anim::class.java)
*/
    }

    fun getUISkin(): Skin? {
        if (skin == null) {
            skin = Skin(Gdx.files.internal("uiskin.json"), get("uiskin.atlas", TextureAtlas::class.java))
        }
        return skin
    }
}

/*
class AnimLoader(resolver: FileHandleResolver) : AsynchronousAssetLoader<Anim, AnimLoader.Companion.AnimParameter>(resolver) {

    internal var anim: Anim? = null

    companion object {
        val json=Json()
        class AnimParameter : AssetLoaderParameters<Anim>()
    }
    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: AnimParameter) {
        anim = null
        anim= json.fromJson(Anim::class.java,Gdx.files.local(fileName))
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: AnimParameter): Anim? {
        val anim = this.anim
        this.anim = null
        return anim
    }

    override fun getDependencies(fileName: String, file: FileHandle, parameter: AnimParameter): GdxArray<AssetDescriptor<*>>? {
        return null
    }


}
*/
