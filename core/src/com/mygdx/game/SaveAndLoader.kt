package com.mygdx.game

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.SnapshotArray
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.mygdx.game.model.Model
import com.mygdx.game.model.ModelController
import com.mygdx.game.model.ModelNode
import org.objenesis.strategy.StdInstantiatorStrategy
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SaveAndLoader {
    internal var kryo: Kryo
    init {
        kryo = Kryo()
        kryo.register(ModelController::class.java)
        kryo.register(Model::class.java)
        kryo.register(ModelNode::class.java)
        //kryo.register(Dir::class.java)

        //kryo.register(TextureRegion.class,new TextureRegionSerializer());
        kryo.instantiatorStrategy = StdInstantiatorStrategy()

        //kryo.register(LinkedList<E>.class, new JavaSerializer());
    }

    @Throws(FileNotFoundException::class)
    fun saveGame(mc:ModelController): Boolean {

        val output = Output(FileOutputStream("save.bin"))
        kryo.writeObject(output, mc)
        output.close()
        return true
    }

    @Throws(FileNotFoundException::class)
    fun loadGame(game:IsoTest) {
        val input = Input(FileInputStream("save.bin"))
        game.modelController=kryo.readObject<ModelController>(input, ModelController::class.java!!)
        input.close()
        game.viewController.modelController=game.modelController
        game.viewController.constructViews(game.tile_textures_map)
    }

}