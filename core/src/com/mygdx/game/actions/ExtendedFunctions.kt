package com.mygdx.game.actions

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction

infix fun Action.NEXT (other:Action) :Action {
    return (this as? SequenceAction)?.apply{addAction(other)}
            ?: Actions.sequence(this,other)
}
infix fun Action.WITH (other:Action) :Action {
    return (this as? ParallelAction)?.apply{addAction(other)}
            ?: Actions.parallel(this,other)
}

typealias A= ExtActions