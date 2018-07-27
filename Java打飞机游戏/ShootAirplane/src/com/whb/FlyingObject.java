package com.whb;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	int x;
	int y;
	int width;
	int height;
	BufferedImage image;
	
	abstract void step();
	
	abstract void outOfBounds();
}

