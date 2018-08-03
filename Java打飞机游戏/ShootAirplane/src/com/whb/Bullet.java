package com.whb;

public class Bullet extends FlyingObject {
	private int speed = 3;
	
	public Bullet(int x, int y) {
		this.x = x;
		this.y = y;
		this.image = Game.bullet;
	}

	@Override
	public boolean outOfBounds() {
		return this.y < -this.height;
	}

	@Override
	public void move() {
		this.y -= this.speed;
	}
	
}
