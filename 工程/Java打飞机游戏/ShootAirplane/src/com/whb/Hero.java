package com.whb;

public class Hero extends FlyingObject {
	
	public Hero() {
		this.image = Main.hero0;
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
		this.x = (Main.WIDTH - this.width) / 2;
		this.y = Main.HEIGHT - 200;
	}

	@Override
	public boolean outOfBounds() {
		return this.x >= 0 && this.x <= Main.WIDTH - this.width && this.y >= 0 && this.y <= Main.HEIGHT - this.height; 
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public Bullet shoot() {
		int x = this.x + (this.width / 2);
		int y = this.y - 20;
		Bullet bullet = new Bullet(x, y);
		return bullet;
	}
	
	public void moveTo(int x, int y) {
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
}
