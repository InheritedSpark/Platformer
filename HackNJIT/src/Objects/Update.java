package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Main.Handler;
import Main.ID;
import Main.Main;

public class Update {

	Color color;
	Main game;
	String message;
	double x, y, origin_x, origin_y, alpha;
	double dis;
	public boolean remove;
	ID id;
	int width, height, r, g, b;
	int speed;
	int size;
	
	Font font;

	public Update(Main game, double x, double y, Color color, String message, float dis, int size) {
		this.x = x;
		this.y = y;
		this.origin_x = x;
		this.origin_y = y;
		this.color = color;
		this.message = message;
		this.dis = dis;
		this.remove = false;
		this.size = size;
		alpha = color.getAlpha();
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		speed = 2;
	}

	public void tick() {
		move_up();
		alpha -= 255*speed/dis;
		alpha = (alpha < 0) ? 0 : alpha;
		size--;
	}

	public void render(Graphics2D g2d) {
		color = new Color((int) r, (int) g, (int) b, (int) alpha);
		g2d.setFont(Handler.font);
		g2d.setStroke(new BasicStroke(3.0f));
		g2d.setColor(color);
		g2d.drawString(message, (float) x, (float) y);
	}

	public String message(String message) {
		return message;
	}

	public void move_up() {
		double travelled = Math.abs(origin_y - y);
		if (travelled > dis) {
			remove = true;
		}
		y -= speed;
	}
}