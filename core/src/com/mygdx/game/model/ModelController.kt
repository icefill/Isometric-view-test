package com.mygdx.game.model

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Array
import com.mygdx.game.basics.Dir
import com.mygdx.game.command.*
import com.mygdx.game.command.CommandQueue
import com.mygdx.game.view.ViewController

class ModelController () {
    lateinit internal var modelArray:Array<Model>
    lateinit internal var cursorModel:Model
    internal var lastIndex=0
    lateinit internal var commandQueue:CommandQueue
    internal var thinking=false

    internal var modelMap = Array<Array<Array<Model>>>()
    internal var players = HashMap<Int,Model>()
    internal var currentPlayer: Model? =null
    internal var minimapArray=CharArray(70)

    constructor (map_info :String):this(){
        modelArray= Array<Model>()
        cursorModel= Model(0, 0, 1, 'C')
        commandQueue= CommandQueue()
        addModel(cursorModel)
        var lines = map_info
                .trimStart().trimEnd()
                .split(Regex("[\r\n]+"))
                .map { it.trimStart().trimEnd().split(Regex("\\s+")) }
        //player_array= Array<Array<Model>>(lines.size)
        for (i in lines.indices) {
            //player_array.add(Array<Model>(lines[i].size))
            modelMap.add(Array<Array<Model>>())
            for (j in lines[i].indices) {
                val tile_type = lines[i][j][0]
                val tile_height = lines[i][j][1].toInt() - 48
                modelMap[i].add(Array<Model>())
                if (tile_type != 'X') {
                    for (k in 0 until tile_height) {
                        Model(i, j, k, tile_type)?.let{
                            modelMap[i][j].add(it)
                            addModel(it)
                        }

                    }
                }
                if (lines[i][j].length>2) {
                    val obj_type = lines[i][j][2]
                    Model(i, j, modelMap[i][j].size, obj_type)?.let {
                        players.put(10*(it.pos.xx)+it.pos.yy,it)
                        it.height=2
                        addModel(it)
                        if (it.type=='s') currentPlayer=it
                    }

                }

            }
        }
    }

    fun addModel(model: Model) {
        modelArray.add(model)
        model.id=lastIndex++
    }
    fun getSizeXX(): Int = modelMap.size
    fun getModelXs(xx: Int): Array<Array<Model>>? {
        if (0 <= xx && xx < modelMap.size)
            return modelMap[xx]
        return null
    }
    fun getObj(xx:Int,yy:Int):Model? {
        return players[10*xx+yy]
    }
    fun getObj(model:Model):Model?{
        return players[10*(model.pos.xx)+model.pos.yy]
    }
    fun getModelXXYYs(xx: Int, yy: Int) : Array<Model>? {
        if (xx in 0 until modelMap.size && yy in 0 until modelMap[xx].size) {
            return modelMap[xx][yy]
        }
        return null
    }

    fun getModel(xx: Int, yy: Int, zz: Int): Model? {
        if (xx in 0 until modelMap.size
                && yy in 0 until modelMap[xx].size
                && zz in 0 until modelMap[xx][yy].size) {

            return modelMap[xx][yy][zz]
        }
        return null
    }

    fun getModel(xx: Int, yy: Int): Model? {
        if (xx in 0 until modelMap.size && yy in 0 until modelMap[xx].size) {
                val zzSize=modelMap[xx][yy].size
                return if (zzSize>0) modelMap[xx][yy][zzSize-1] else null
            }
        return null
    }
    fun getModel(modelPosition:ModelPosition) : Model? = getModel(modelPosition.xx,modelPosition.yy,modelPosition.zz)
    fun getTopModel(modelPosition:ModelPosition) :Model? = getModel(modelPosition.xx,modelPosition.yy)
    fun getAdjacentModel(model : Model, dir: Dir): Model? = getModel(model.pos + dir)
    fun getAdjacentTopModel(model: Model, dir: Dir): Model? =getTopModel(model.pos+dir)

    fun act(): Command? {
        var tempCommand: Command?=null
        currentPlayer?.apply{
            if (command?.undoing ?:false) {
                command?.undo(this@ModelController)
            }
            else {
                command?.act(this@ModelController)
                //command?.let { commandQueue.enQueue(it) }
            }
            tempCommand=command
            command =null
        }
        return tempCommand
    }
    fun cursorAct(): SubCommand? {
        var tempSubCommand: SubCommand?=null
        cursorModel?.apply{
            if (subCommand?.act(this@ModelController) == true) {
                //commandArray.add(command)
                tempSubCommand = subCommand
            }
            subCommand =null
        }
        return tempSubCommand
    }
    fun inputKeysToIntPair(dir :ViewController.ButtonType) :Pair<Int,Int>{
        return when (dir ) {
            ViewController.ButtonType.LU-> {return Pair(0,1)}
            ViewController.ButtonType.RD-> {return Pair(0,-1)}
            ViewController.ButtonType.LD-> {return Pair(-1,0)}
            ViewController.ButtonType.RU ->{return Pair(1,0) }
            else -> return Pair(0,0)
        }
    }
    fun receiveInput(buttonType: ViewController.ButtonType) {
            when (buttonType){
                ViewController.ButtonType.LU,
                ViewController.ButtonType.RU,
                ViewController.ButtonType.LD,
                ViewController.ButtonType.RD-> {
                    val (dxx, dyy) = inputKeysToIntPair(buttonType)
                    cursorModel.let {
                        it.subCommand = SC_MOVE(it, it.pos.xx + dxx, it.pos.yy + dyy)
                    }
                }
                ViewController.ButtonType.O -> {
                    if (this.getObj(cursorModel)!=null) {
                        currentPlayer=getObj(cursorModel)
                    }
                    else {
                        currentPlayer?.let {
                            println("Space received ${cursorModel.pos}")
                            Thread {
                                thinking=true
                                val command=C_WALK_TO(it, cursorModel.pos.xx, cursorModel.pos.yy, this).apply {
                                    undoing = false
                                    commandQueue.enQueue(this)
                                }
                                it.command=command
                                thinking=false
                            }.start()
                        }
                    }
                }
                ViewController.ButtonType.AT -> {
                    currentPlayer?.let{
                        println("rt received ${cursorModel.pos}")
                        it.command= C_ATTACK(it,getObj(cursorModel),this)
                        it.command?.let{
                            it.undoing=false
                            commandQueue.enQueue(it)
                        }
                    }
                }
                ViewController.ButtonType.U -> {
                    currentPlayer?.let{
                        println("u received")
                        it.command=commandQueue.getPreviousCommand()
                        it.command?.undoing=true
                    }
                }
                ViewController.ButtonType.R-> {
                    currentPlayer?.let{
                        println("r received")
                        it.command=commandQueue.getNextCommand()
                        it.command?.undoing=false
                    }
                }
            }
    }
    fun getMinimap() :CharArray{
        for (i in minimapArray.indices) {
            minimapArray[i]='O'
        }
        for (temp in players) {
            minimapArray[temp.key]=temp.value.type
        }
        return minimapArray
    }
}