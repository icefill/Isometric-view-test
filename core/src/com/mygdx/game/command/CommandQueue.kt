package com.mygdx.game.command

import java.util.*

class CommandQueue {
    var _currentIndex=-1
    var _maxIndex=0
    val _queue = LinkedList<Command>()

    fun enQueue(command: Command) {
        _queue.add(++_currentIndex,command)
        _maxIndex=_currentIndex
    }
    fun getPreviousCommand():Command? {
        if (_currentIndex==-1) return null
        return _queue[_currentIndex--]
    }
    fun getNextCommand():Command?{
        if (_currentIndex+1 <=_maxIndex) {
            return _queue[++_currentIndex]
        }
        else return null
    }



}