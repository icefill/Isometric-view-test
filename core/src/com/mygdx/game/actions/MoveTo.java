package com.mygdx.game.actions;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.game.view.ObjActor;
import com.mygdx.game.view.ViewActor;

public class MoveTo extends MoveToAction {
	protected float startZ;
	protected float endZ;
	protected float startX;
	protected float startY;
	protected float endX;
	protected float endY;

	protected void begin(){
		startX=((com.mygdx.game.view.ViewActor)target).getX();
		startY=((com.mygdx.game.view.ViewActor)target).getY();
		startZ=((com.mygdx.game.view.ViewActor)target).getZ();
		super.begin();
	}
	protected void setPosition(float x,float y, float z) {
		super.setPosition(x, y);
		this.endX=x;
		this.endY=y;
		this.endZ=z;
	}

	protected void update (float percent) {
		super.update(percent);
		((ViewActor)target).setZ(startZ + (endZ - startZ) * percent);
	}
	
	
}
