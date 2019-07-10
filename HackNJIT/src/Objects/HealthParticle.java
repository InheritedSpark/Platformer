package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import Main.ID;
import Main.Main;

public class HealthParticle extends Object {

	float speedx, speedy, accx, accy;
	private Object target;
	private Color color;
	private Main game;
	Random ran;

	public HealthParticle(Main game, Object target, double x, double y, int width, int height, Color color, ID id) {
		super(game, x, y, width, height, id);
		this.x = x;
		this.y = y;
		this.target = target;
		this.color = color;
		this.game = game;
	}

	@Override
	public void tick() {
		x += (target.getX() - x) * 0.05;
		y += (target.getY() - y) * 0.05;

		if (target.getBounds().intersects(getBounds())) {
			game.handler.removeObject(this);

			game.handler.updates.add(new Update(game, target.x, target.y, color, "+20", 150, target.width));
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

//		g2d.setColor(color);
//		g2d.fillOval((int) x, (int) y, width, height);
		g2d.drawImage(game.asset.items[2], (int)x, (int)y, 64, 64, null);

	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

}
