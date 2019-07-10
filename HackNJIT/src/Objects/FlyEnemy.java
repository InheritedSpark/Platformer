package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Graphics.Animation;
import Main.ID;
import Main.Main;

public class FlyEnemy extends Enemy {

	private int detectionRadius;
	private Object target;
	private Main game;
	private Animation flying, hurt, currentAnimation;
	private double speed_x, speed_y;

	public FlyEnemy(Main game, Object target, int x, int y, int width, int height, int r, ID id) {
		super(game, x, y, width, height, id);
		this.game = game;
		detectionRadius = r;
		this.target = target;
		damage = 50;
		flying = new Animation(60, game.asset.flyingEnemy);
		hurt = new Animation(60, game.asset.flyingEnemyHit);
		currentAnimation = flying;
		detected = false;
	}
	
	public void tick(){
		if (health <= 0) { 
			// health effects to player 
			for (int i = 0; i < 1; i++) {
				game.handler.addObject(new HealthParticle(game, game.handler.player, x + ran.nextInt(200) + 1,
						y + ran.nextInt(200) + 1, 16, 16, new Color(14, 232, 221), ID.Effect));
			}
			game.handler.removeObject(this);
		}
		move();
	}

	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		// g2d.setColor(Color.black);
		// g2d.draw(getBounds());
		// g2d.draw(zone());
		if (speed_x >= 0) {
			facing = 1;
			g2d.drawImage(currentAnimation.getCurrentFrame(), (int) x, (int) y, (int) width, (int) height, null);
		} else if (speed_x <= 0) {
			facing = -1;
			g2d.drawImage(currentAnimation.getCurrentFrame(), (int) x + width, (int) y, (int) -width, (int) height, null);
		}
	}

	int counter;
	public void move() {
		if (flash) {
			currentAnimation = hurt;
			if(currentAnimation.getCurrentFrame() == game.asset.flyingEnemyHit[game.asset.flyingEnemyHit.length-1]){
				counter ++;
			}
			if(counter >= 50){
				flash = false;
				counter = 0;
			}
		} else {
			currentAnimation = flying;
		}
		currentAnimation.tick();
		
		if (target.getBounds().intersects(zone())) { // set detected to true when player in zone
			detected = true;
		}
		if(detected){ // move towards player when detected
			x += speed_x;
			y += speed_y;
			speed_x = (target.getX() - x) * 0.02;
			speed_y = (target.getY() - y) * 0.02;

			if (target.getBounds().intersects(getBounds())) { // deal damage
																// when collided
																// with player
				String message = "-" + (int) damage;
				game.handler.updates.add(new Update(game, target.getX(), target.getY(), new Color(255, 25, 24, 255),
						message, 150, target.getWidth())); // create a -damage
															// feedback
				game.handler.removeObject(this); // remove this
			}
		}

	}

	public Rectangle zone() {
		return new Rectangle((int) x - detectionRadius + width / 2, (int) y - detectionRadius + height / 2,
				detectionRadius * 2, detectionRadius * 2 - height / 2);
	}

	public void collision() {
	}
}
