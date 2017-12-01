package com.mygdx.game.actions;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.view.ViewActor;


public class JumpToWDir extends JumpTo {

	private Vector2 direction_start;
	private Vector2 direction_end;
	private Vector2 direction;
	protected void begin(){
		super.begin();
		float z_modifier= this.getDuration()*v0;
		direction= new Vector2();
		direction_start= new Vector2(endX-startX,endY-startY+z_modifier);
		direction_end= new Vector2(endX-startX,endY-startY-z_modifier);
		target.setRotation(direction_start.angle());

	}
	protected void update (float percent) {
		super.update(percent);
		direction.set(direction_end);
		direction.sub(direction_start);
		direction.scl(percent);
		direction.add(direction_start);
		target.setRotation(direction.angle());

	}
	
	
}
