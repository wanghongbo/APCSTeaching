package com.whb;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	public static final int WIDTH = 400; // ����
	public static final int HEIGHT = 654; // ����

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
			background = ImageIO.read(Main.class.getResource("background.png"));
			start = ImageIO.read(Main.class.getResource("start.png"));
			airplane = ImageIO.read(Main.class.getResource("airplane.png"));
			bee = ImageIO.read(Main.class.getResource("bee.png"));
			bullet = ImageIO.read(Main.class.getResource("bullet.png"));
			hero0 = ImageIO.read(Main.class.getResource("hero0.png"));
			hero1 = ImageIO.read(Main.class.getResource("hero1.png"));
			pause = ImageIO.read(Main.class.getResource("pause.png"));
			gameover = ImageIO.read(Main.class.getResource("gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		GamePanel game = new GamePanel(); // ������
		frame.add(game); // �������ӵ�JFrame��
		frame.setSize(WIDTH, HEIGHT); // ���ô�С
		frame.setAlwaysOnTop(true); // ��������������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ĭ�Ϲرղ���
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); // ���ô����ͼ��
		frame.setLocationRelativeTo(null); // ���ô����ʼλ��
		frame.setVisible(true); // �������paint
		
		game.run();
	}

}
