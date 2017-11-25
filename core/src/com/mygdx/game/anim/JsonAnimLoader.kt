package com.mygdx.game.anim
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json


class JsonAnimLoader {
    val json = Json()

    fun AnimFromJson(fileName: String): Anim {
        return json.fromJson(Anim::class.java, Gdx.files.local(fileName)) ?: Anim()

    }

}