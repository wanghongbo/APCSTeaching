package com.whb;

public class Airplane extends FlyingObject implements Enemy {
	private int speed = 3;
	
	public Airplane() {
		this.x = (int)(Math.random() * (Game.WIDTH - this.width + 1));
		this.y = -this.height;
		this.image = Game.airplane;
		width = this.image.getWidth();
		height = this.image.getHeight();
	}

	@Override
	public int getScore() {
		return 5;
	}

	@Override
	public boolean outOfBounds() {
		return this.y > Game.HEIGHT + this.height;
	}

	@Override
	public void move() {
		this.y += speed;
	}
	
}
