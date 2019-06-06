package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Graphics.Animation;
import Main.Handler;
import Main.ID;
import Main.Main;

public class Player extends Object  {

	Main game;
	Gun gun;
	private Handler handler;
	private double friction;
	private double accy;
	private double gravity,maxDy;
	private boolean falling,canJump,onGround;
	private boolean highlightPlatform = false;
	private boolean buildMode = false;
	private int maxJumps, jumps;
	public int health = 2; 
	
	private int platWidth, platHeight;
	
	private Animation animRight;
	private Animation animLeft;
	private Animation landingDust;
	
	int released = 0; 
	int pressed = 1;
	public boolean equipped = false;
	public boolean winner = false;
	public boolean loser = false;

	
	public Player(Main game, int x, int y, int width, int height, ID id, Handler handler) {
		super(game, x, y, width, height, id);
		this.handler = handler;
		this.game = game;
		//set velocity// 445
		gravity = 0.75;
		friction = 0.02;
		falling = true;
		maxDy = 10;
		jumps = 5;
		maxJumps = jumps;
		platWidth = platHeight = 32;
		
		//Animation
		animLeft = new Animation(90,game.asset.player1SpritesFlipped);
		animRight = new Animation(90, game.asset.player1Sprites);
		landingDust = new Animation(100,game.asset.dustEffect);
		facing = -1;
	}

	@Override
	public void tick() {
		//System.out.println("onGround: "+ onGround+ " accy: "+accy);
		teleport();
		collision();
		move();
		fall();
		build();
		if(health <= 0 ){
			handler.getGameObjects().remove(this);
			handler.player = null;
			handler.restart = true;
		}
		if(winner){
			handler.setUp(true);
		}
		else if(loser == true){
			handler.removeObject(this);
		}
		
		//Animation
		animLeft.tick();
		animRight.tick();
		
		// check for build mode
			buildMode = handler.getBuild();
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		// health
//		g2d.setColor(Color.green);
//		g2d.fill(new Rectangle((int)x,(int)y-20,width - width/health,10));
//		g2d.draw(new Rectangle((int)x,(int)y-20,width,10));
		g2d.drawImage(getCurrAnimFrame(), (int)x, (int)y, width, height, null);
//		g2d.draw(getBoundsBottom());
//		g2d.draw(getBoundsTop());
//		g2d.draw(getBoundsRight());
//		g2d.draw(getBoundsLeft());
//		g2d.draw(getBounds());
		
		
		//build mode
		if(buildMode){
			// build range
			g2d.setColor(Color.white);
			g2d.draw(getBoundsBuild());
			g2d.drawOval(getBoundsBuild().x, getBoundsBuild().y, getBoundsBuild().width, getBoundsBuild().height);
			
			// highlight platform inside build range
			if(highlightPlatform){
				g2d.setColor(Color.black);
				g2d.fill(new Rectangle(handler.getMouse()[0]/platWidth*platWidth,handler.getMouse()[1]/platHeight*platHeight,platWidth,platHeight));
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
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
	public Rectangle getBoundsBuild(){
		return new Rectangle((int)x-64, (int)y-64, (int)(width+128), height+128);
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
	double currentVelx;
	public void move(){
		//check final velocity
		if(velx >= XVELOCITY){
			velx = XVELOCITY;
		}
		else if (velx <= -XVELOCITY){
			velx = -XVELOCITY;
		}
		if(vely >= YVELOCITY){
			vely = YVELOCITY;
		}
		else if (vely <= -YVELOCITY){
			vely = -YVELOCITY;
		}
		
		//gravity
		///update position
		currentVelx = approach(velx, currentVelx, game.delta*.45);
		x += currentVelx;
		y+=accy*game.delta;
		
		///move right
		if(handler.isRight()){
			facing = 1;
			velx = 10;
		}
		///move left
		else if(handler.isLeft()){
			facing = -1;
			velx  = -10;
		}
		else{
			velx = 0;
		}
		
		///jump
		if(handler.isUp()){
			jump(15);
		}
		else{
			if(maxJumps > 1 && !canJump){
				canJump  = true;
				maxJumps--;
			}
		}
	}
	
	public void build(){
		if(buildMode){
			Boolean crossing = false;
			int platformX = handler.getMouse()[0]/platWidth*platWidth;
			int platformY = handler.getMouse()[1]/platHeight*platHeight;
			// highlight platform if within the build range
			if(getBoundsBuild().intersects(new Rectangle(platformX,platformY,platWidth,platHeight))){
				highlightPlatform = true;
			}
			else{
				highlightPlatform = false;
			}
			
			// check intersection before adding 
			if(handler.isMouse1()){
				LinkedList<Object> gameObjects = handler.getGameObjects();
				handler.setGameObjects(handler.getGameObjects());
				for(int i = 0; i < gameObjects.size(); i++){
					if(gameObjects.get(i).id == ID.Platform || gameObjects.get(i).id == ID.Player1){
						if(gameObjects.get(i).getBounds()
								.intersects(new Rectangle(platformX,platformY,platWidth,platHeight))){
							crossing = true;
						}
					}
				}
				if(!crossing){
					if(getBoundsBuild().intersects(new Rectangle(platformX,platformY,32,32))){
						gameObjects.add(new Platform(game,platformX,platformY,platWidth,platHeight,ID.Platform, true));
					}
				}
			}
		}
	}
	
	int weaponInv = 0;
	public void collision(){
		for(int i =0; i<handler.getGameObjects().size();i++){
			Object temp = handler.getGameObjects().get(i);
			onGround = false;
			
			if(temp != null && getBounds().intersects(temp.getBounds())){
				if(temp.id == ID.Platform ){
					///bottom collision
					if(getBoundsBottom().intersects(temp.getBounds()) &&  accy >= 0){
						falling = false;
						canJump  = true;
						onGround = true;
						maxJumps = jumps;
						accy = 0;
						y = temp.y - height+1;
					}
					///top collision 
					else if(getBoundsTop().intersects(temp.getBounds()) &&  accy < 0){
						y = temp.y + temp.height;
						accy = 0;
					}
					
					///right collision 
					if(getBoundsRight().intersects(temp.getBounds())){
						velx = 0;
						x = temp.x - width;
					}
					///left collision 
					else if(getBoundsLeft().intersects(temp.getBounds())){
						velx = 0;
						x = temp.x + temp.width;
					}
					
				}
				
				///fire damage
				if(temp.id == ID.Bullet2){
					Bullet bullet = (Bullet)temp;
					/// push back when hit///
					x+=bullet.pushback*bullet.facing;
					/// decrease health when hit
					health-=bullet.damage;
					/// remove bullet when hit
					handler.removeObject(temp);
				}
				if(temp.id == ID.MovePlat){
					if(accy >= 0){
						MovingPlatform mp = (MovingPlatform)temp;
						mp.start = true;
						falling = false;
						canJump  = true;
						onGround = true;
						maxJumps = jumps;
						accy = 0;
						y = mp.getBounds().y - height+5;
						y -= mp.speed * Math.sin(mp.theta);
						x -= mp.speed * Math.cos(mp.theta);
					}
				}
				
				if (temp.id == ID.Laser){
					health = 0;
				}
				
				///pick up gun
				if(temp.id == ID.Gun){
					gun = (Gun)temp;
					if(handler.isDown()){
						if(gun.parent == null){
							gun.equipped = true;
							gun.parent = this.id;
							weaponInv += 1;
						}
						else{
							if(weaponInv > 1){
								gun.parent = null;
								gun.equipped = false;
								handler.removeObject(gun);
								weaponInv -= 1;
							}
						}
					}
					
					///position the gun
					if(gun.equipped && gun.parent == this.id){
						if(gun.type=="sniper"){
							gun.x = x+facing * (width/2);
							gun.y = y+(height/3);
						}
						else{
							gun.x = x+facing * (width/2);
							gun.y = y+(height/2);
						}
					}
					
				}
			}
		}
		if(!onGround){
			falling = true;
		}
	}
	
	public void fall(){
		if(falling){
			accy+= gravity;
			if(accy >maxDy){
				accy = maxDy;
			}
		}
	}
	
	public void jump(double jumpHeight){
		if(canJump){
			accy = -jumpHeight;
		}
		canJump = false;
	}
	
	public BufferedImage getCurrAnimFrame(){
		if(velx > 0){
			return animRight.getCurrentFrame();
		}
		else if(velx < 0){
			return animLeft.getCurrentFrame();
		}
		if(facing >0) 
			return animRight.frames[1];
		else{
			return animLeft.frames[1];
		}
		
	}
	
}
