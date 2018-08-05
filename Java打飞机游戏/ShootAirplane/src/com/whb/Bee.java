package com.whb;

public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;
	private int ySpeed = 2;
	private int awardType;
	
	public Bee() {
		this.x = (int)(Math.random() * (Main.WIDTH - this.width + 1));
		this.y = -this.height;
		this.image = Main.bee;
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
		this.awardType = (int)(Math.random() * 2);
	}

	@Override
	public int getType() {
		return this.awardType;
	}

	@Override
	public boolean outOfBounds() {
		return this.y > Main.HEIGHT + this.height;
	}

	@Override
	public void move() {
		if(this.x + xSpeed > Main.WIDTH - this.width || this.x + xSpeed < 0) {
			xSpeed = -xSpeed;
		}
		this.x += xSpeed;
		this.y += ySpeed;
	}
	
}
