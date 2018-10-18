package com.whb;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage image;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public abstract boolean outOfBounds();
	
	public abstract void move();

	public boolean hit(FlyingObject other){
		int centerX1 = this.x + this.width/2;
		int centerX2 = other.x + other.width/2;
		int centerY1 = this.y + this.height/2;
		int centerY2 = other.y + other.height/2;
		
		return Math.abs(centerX1 - centerX2) < (this.width / 2 + other.width / 2) && Math.abs(centerY1 - centerY2) < (this.height / 2 + other.height / 2);
	}
}

