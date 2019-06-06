package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.ID;
import Main.Main;

public class MovingPlatform extends Object{

	private double x,y;
	private int width, height;
	public double [][] path;
	public double speed, vel, theta, xdis, ydis;
	public MovingPlatform(Main game, double x, double y, int width, int height, ID id) {
		super(game, x, y, width, height, id);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.path = new double[2][2];
		path[0][0] = x;
		path[0][1] = y;
		path[1][0] = x;
		path[1][1] = y-22*32;
//		path[2][0] = x + 800;
//		path[2][1] = y;
//		path[3][0] = x + 500;
//		path[3][1] = y;
		vel = 4;
		speed = vel;
	}
	

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
		for(int i = 0; i<path.length; i++){
			g2d.fill(new Rectangle((int)path[i][0], (int)path[i][1], 10, 10));
			
			g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
			if(i < path.length-1){
				g2d.drawLine((int)path[i][0]+10/2, (int)path[i][1]+10/2, (int)path[i+1][0]+10/2, (int)path[i+1][1]+10/2);
			}
			g2d.setStroke(new BasicStroke(3));
		}
		
		g2d.draw(getBounds());
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x-width/2, (int)y-height/2, width, height);
	}


	public boolean start = false;
	public void tick() {
		if(start){
			move();	
		}
		
	}
	
	
	boolean move = false;
	boolean moveback = false;
	int i = 1;
	double xdiff, ydiff;
	double targetx, targety;
	public void move(){
		targetx = path[i][0]+10/2;
		targety = path[i][1]+10/2;
		move = false;
		if(speed != 0){
			xdis = (x) - (targetx);
			ydis = (y) - (targety);
			xdiff = Math.abs((x) - (targetx));
			ydiff = Math.abs((y) - (targety));
			theta = Math.atan2(ydis, xdis);
			x -= speed * Math.cos(theta);
			y -= speed * Math.sin(theta);
			
			if(xdiff < speed && ydiff < speed){
				speed = 0;
			}
		}
		
		
		
		if(speed == 0){
			move = true;
		}
		
		if(move){
			if(moveback){
				i--;
				if(i < 0){
					i = 1;
					moveback = false;
				}
			}else{
				i++;
			}
			speed = vel;
			if(i == path.length){
				moveback = true;
				i = path.length -2;
			}
		}
		
	}
	

}
