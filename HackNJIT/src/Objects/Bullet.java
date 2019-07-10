package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import Graphics.Animation;
import Main.Handler;
import Main.ID;
import Main.Main;

public class Bullet extends Object {

	protected Handler handler;
	double facing;
	Animation explosion;
	boolean explode = false;
	Main game;
	int explosionR;
	public double speed, damage, pushback, range;
	public double recoil;
	public String type;
	public double originX, originY;
	public Line2D ray;
	protected double trailVis;

	public Bullet(Main game, double x, double y, int width, int height, double facing, double speed, String type,
			int damage, int range, int pushback, double recoil, ID id, Handler handler) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.facing = facing;
		this.speed = speed;
		this.type = type;
		this.damage = damage;
		this.pushback = pushback;
		this.range = range;
		this.recoil = recoil;
		this.explosionR = width*2;
		
		trailVis = 20;
		originX = x;
		originY = y;
		velx = (float) (speed * facing);
		explosion = new Animation(10, game.asset.explosionEffect);
	}

	public void tick() {
		teleport();
		if (type.equals("turret")) {
			ray = new Line2D.Double(x, y, x + speed * Math.cos(facing) * game.delta,
					y + speed * Math.sin(facing) * game.delta);
			x += speed * Math.cos(facing);
			y += speed * Math.sin(facing);
		} else {
			ray = new Line2D.Double(x, y, x + velx, y);
			x += velx;
			y += Math.sin(recoil) * facing;
		}
		destroy();
		if (explode) {
			explosion.tick();
			if (explosion.getCurrentFrame() == game.asset.explosionEffect[7]) {
				handler.removeObject(this);
			}
		}

		trailVis -= 0.01;
		if (trailVis <= 0) {
			trailVis = 0;
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(255, 155, 10));
		if (!explode) {
			// g2d.rotate(recoil, x, y);
			g2d.fill(getBounds());
			// g2d.rotate(-recoil, x, y);
		} else {
			if (facing > 0) {
				g2d.drawImage(explosion.getCurrentFrame(), (int) x - explosionR/2, (int) y-explosionR/2, explosionR, explosionR,  null);
			} else {
				g2d.drawImage(explosion.getCurrentFrame(), (int) x-explosionR/2, (int) y-explosionR/2, explosionR, explosionR, null);
			}
		}
		// draw trail only if it's visible
		if (trailVis > 0) {
			g2d.setStroke(new BasicStroke(width));
			g2d.setColor(new Color(255, 255, 255, (int) trailVis));
			g2d.draw(trail());
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public Line2D trail() {
		return new Line2D.Double(originX, originY, x, y);
	}

	public void destroy() {
		// destroy beyond gun range
		int disTravelled = (int) Math.abs(originX - x);
		if (disTravelled >= range) {
			velx = 0;
			explode = true;
		}

		for (int i = 0; i < handler.getGameObjects().size(); i++) {
			Object temp = handler.getGameObjects().get(i);
			if (temp == null) {
				continue;
			}
			if (ray.intersects(temp.getBounds())) {
				/// destroy when hit platform
				if (temp.id == ID.Platform) {
					Platform p = (Platform) temp;
					if (p.isfragile) {
						temp.health -= damage;
					}
					speed = 0;
					velx = 0;
					explode = true;
					x = temp.getX();
				}
				if (temp.id == ID.Enemy) {
					Enemy e = (Enemy) temp;
					if (velx > 0) {
						e.setX((int) e.getX() + (int) pushback * 2);
					} else if (velx < 0) {
						e.setX((int) e.getX() - (int) pushback * 2);
					}
					e.flash = true;
					explode = true;
					e.detected = true;
					velx = 0;
					e.health -= damage;
					String message = "-"+(int)damage;
					handler.updates.add(new Update(game, temp.getX(), temp.getY(), new Color(255,25,24,255),message, 100, temp.getWidth()));
					handler.removeObject(this);
					handler.cam.screenShake((int)recoil);
				}
				/// destroy when collides with other bullets
				else if (temp.id == ID.Bullet1 || temp.id == ID.Bullet2) {
					if (velx != temp.velx) {
						speed = 0;
						explode = true;
						handler.removeObject(temp);
					}
				} else if (type.equals("turret") && temp.id == ID.Player1) {
					if (velx != temp.velx) {
						speed = 0;
						explode = true;
					}
				}
			}
		}
	}

}
