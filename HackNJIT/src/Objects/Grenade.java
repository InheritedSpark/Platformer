package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import Main.Handler;
import Main.ID;
import Main.Main;

public class Grenade extends Bullet {

	float speed_x, speed_y;
	private int radius;
	Rectangle2D intersection;
	private long start_time, current_time;
	float time_left, explosion_time, timer;
	double xoff, yoff, speed_xoff, speed_yoff;
	boolean collide;
	double xoff2, yoff2;
	boolean start = false;

	public Grenade(Main game, double x, double y, int width, int height, double facing, double speed, String type,
			int damage, int range, int pushback, double recoil, ID id, Handler handler, float timer) {
		super(game, x, y, width, height, facing, speed, type, damage, range, pushback, recoil, id, handler);
		this.accy = -2f;
		this.speed_x = 15;
		this.speed_y = 30;
		this.radius = range;
		this.width = width;
		this.height = height;
		this.y = y;
		this.timer = timer;
		this.damage = damage;
		this.start_time = System.currentTimeMillis();
		collide = false;
		temp_speed_x = speed_x;
		temp_speed_y = speed_y;
	}

	@Override
	public void tick() {
		time_left = startTimer(timer);
		if (time_left <= 0 && !explode) {
			explode = true;
			start = true;
			speed_y = 0;
			speed_x = 0;
			destroy();
		}

		ray = new Line2D.Double(x, y, x + velx, y);

		if (!collide) {
			trajectory();
		}
		
		if(!explode){
			destroy();
		}

		if (speed_y < -40)
			speed_y = -40;

		if (explode) {
			if (start) {
				start_time = System.currentTimeMillis();
				start = false;
			}

			if (explosion_time >= 0) {
				// System.out.println(explosion_time);
				explosion_time = startTimer(0.5f);
				handler.cam.screenShake(10);
				explosion.tick();
			} else {
				// System.out.println(
				// "remove: " + explosion_time + " start_time: " + start_time +
				// " current_time: " + current_time);
				handler.removeObject(this);
			}

		}

		trailVis -= 0.01;
		if (trailVis <= 0) {
			trailVis = 0;
		}
	}

	public void trajectory() {
		if (!(speed_x == 0 && speed_y == 0)) {
			xoff2 = xoff;
			yoff2 = yoff;
			xoff = x;
			yoff = y;
			x += speed_x * facing;
			y -= speed_y;
			speed_y += accy;
		}
	}

	float temp_speed_x;
	float temp_speed_y;

	public void destroy() {
		for (int i = 0; i < handler.getGameObjects().size(); i++) {
			Object temp = handler.getGameObjects().get(i);
			collide = false;
			if (temp == null) {
				continue;
			}
			
			// destroy objects when exploding
			if (explode && temp.getBounds().intersects(getBoundsExplosion())) {
				if(temp.id == ID.Enemy || temp.id == ID.Turret){
					temp.health -= damage;
//					handler.removeObject(temp);
					String msg = "-" + (int) damage;
					handler.updates.add((new Update(game, temp.x, temp.y, new Color(255, 254, 45), msg, 150, temp.width * 2)));
				}
				else if(temp.id == ID.Platform){ // destroy fragil block
					Platform p = (Platform)temp;
					if (p.isfragile) {
						temp.health -= damage;
//						handler.removeObject(temp);
						String msg = "-" + (int) damage;
						handler.updates.add((new Update(game, temp.x, temp.y, new Color(255, 254, 45), msg, 150, temp.width * 2)));
					}
				}
			}
			
			if (temp.getBounds().intersects(getBounds())) {

				if (Math.abs(speed_y) > 0 && Math.abs(speed_y) < 2)
					speed_y = 0;
				if (Math.abs(speed_x) > 0 && Math.abs(speed_x) < 0.25)
					speed_x = 0;

				if (temp.id == ID.Portal) {
					teleport((Portal) temp);
				}
				// Bounce when hit platform
				if (temp.id == ID.Platform || temp.id == ID.Turret) {
					// x = xoff2;
					// y = yoff2;
					// xoff = xoff2;
					// yoff = yoff2;
					speed_x = speed_x * .70f;
					speed_y = speed_y * .55f;
					// x = xoff;
					// y = yoff;

					// speed_x *= .7;
					// speed_y *= .55;
					collide = true;
					//
					intersection = temp.getBounds().createIntersection(getBounds());

					if (getBoundsTop().intersects(temp.getBounds())) {
						// Hit was from below the brick
						y = temp.y + temp.height;
						speed_y = -speed_y;
					}

					else if (getBoundsBottom().intersects(temp.getBounds())) {
						intersection = getBoundsBottom().createIntersection(temp.getBounds());
						// Hit was from above the brick
						y = temp.y - height;
						speed_y = -speed_y;
					}

					else if (getBoundsRight().intersects(temp.getBounds())) {
						// Hit was on left
						intersection = getBoundsRight().createIntersection(temp.getBounds());
						x = temp.x - width;
						speed_x = -speed_x;
					} else if (getBoundsLeft().intersects(temp.getBounds())) {
						// Hit was on right
						intersection = getBoundsLeft().createIntersection(temp.getBounds());
						x = temp.x + temp.width;
						speed_x = -speed_x;
						// System.out.println("right"+ speed_x);
					}
					// if(speed_x == 0){
					// explode = true;
					// }
					break;
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// draw timer
		g2d.setStroke(new BasicStroke(8));
		g2d.setColor(Color.RED);
		g2d.drawString(String.valueOf(time_left), (int) x, (int) y - 20);

		g2d.setColor(new Color(255, 155, 10));
		if (!explode) {
			// g2d.fill(getBounds());
			// g2d.fillOval((int) x, (int) y, width, height);
			// g2d.draw(getBounds());
			g2d.drawOval((int) x, (int) y, width, height);
			// draw bounds
			// g2d.setStroke(new BasicStroke(1));
			// g2d.setColor(Color.black);
			// g2d.draw(getBoundsBottom());
			// g2d.draw(getBoundsTop());
			// g2d.draw(getBoundsRight());
			// g2d.draw(getBoundsLeft());
			// g2d.draw(getBoundsExplosion());
			//
			// g2d.setColor(Color.BLUE);
			// g2d.draw(getBounds2());
			// g2d.setColor(Color.BLACK);
			// g2d.draw(getBounds3());

			// draw intersection of collision
			if (intersection != null) {
				g2d.setColor(Color.red);
				g2d.draw(intersection);
			}
			if (collide) {
			}

		} else {
			if (facing > 0) {
				g2d.drawImage(explosion.getCurrentFrame(), (int) getBoundsExplosion().x, (int) getBoundsExplosion().y,
						getBoundsExplosion().width, getBoundsExplosion().height, null);
			} else {
				g2d.drawImage(explosion.getCurrentFrame(), (int) getBoundsExplosion().x, (int) getBoundsExplosion().y,
						getBoundsExplosion().width, getBoundsExplosion().height, null);
			}
		}
		// draw trail only if it's visible
		if (trailVis > 0) {
			g2d.setStroke(new BasicStroke(width));
			g2d.setColor(new Color(255, 255, 255, (int) trailVis));
			// g2d.draw(trail());
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public Rectangle getBounds2() {
		return new Rectangle((int) xoff, (int) yoff, width, height);
	}

	public Rectangle getBounds3() {
		return new Rectangle((int) xoff2, (int) yoff2, width, height);
	}

	public Rectangle getBoundsBottom() {
		return new Rectangle((int) (x + width / 4), (int) y + height - height / 4, (int) (width - width / 2),
				height / 4);
	}

	public Rectangle getBoundsTop() {
		return new Rectangle((int) (x + width / 4 + 2.5), (int) y, (int) (width - width / 2 - 5), height / 3);
	}

	public Rectangle getBoundsRight() {
		return new Rectangle((int) x + width - width / 4, (int) y + height / 5, width / 4, height - height / 2);
	}

	public Rectangle getBoundsLeft() {
		return new Rectangle((int) x, (int) y + height / 5, width / 4, height - height / 2);
	}

	public Rectangle getBoundsExplosion() {
		return new Rectangle((int) x - radius * 2 + width / 2, (int) y - radius * 2 + height / 2, (int) (radius * 4),
				radius * 4);
	}

	private float startTimer(float seconds) {
		current_time = System.currentTimeMillis();
		float r = (seconds * 1000 - (current_time - start_time)) / 1000.00f;
		DecimalFormat df = new DecimalFormat("#.#");
		return Float.valueOf(df.format(r));
	}

	private void teleport(Portal port) {
		x = port.x2 + port.getWidth() / 2;
		y = port.y2 + port.getHeight() / 2;
	}
}
