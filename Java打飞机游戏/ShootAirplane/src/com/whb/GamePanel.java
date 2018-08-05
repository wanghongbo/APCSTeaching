package com.whb;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private Hero hero = new Hero();
	private ArrayList<Airplane> airplanes = new ArrayList<>();
	private ArrayList<Bullet> bullets = new ArrayList<>();

	@Override
	public void paint(Graphics g) {
		g.drawImage(Main.background, 0, 0, null);
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
		for (Airplane ariplane : airplanes) {
			g.drawImage(ariplane.getImage(), ariplane.getX(), ariplane.getY(), null);
		}
		for (Bullet bullet : bullets) {
			g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), null);
		}
	}
	
	public void run() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				System.out.println("x: " + x + " y:" + y);
				hero.moveTo(x, y);
				super.mouseMoved(e);
			}
		};
		this.addMouseMotionListener(mouseAdapter);
	}
}
