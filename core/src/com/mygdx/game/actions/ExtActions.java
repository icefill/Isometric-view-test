package com.mygdx.game.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class ExtActions extends Actions{
	static public JumpTo jumpTo(float x, float y, float z, float duration) {
		JumpTo action = action(JumpTo.class);
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		action.setG(500);
		 		return action; 
 
		 	} 
	
	static public JumpToWDir jumpToWDir(float x, float y, float z, float duration) {
		JumpToWDir action = action(JumpToWDir.class);
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		action.setG(500);
		 		return action; 
 
		 	} 

	static public MoveTo moveTo(float x, float y, float z, float duration) {
		MoveTo action = action(MoveTo.class);
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		return action; 
 
		 	} 
	static public RunnableAction execute(Runnable r) {
		RunnableAction action=action(RunnableAction.class);
		action.setRunnable(r);
		return action;
	}
		 

}

