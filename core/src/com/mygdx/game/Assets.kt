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
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.ObjectMap
import com.mygdx.game.view.AnchoredTextureRegion


typealias GdxArray<T> = com.badlogic.gdx.utils.Array<T>

class Assets : AssetManager(){
    var skin:Skin?=null
    val jsonReader= JsonReader()
    val json=Json()
    var tileTexturesMap: ObjectMap<Char, AnchoredTextureRegion>?=null

    init{
        setLoader(Anim::class.java,AnimLoader(this.fileHandleResolver))
    }
    companion object {
        const val ANIM_PATH="anim/"
    }
    fun QueueingAssets() {
        load("uiskin.atlas",TextureAtlas::class.java)
        load("tiles.atlas", TextureAtlas::class.java)
        load("wave.png", Texture::class.java)
        load("mask.png", Texture::class.java)
        load("basic.atlas", TextureAtlas::class.java)
        load("weapons.atlas", TextureAtlas::class.java)
        val animNameArray=json.fromJson(Array<String>::class.java,Gdx.files.internal("animList.json"))
        animNameArray.forEach{
            load(ANIM_PATH+it,Anim::class.java)
        }
        load("wave.png",Texture::class.java)
    }

    fun getUISkin(): Skin {
        skin?.let{return it}
        return Skin(Gdx.files.internal("uiskin.json"), get("uiskin.atlas", TextureAtlas::class.java)).apply{skin=this}
    }
    fun getTileTextureMap():ObjectMap<Char, AnchoredTextureRegion> {
        tileTexturesMap?.let{return it}
        return ObjectMap<Char, AnchoredTextureRegion>().apply{
            val atlas=get("tiles.atlas",TextureAtlas::class.java)
            val parsed= jsonReader.parse(Gdx.files.internal("tile_list.json"))
            for (jv in parsed) {
                put(jv["type"].asChar(),AnchoredTextureRegion(jv["anchorX"].asFloat(),jv["anchorY"].asFloat(),jv["offsetX"].asFloat(),jv["offsetY"].asFloat(),atlas.findRegion(jv["regionName"].asString())))
            }

        }

    }
}


class AnimLoader(resolver: FileHandleResolver) : AsynchronousAssetLoader<Anim, AnimLoader.Companion.AnimParameter>(resolver) {

    internal var anim: Anim? = null

    companion object {
        @JvmField var json=Json()
        object AnimParameter : AssetLoaderParameters<Anim>()
    }
    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: AnimParameter?) {
        anim = null
        anim= json.fromJson(Anim::class.java,Gdx.files.internal(fileName))
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: AnimParameter?): Anim? {
        var anim = this.anim
        this.anim = null
        return anim
    }

    override fun getDependencies(fileName: String, file: FileHandle, parameter: AnimParameter?): GdxArray<AssetDescriptor<*>?>? {
        return  null

    }


}


