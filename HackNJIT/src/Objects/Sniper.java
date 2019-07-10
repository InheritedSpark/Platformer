package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Random;

import Main.ID;
import Main.Main;

public class Sniper extends Gun {

	LinkedList<Object> game_objs;
	double los_x;
	int los_y;

	public Sniper(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id, type);

		this.game_objs = game.handler.getGameObjects();
		bulletType = 0;
		spriteRight = game.asset.sniperRight;
		spriteLeft = game.asset.sniperLeft;
		lineOfSight = true;
		range = 10000;
		bulletSpeed = 100;
		damage = 75;
		pushback = 5;
		shake = 15;
		fireDelay = 5;
		recoil = 10;
		ammo = 1;
		ammoLeft = ammo;
		reloadSpeed = 50f;
		bulletWidth = 20;
		bulletHeight = 8;
		los_x = 150;
		los_y = y + 25;

	}

	public Rectangle line_of_sight() {
		return new Rectangle((int) x + width - 30, (int) los_y, (int) los_x, (int) 3);
	}
	public Rectangle line_of_sightL() {
		return new Rectangle((int) (x + width - 30-los_x), (int) los_y, (int) x + width - 30, (int) 3);
	}

	int dot_height = 15;
	int dot_x = 0;
	int dot_y = 0;

	boolean hit = false;
	Object temp2;
	int default_x = 500;
	int dist_x = 10000000;
	int dist_xtmp = -1;
	Random rand = new Random();
	int ran_x;
	Rectangle tempLOS;
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Dialog", Font.BOLD, 18));
		// g2d.draw(getBounds());

		if (equipped) {
			dist_x = 10000000;
			los_x = current_length;
			los_y = (int) y + 18;
			hit = false;
			for (int i = 0; i < game_objs.size(); i++) {
				Object temp = game_objs.get(i);
				
				if (temp.id != ID.Player1 && temp.id != ID.Gun) {
					if (temp.getBounds().intersects(line_of_sight())) {
						dist_xtmp = (int) Math.abs(temp.getX() - (x + width-30));
						if (dist_xtmp < dist_x) {
							dist_x = dist_xtmp;
						}
						hit = true;
					}
				}

			}
			if (hit) {
				los_x = dist_x;
				dot_height = 15;
				dot_x = (int) (x + width - 35 + los_x);
				dot_y = (int) y + 18 - dot_height / 2;
			}
			if (!hit) {
				oscillate();
				dot_height = 0;
				dot_x = 0;
				dot_y = 0;
			}
//			System.out.println(hit+" dist_x:"+dist_x+" los_x:"+los_x);
			// render line of sight
			if ((parent == ID.Player1 || parent == ID.Player2)) {
				reload(g2d);
				if (player.facing > 0) {
					g2d.drawImage(spriteRight, (int) x, (int) y, width, height, null);
					if (lineOfSight) {
						g2d.setColor(Color.red);
						// g.drawLine((int)x+width-5, (int)y+25, (int)x+250,
						// (int)y+25);
						g2d.fill(line_of_sight());
						g2d.fillOval((int) dot_x, (int) dot_y, dot_height, dot_height);
					}
				} else {
					g2d.drawImage(spriteLeft, (int) x, (int) y, width, height, null);
					if (lineOfSight) {
						g2d.setColor(Color.red);
//						g.drawLine((int)x, (int)y+25, (int)(x-los_x), (int)y+25);
//						g2d.fill(line_of_sightL());
						g2d.fillOval((int) dot_x, (int) dot_y, dot_height, dot_height);
					}
				}
			}

		} else {
			g2d.rotate(theta, x + width / 2, y + height / 2);
			g2d.drawImage(spriteLeft, (int) x, (int) y, width, height, null);
			g2d.rotate(-theta, x + width / 2, y + height / 2);
			rotate(rotate_range, rotate_speed);
		}

	}

	boolean move = false;
	boolean dec = false;
	double speed_x = 5;
	double current_length = 500;

	public void oscillate() {
		if (!move) {
			ran_x = ran.nextInt(150);
			move = true;
		} else {
			if (current_length < default_x + ran_x && !dec) {
				current_length += speed_x;
			} else {
				dec = true;
				current_length -= speed_x;
				if (current_length < default_x && dec) {
					dec = false;
				}
			}
		}
		los_x = current_length;
	}
}
