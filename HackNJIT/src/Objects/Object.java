package Objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import Main.ID;
import Main.Main;

public abstract class Object {
	
	protected double x, y;
	public int facing;
	protected int width, height;
	protected double originalW, originalH;
	protected float velx = 0,vely = 0;
	protected float accx = 0,accy = 0;
	protected ID id;
	protected final int XVELOCITY  = 10;
	protected final int YVELOCITY  = 10;
	protected Random ran;
	public Portal p;
	public int health;
	
	public boolean shrink, expand, safe2Remove;
	
	public Object(Main game,double x2,double y2, int width, int height, ID id){
		this.x = x2;
		this.y = y2;
		this.width = width;
		this.height = height;
		this.originalW = width;
		this.originalH = height;
		this.id = id;
		this.ran = new Random();
		
		this.shrink = false;
		this.expand = false;
		this.safe2Remove = false;
		
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	public void teleport(){
		if(shrink){
			if(width < originalW * .20 || height < originalH *.20){
				shrink = false;
				x = p.getBoundsExit().getX()+p.getBoundsExit().getWidth()/2;
				y = p.getBoundsExit().getY()+p.getBoundsExit().getHeight()/2;
				expand = true;
			}else{
				System.out.println(width+" "+originalW/32);
				width -= originalW/32;
				height -= originalH/32;
				System.out.println(width);
				x = p.getBounds().getX()+p.getBounds().getWidth()/2-width/2;
				y = p.getBounds().getY()+p.getBounds().getHeight()/2-height/2;

			}
		}
		else if(expand){
			if(width == originalW && height == originalH){
				expand = false;
			}
			
			if(width != originalW){
				width += originalW/32;
			}
			if(height != originalH){
				height += originalH/32;
			}
			
			
			
			x = p.getBoundsExit().getX()+p.getBoundsExit().getWidth()/2-width/2;
			y = p.getBoundsExit().getY()+p.getBoundsExit().getHeight()/2-height/2;
		}
	}
	
	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getVelx() {
		return velx;
	}

	public void setVelx(float velx) {
		this.velx = velx;
	}

	public float getVely() {
		return vely;
	}

	public void setVely(float vely) {
		this.vely = vely;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	public void setWidth(int tx) {
		// TODO Auto-generated method stub
		this.width = tx;
	}

	
}
