package com.mygdx.game.command

import com.mygdx.game.model.ModelController
import java.util.*

abstract class Command() {
    // model controller execute all subcommand in the command object
    // return command object
    // view controller receive command and act sequentially coresponding act.
    val subCommandQueue = LinkedList<SubCommand>()
    var _currentIndex=-1
    var undoing=false

    fun getNextSubCommand(): SubCommand?{
        return if (!undoing) subCommandQueue.getOrNull(++_currentIndex)
                else subCommandQueue.getOrNull(--_currentIndex)
    }
    fun getcurrentSubCommand(): SubCommand?{
        return subCommandQueue.getOrNull(_currentIndex)
    }
    fun act(modelController: ModelController) :Boolean{
        undoing=false
        _currentIndex=-1
        for (subCommand in subCommandQueue) {
            subCommand.act(modelController)
        }
        return true
    }
    fun undo(modelController:ModelController) :Boolean{
        undoing=true
        _currentIndex=subCommandQueue.size
        for (i in subCommandQueue.size-1 downTo 0) {
            subCommandQueue[i].undo(modelController)
        }
        return true
    }
    fun addSC(vararg scs:SubCommand) {
        scs.forEach{subCommandQueue.add(it)}
    }
}