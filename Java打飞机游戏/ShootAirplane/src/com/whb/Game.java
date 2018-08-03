package com.whb;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Game {
	public static final int WIDTH = 400; // Ãæ°å¿í
	public static final int HEIGHT = 654; // Ãæ°å¸ß

	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;

	static {
		try {
			background = ImageIO.read(Game.class.getResource("background.png"));
			start = ImageIO.read(Game.class.getResource("start.png"));
			airplane = ImageIO.read(Game.class.getResource("airplane.png"));
			bee = ImageIO.read(Game.class.getResource("bee.png"));
			bullet = ImageIO.read(Game.class.getResource("bullet.png"));
			hero0 = ImageIO.read(Game.class.getResource("hero0.png"));
			hero1 = ImageIO.read(Game.class.getResource("hero1.png"));
			pause = ImageIO.read(Game.class.getResource("pause.png"));
			gameover = ImageIO.read(Game.class.getResource("gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
}
