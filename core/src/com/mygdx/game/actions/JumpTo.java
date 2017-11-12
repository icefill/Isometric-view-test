package com.mygdx.game.actions;


import com.mygdx.game.view.ViewActor;

public class JumpTo extends MoveTo {

	protected float v0;
	private float g;

	protected void begin(){
		super.begin();
		v0=(float)((endZ-startZ)/this.getDuration()+g*0.5*this.getDuration());

	}
	public void setG(float g) {
		this.g=g;
	}
	protected void update (float percent) {
		super.update(percent);
		float time=this.getDuration()*percent;
		((ViewActor)target).setZ((float)(startZ+v0*time-0.5*g*time*time));
	}
	
	
}
