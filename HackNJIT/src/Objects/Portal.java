package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.ID;
import Main.Main;

public class Portal extends Object{
	
	private Main game;
	private Object obj;
	private int objW, objH;
	public double x, y,x2,y2,width,height,theta;
	private boolean shrink;
	private boolean expand;
	public Portal(Main game, double x, double y, double x2, double y2, int width, int height, ID id) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.width = width;
		this.height = height;
		theta = 0.0;
		shrink = false;
		expand = false;
	}

	@Override
	public void tick() {
		for(int i = 0; i<game.handler.getGameObjects().size(); i++){
			Object obj = game.handler.getGameObjects().get(i);
			if(obj == null){
				continue;
			}
			if((obj.id == ID.Player1 || obj.id == ID.Gun || obj.id == ID.Enemy ) && obj.getBounds().intersects(getBounds()) && !obj.shrink){
				obj.p = this;
				obj.shrink = true;
			}
			else if(obj.id == ID.Bullet1 || obj.id == ID.Bullet2){
				Bullet b = (Bullet)obj;
				if(b.ray != null)
				if(b.ray.intersects(getBounds())){
					obj.p = this;
					obj.shrink = true;
				}
			}
			
			
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.rotate(theta, x+width/2, y+height/2);
		g2d.setColor(Color.cyan);
		g2d.fill(getBounds());
		g2d.rotate(-theta,x+width/2, y+height/2);

		g2d.rotate(theta, x2+width/2, y2+height/2);
		g2d.setColor(Color.orange);
		g2d.fill(getBoundsExit());
		g2d.rotate(-theta,x2+width/2, y2+height/2);
		theta+=0.3;
		
		//g2d.dispose();
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	public Rectangle getBoundsExit() {
		return new Rectangle((int)x2, (int)y2, (int)width, (int)height);
	} 

}
