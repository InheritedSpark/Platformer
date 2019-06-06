package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Graphics.Animation;
import Main.ID;
import Main.Main;

public class Enemy extends Object{

	private Main game;
	private double vx, vy, currentvx;
	int health = 750;
	int damage = 1000;
	Animation walk, attack, death;
	private int state;
	boolean flash;
	public Enemy(Main game, int x, int y, int width, int height, ID id) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.vx = 5;
		this.vy = 7;
		flash = false;
		state = 0;
		walk = new Animation(100, game.asset.enemy1Sprites);
		attack = new Animation(100, game.asset.enemy1Attack);
		death = new Animation(100, game.asset.enemy1Death);
	}

	@Override
	public void tick() {
		teleport();
		switch(state){
		case 0:
			walk.tick();
			break;
		case 1:
			attack.tick();
			break;
		case 2:
			death.tick();
			break;
		}
		
		if(state != 2){
			walk.tick();
			attack.tick();
			death.tick();
			move();
			collision();
		}
		if(health <= 0){ // remove enemy when killed
			state = 2;
			vx = 0;
			if(death.getCurrentFrame() == game.asset.enemy1Death[game.asset.enemy1Death.length-1]){
				game.handler.getGameObjects().remove(this);	
			}
		}
		
	}

	int counter = 0;
	int c = 0;
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D)g;
		//g2d.setColor(new Color(255,12,20));
		//g2d.draw(getBounds());
		switch(state){
		case 0:
			if(!flash){
				if(currentvx >= 0){
					g2d.drawImage(walk.getCurrentFrame(), (int)x, (int)y,width, height, null);
				}else{
					g2d.drawImage(walk.getCurrentFrame(), (int)x+width, (int)y, -width, height, null);
				}
			}
			else{
				if(counter <= 2){
					if(currentvx >= 0){
						g2d.drawImage(game.asset.dye(walk.getCurrentFrame(), new Color(255*c,255*c,255*c)), (int)x, (int)y,width, height, null);
					}else{
						g2d.drawImage(game.asset.dye(walk.getCurrentFrame(), new Color(255*c,255*c,255*c)), (int)x+width, (int)y, -width, height, null);
					}
				}
			}
			break;
		case 1:
			if(currentvx >= 0){
				g2d.drawImage(attack.getCurrentFrame(), (int)x, (int)y,width, height, null);
			}else{
				g2d.drawImage(attack.getCurrentFrame(), (int)x+width, (int)y, -width, height, null);
			}
			break;
		case 2:
			if(currentvx >= 0){
				g2d.drawImage(death.getCurrentFrame(), (int)x, (int)y,width, height, null);
			}else{
				g2d.drawImage(death.getCurrentFrame(), (int)x+width, (int)y, -width, height, null);
			}
			break;
		}
			
		if(flash){
			counter ++;
			c = (c == 0)? 1:0;
			if(counter > 2){
				flash = false;
				counter = 0;
			}
		}
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y, width, height);
	}
	public Rectangle getBoundsBottom(){
		return new Rectangle((int)(x+width/4), (int)y+height-height/4, (int)(width-width/2), height/4);
	}
	public Rectangle getBoundsTop(){
		return new Rectangle((int)(x+width/4), (int)y, (int)(width-width/2), height/4);
	}
	public Rectangle getBoundsRight(){
		return new Rectangle((int)x+width-width/4, (int)y+height/4, width/4, height-height/2);
	}
	public Rectangle getBoundsLeft(){
		return new Rectangle((int)x, (int)y+height/4, width/4, height-height/2);
	}
	
	
	public void move(){
		currentvx = approach(vx, currentvx, game.delta*.8);
		x += currentvx;
		y+= vy;
	}
	public double approach(double velGoal, double velCurrent, double dt){
		double diff = velGoal - velCurrent;
		
		if(diff > dt){
			return velCurrent + dt;
		}
		if(diff < -dt){
			return velCurrent  - dt;
		}
		return velGoal;
	}

	public void collision(){
		for(int i = 0; i < game.handler.getGameObjects().size(); i++){
			Object temp = game.handler.getGameObjects().get(i);
			if(temp == null){
				continue;
			}
			if(getBounds().intersects(temp.getBounds())){
				if(!temp.shrink && !temp.expand){
					if(temp.id == ID.Platform){ // platform
						Platform p = (Platform)temp;
						if(getBoundsRight().intersects(temp.getBounds())){
							x = temp.x- width - currentvx;
							currentvx = -currentvx;
							vx = -vx;
						}
						else if(getBoundsLeft().intersects(temp.getBounds())){
							x = temp.getX() + temp.getWidth() - currentvx;
							currentvx = -currentvx;
							vx = -vx;
						}
						
						else if(getBoundsBottom().intersects(temp.getBounds())){
							y = temp.getY() - height;
						}
					}
					
					
					else if(temp.id == ID.Player1){ // player
						Player player = (Player)temp;
						if(currentvx > 0){
							player.currentVelx += 1;
						}
						else if(currentvx < 0){
							player.currentVelx -= 1;
						}
						state = 1;
						player.jump(5);
						player.health -= damage;
					}
				}
			}
		}
	}
}
