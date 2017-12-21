package com.mygdx.game.model


import com.badlogic.gdx.utils.Pool
import com.mygdx.game.basics.Dir
import com.mygdx.game.command.Command
import com.mygdx.game.command.SubCommand


// model : interface
// tileModel
// charModel (Every interactable things)
// map -> like before, but water tile?
// model should have height?
// projectile weapon ?



class Model()  {
    internal var pos: ModelPosition= ModelPosition()
    internal var type: Char = 'A'
    internal var id: Int =-1
    internal var command: Command?=null
    internal var subCommand: SubCommand?=null
    internal var dir= Dir.DL
    internal var height=1
    internal var isAlive=true

    constructor(xx: Int, yy: Int, zz: Int, type: Char):this(){
        this.pos.xx = xx
        this.pos.yy = yy
        this.pos.zz = zz
        this.type = type
        dir= Dir.getRandomHeadingPosition()
    }
    companion object {
        lateinit var mc :ModelController
        infix fun EAST_OF(model: Model) = mc.getAdjacentModel(model, Dir.DR)
        infix fun WEST_OF(model: Model) = mc.getAdjacentModel(model, Dir.UL)
        infix fun SOUTH_OF(model: Model) = mc.getAdjacentModel(model, Dir.DL)
        infix fun NORTH_OF(model: Model) = mc.getAdjacentModel(model, Dir.UR)
        infix fun eastTopOf(model: Model) = mc.getAdjacentTopModel(model, Dir.DR)
        infix fun westTopOf(model: Model) = mc.getAdjacentTopModel(model, Dir.UL)
        infix fun southTopOf(model: Model) = mc.getAdjacentTopModel(model, Dir.DL)
        infix fun northTopOf(model: Model) = mc.getAdjacentTopModel(model, Dir.UR)
        infix fun ABOVE_OF(model:Model)= mc.getAdjacentTopModel(model, Dir.AB)
        infix fun BELOW_OF(model:Model)= mc.getAdjacentTopModel(model, Dir.BL)

        infix fun aheadOf(model: Model) =mc.getAdjacentModel(model,model.dir)
        infix fun behindOf(model:Model) =mc.getAdjacentModel(model,model.dir.opposite())
        infix fun aboveOf(model:Model)= mc.getObj(model)

        operator fun get(pos:ModelPosition) = mc.getModel(pos)
        operator fun get(xx:Int,yy:Int) = mc.getModel(xx,yy)

    }
    inline fun doToNears(func :(Model)-> Boolean ):Boolean {
        if ((Model EAST_OF this)?.let{func(it)} != false)
            if ((Model SOUTH_OF this)?.let{func(it)} !=false)
                if ((Model WEST_OF this)?.let{func(it)}!=false)
                    if ((Model NORTH_OF this)?.let{func(it)}!=false)
                true
        return false
    }
    inline fun doToNearTops(func :(Model)-> Boolean ):Boolean {
        if ((Model eastTopOf this)?.let{func(it)} != false)
            if ((Model southTopOf this)?.let{func(it)} !=false)
                if ((Model westTopOf this)?.let{func(it)}!=false)
                    if ((Model northTopOf this)?.let{func(it)}!=false)
                        true
        return false
    }


    fun setPosition(to:ModelPosition,mc:ModelController) {
        if (this!=mc.cursorModel)
        mc.players.remove(this.pos.xx*10+this.pos.yy)
        this.pos.xx=to.xx
        this.pos.yy=to.yy
        this.pos.zz=to.zz
        if (this!=mc.cursorModel)
        mc.players.put(to.xx*10+to.yy,this)

    }
    fun kill(mc:ModelController){
        mc.players.remove(this.pos.xx*10+this.pos.yy)
        isAlive=false
    }
    fun revive(mc:ModelController){
        mc.players.put(this.pos.xx*10+this.pos.yy,this)
        isAlive=true
    }
}


data class ModelPosition (var xx:Int=0,var yy:Int=0,var zz:Int=0): Pool.Poolable
{

    companion object {
        private val mpPool = object : Pool<ModelPosition>() {
            override fun newObject(): ModelPosition {
                return ModelPosition()
            }
            fun obtain (xx:Int,yy:Int,zz:Int) :ModelPosition{
                return this.obtain().apply {
                    this.xx = xx
                    this.yy = yy
                    this.zz = zz
                }
            }
        }
        operator fun invoke() {
            mpPool.obtain()
        }


    }

    override fun reset() {
        xx=0
        yy=0
        zz=0
    }

    constructor(model :Model): this(model.pos.xx,model.pos.yy,model.pos.zz)
    operator fun plusAssign(another:ModelPosition) {
        this.xx+=another.xx
        this.yy+=another.yy
        this.zz+=another.zz
    }
    operator fun minusAssign(another:ModelPosition) {
        this.xx-=another.xx
        this.yy-=another.yy
        this.zz-=another.zz
    }
    operator fun minus(another:ModelPosition)=
            ModelPosition(this.xx-another.xx,this.yy-another.yy,this.zz-another.zz)

    operator fun plus(another:ModelPosition)=
            ModelPosition(this.xx+another.xx,this.yy+another.yy,this.zz+another.zz
            )
    operator fun plus(dir: Dir) = plus(Dir.dirToModelPosition(dir))

    infix fun dirTo(to:ModelPosition) : Dir {
        return getDirection(this,to)
    }

    fun getDirection(from:ModelPosition,to:ModelPosition,model:Model?=null)   : Dir {
        val d=to-from
        return when{
            d.xx<0 && d.yy==0 -> Dir.DL
            d.xx>0 && d.yy==0 -> Dir.UR
            d.xx==0 && d.yy>0 -> Dir.UL
            d.xx==0 && d.yy<0 -> Dir.DR
            else -> model?.dir ?: Dir.DL
        }
    }


}
