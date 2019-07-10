package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Main.ID;
import Main.Main;

public class Gun extends Object {

	Main game;
	public int ammo;
	public int ammoLeft;
	protected Player player;
	public ID parent;
	public boolean equipped = false;
	public String type;
	public BufferedImage spriteRight, spriteLeft;
	public boolean lineOfSight = false;
	public int range;
	public int bulletSpeed;
	public int damage;
	public int pushback;
	public int shake;
	public int bulletType;
	public int fireDelay;
	public double recoil;
	public float reloadSpeed;
	public double temp;
	public boolean reload = false;
	public float fraction;
	protected Random ran;
	protected int bulletWidth, bulletHeight;
	boolean landed;
	boolean collide;

	public Gun(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.type = type;
		// player = game.handler.player;
		ran = new Random();
		vely = 0.5f;
		ammoLeft = ammo;
		landed = false;
	}

	public void tick() {
		teleport();
		if (player == null) {
			player = game.handler.player;
			temp = player.width;
			fraction = player.width / reloadSpeed;
		}
		if (player != null) {
			if (equipped) {
				if (type == "sniper") {
					x = player.x + player.facing * (player.width / 2);
					y = player.y + (player.height / 3);
				} else {
					x = player.x + player.facing * (player.width / 2);
					y = player.y + (player.height / 2);
				}
			}

			shoot();
		}
		if (!equipped) {
			y += vely;
			for (int i = 0; i < game.handler.getGameObjects().size(); i++) {
				if (game.handler.getGameObjects().get(i) == null) {
					continue;
				}
				if (game.handler.getGameObjects().get(i).id == ID.Platform) {
					if (getBounds().intersects(game.handler.getGameObjects().get(i).getBounds())) {
						y = game.handler.getGameObjects().get(i).y - height-y_range/2;
						vely = 0;
						landed = true;
						collide = true;
						
					}
				}
			}
			if (landed) {
				//hover(1, 32);
				rotate(rotate_range, rotate_speed);
			}
			collide = false;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	double theta = 0.0;
	double rotate_range = 0.2;
	double rotate_speed = 0.005;
	boolean decrease = false;

	public void rotate(double rotate_range, double rotate_speed) {
		if (theta < rotate_range && !decrease)
			theta += rotate_speed;
		else {
			theta -= rotate_speed;
			decrease = true;
			if (theta <= -rotate_range) {
				decrease = false;
			}
		}
	}

	double y_dis = 0.0;
	double up_speed = 2;
	double y_range = 15;
	boolean up = false;

	public void hover(double up_speed, double y_range) {
		if (y_dis < y_range/2 && !up)
			y_dis += up_speed;
		else {
			y_dis -= up_speed;
			up = true;
			if (y_dis <= -y_range) {
				up = false;
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Dialog", Font.BOLD, 18));
		if (equipped) {
			if (parent == ID.Player1) {
				reload(g2d);
				if (player.facing > 0) {
					g2d.drawImage(spriteRight, (int) x, (int) y, width, height, null);
					if (lineOfSight) {
						g2d.setColor(Color.red);
						g.drawLine((int) x + width - 5, (int) y + 10, (int) x + range, (int) y + 10);
					}
				} else {
					g2d.drawImage(spriteLeft, (int) x, (int) y, width, height, null);
					if (lineOfSight) {
						g2d.setColor(Color.red);
						g.drawLine((int) x, (int) y + 10, (int) x - range, (int) y + 10);
					}
				}
			}
		} else {
			g2d.translate(0, y_dis);
			g2d.rotate(theta, x + width / 2, y + height / 2);
			g2d.drawImage(spriteLeft, (int) x, (int) y, width, height, null);
			g2d.rotate(-theta, x + width / 2, y + height / 2);
			g2d.translate(0, -y_dis);

		}
	}

	// shoot method
	int startTime = -1;
	int recoilBuilt = 0;
	boolean pushPlayer = false;
	double pushAmount = 0;

	public void shoot() {
		if (ammoLeft < 1) {
			reload = true;
		}
		if (game.handler.isShoot1() && equipped && !reload) {
			if (startTime == -1) {
				createBullet(player.facing, parent);
				// shake screen
				game.handler.cam.screenShake(shake);
			}
			startTime++;
			if (startTime >= fireDelay) {
				game.handler.shake1 = true;
				game.handler.shake = shake;
				if (recoilBuilt > recoil) {
					recoilBuilt = (int) recoil;
				}
				y -= recoilBuilt; // recoil
				if (parent != null && game.handler.player != null) {
					createBullet(game.handler.player.facing, parent);
					game.handler.cam.screenShake(shake);
				}
				pushPlayer = true;
				recoilBuilt++;
				startTime = 0;
			}
		} else {
			recoilBuilt = 0;
		}
		pushBack(player, 0.5);

	}

	// push back object
	public void pushBack(Object obj, double increment) {
		// pushback player when shoots
		if (pushAmount >= pushback) {
			pushPlayer = false;
			pushAmount = 0;
		}
		if (pushPlayer) {
			pushAmount += increment;
		}
		player.x += pushAmount * -player.facing;
	}

	double upDown = 1.0;

	public void createBullet(double facing, ID parent) {
		game.handler.cam.screenShake(shake);
		if (ran.nextDouble() >= 0.5) {
			upDown = 1.0;
		} else {
			upDown = -1.0;
		}

		recoil = ran.nextDouble();
		if (facing > 0) {
			game.handler.addObject(
					new Bullet(game, (int) x + width - 10, (int) y + (height / 3), bulletWidth, bulletHeight, facing,
							bulletSpeed, type, damage, range, pushback, recoil * upDown, ID.Bullet1, game.handler));
		} else {
			game.handler.addObject(new Bullet(game, (int) x + 10, (int) y + (height / 3), bulletWidth, bulletHeight,
					facing, bulletSpeed, type, damage, range, pushback, recoil * upDown, ID.Bullet1, game.handler));
		}

		ammoLeft--;
	}

	public void reload(Graphics2D g2d) {
		if (reload) {
			g2d.setColor(Color.white);
			g2d.drawString("Reloading", (int) player.x, (int) player.y - 30);
			g2d.draw(new Rectangle((int) player.x, (int) player.y - 20, player.width, 10));
			g2d.fill(new Rectangle((int) player.x, (int) player.y - 20, (int) (temp), 10));
			if (temp <= 0) {
				temp = player.width;
				ammoLeft = ammo;
				reload = false;

			}
			temp -= fraction;
		} else {
			g2d.setColor(Color.WHITE);
			g2d.drawString("" + ammoLeft, (int) player.x + player.width / 2, (int) player.y - 10);
		}
	}

}
