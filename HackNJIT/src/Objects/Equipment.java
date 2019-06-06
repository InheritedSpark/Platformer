package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Graphics.Animation;
import Main.Handler;
import Main.ID;
import Main.Main;

public class Equipment extends Object {

	private Handler handler;
	int facing;
	Animation explosion;
	boolean explode = false;
	Main game;
	public int speed, damage, pushback;
	private double originY;
	public String type;
	
	public Equipment(Main game, int x, int y, int width, int height, int facing,int speed, String type, int damage, int pushback, ID id, Handler handler) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.handler = handler;
		this.facing = facing;
		this.speed = speed;
		this.type = type;
		this.damage = damage;
		this.pushback = pushback;
		velx = speed*facing;
		accy = -5;
		explosion = new Animation(10,game.asset.explosionEffect);
		originY = y;
	}

	public void tick() {
		x +=velx;
		y += accy;
		if(Math.abs(originY-y) > 10){
			accy *= -1;
		}
		destroy();
		if(explode){
			explosion.tick();
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.yellow);
		if(!explode){
			g2d.fill(getBounds());
		}else{
			if(facing>0){
				g2d.drawImage(explosion.getCurrentFrame(), (int)x-16, (int)y-32, 32, 32, null);
			}
			else{
				g2d.drawImage(explosion.getCurrentFrame(), (int)x, (int)y-32, 32, 32, null);
			}
			
			if(explosion.getCurrentFrame() == game.asset.explosionEffect[7]){
				handler.removeObject(this);
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	public void destroy(){
		for(int i =0; i<handler.getGameObjects().size();i++){
			Object temp = handler.getGameObjects().get(i);
			///destroy when hit platform
			if(temp.id == ID.Platform){
				if(getBounds().intersects(temp.getBounds())){
					velx = 0;
					explode = true;
				}
			}
		}
	}

}
