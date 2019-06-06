package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import Main.ID;
import Main.Main;

public class Turret extends Gun {

	private double x,y;
	private int width, height;
	private int radius, distanceX, distanceY;
	double centerX; 
	double centerY;
	private double theta, smoothTheta;
	double distance;
	private Main game;
	private LinkedList objects;
	private boolean detected, fire;
	private Object target;
	double thetaDeg;
	double currentThetaDeg;
	public Turret(Main game, int x, int y, int width, int height, ID id, String type, int radius) {
		super(game, x, y, width, height, id, type);
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.centerX = x+width/2;
		this.centerY = y+height/2;
		this.detected = false;
		this.smoothTheta = 0.0;
		fireDelay = 70;
		startTime = 0;
		reload = false;
		temp = width;
		ammo = 5;
		ammoLeft = ammo;
		reloadSpeed = 500f;
		fraction = width / reloadSpeed;
		damage = 850;
		bulletWidth = 10;
		bulletHeight = 10;
		bulletSpeed = 25;
		range = 10000;
		fire = false;
	}

	@Override
	public void tick() {
		if(objects == null){
			objects = game.handler.getGameObjects();
		}
		
		if(player != null){
			distanceX = (int)Math.abs((x+width/2 -(player.x+player.width/2)));
			distanceY = (int)Math.abs((y+height/2)-(player.y-player.health/2));
			distance = Math.sqrt(distanceX*distanceX + distanceY*distanceY);
		}
		if(!reload){
			trackPlayer();
			smoothTracking(1);	
		}
		reload();
		
		
	}

	Line2D ray;
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
//		g2d.drawString("Target: "+thetaDeg, (int)x, (int)y-100);
//		g2d.drawString("Angle diff: "+diff, (int)x, (int)y-125);
//		g2d.drawString("Current Angle: "+currentThetaDeg, (int)x, (int)y-150);
		
		if(reload){
			g2d.setColor(Color.black);
			g2d.draw(new Rectangle((int)x, (int)y-20, width, 10));
			g2d.fill(new Rectangle((int)x, (int)y-20, (int)temp, 10));
		}
		if(fire){
			g2d.rotate(smoothTheta, x+width/2, y+height/2);
			g2d.setColor(Color.black);
			//g2d.fill(getBounds());
		}
		if(detected && !reload && fire){
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.red);
			g2d.drawLine((int)x+width/2, (int)y+height/2, (int)(x+width/2+radius), (int)(y+height/2));
		}
		else{
			g2d.setColor(new Color(0,155,0));
		}
		g2d.setStroke(new BasicStroke(3));
		//g2d.draw(range());
		//g2d.translate(x, y);
		if(reload){
			g2d.setColor(Color.gray);
		}
		g2d.draw(getBounds());
		//g2d.drawImage(Assets.turret, (int)x, (int)y, width, height, null);
		g2d.dispose();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	public Rectangle range(){
		return new Rectangle((int)centerX-radius, (int)centerY-radius, radius*2, radius*2);
	}
	public Rectangle barrel(){
		return new Rectangle((int)centerX, (int)centerY, 10, width);
	}
	
	public void trackPlayer(){
		for(int i = 0; i< objects.size(); i++){
			Object temp = (Object) objects.get(i);
			if(temp == null){
				continue;
			}
			if(temp.id == ID.Player1){
				target = temp;
				/// check if player in range 
				if(temp.getBounds().intersects(range())){
					detected = true;
					double disX = temp.getX()+ temp.width/2-(x+width/2);
					double disY = temp.getY()+ temp.height/2 - (y+height/2);
					theta = Math.atan2(disY, disX);
					ray = new Line2D.Double((x+width/2), (y+height/2), temp.x+temp.width/2, temp.y+temp.height/2);
				}
				else{
					detected = false;
					startTime = 0;
				}
			}
		}
	}
	
	
	double diff = 0;
	public void smoothTracking(double speed){
		double rotationSpeed = speed;
		if(detected){
			smoothTheta = theta-.01;
			shoot();
		}
	}
	
	public void shoot(){
		fire = true;
		/// check if there is a non breakable block blocking the shot
		for(int i = 0; i<game.handler.getGameObjects().size(); i++){
			Object temp = game.handler.getGameObjects().get(i);
			if(temp == null){
				continue;
			}
			if(temp.getBounds().intersectsLine(ray)){
				if(temp.id == ID.Platform){
					Platform p  = (Platform) temp;
					if(!p.isfragile){
						fire = false;
					}
				}
				else if(temp.id == ID.Player1 && (temp.shrink || temp.expand)){
					fire = false;
				}
			}
			
		}
		
		if(ammoLeft < 1){
			reload = true;
		}
		if(startTime == 0 && fire){
			createBullet(game.handler.player.facing, ID.Turret);
			ammoLeft--;
		}
		if(startTime >= fireDelay && !reload && fire){
			createBullet(game.handler.player.facing, ID.Turret);
			startTime = 0;
			ammoLeft--;
		}
		startTime++;
	}
	
	public void createBullet(double angle, ID parent){
		game.handler.addObject(new Bullet(game, (int)x+width/2, (int)y+(height/2), bulletWidth, bulletHeight, theta, bulletSpeed, "turret", damage, range, 0, 5, ID.Bullet2, game.handler));
	}
	public void reload(){
		if(reload){
			if(temp <= 0){
				temp = width;
				ammoLeft = ammo;
				reload = false;
			}
			temp -= fraction;
		}
	}
	
}