package com.whb;

public class Airplane extends FlyingObject implements Enemy {
	private int speed = 3;
	
	public Airplane() {
		this.x = (int)(Math.random() * (Main.WIDTH - this.width + 1));
		this.y = -this.height;
		this.image = Main.airplane;
		width = this.image.getWidth();
		height = this.image.getHeight();
	}

	@Override
	public int getScore() {
		return 5;
	}

	@Override
	public boolean outOfBounds() {
		return this.y > Main.HEIGHT + this.height;
	}

	@Override
	public void move() {
		this.y += speed;
	}
	
}
