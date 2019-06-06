package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

public class Camera {
	Handler handler;
	Main game;
	public double x, y, width, height, WIDTH, HEIGHT, camLength;
	public double windowArea, cameraArea;
	public int safeAreaX, safeAreaY;
	public double zoomXRatio, zoomYRatio;
	public double camXCenter,camYCenter;
	public double zoomRatio;
	public boolean player2 = false;
	public double offsetX, offsetY;
	public Objects.Object target;

	public Camera(Main game, Handler handler) {
		this.handler = handler;
		this.game = game;
		this.x = 0;
		this.y = 0;
		this.width = game.Width;
		this.height = game.Height;
		this.safeAreaX = 128;
		this.safeAreaY = (int) 128;
	}

	public void tick() {
		camXCenter = x+width/2;
		camYCenter = y+height/2;
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.blue);
		g2d.draw(getBounds());
		g2d.setColor(Color.black);
		g2d.draw(getBounds2());
		g2d.setColor(Color.red);
		g2d.fill(new Rectangle((int)camXCenter-5, (int)camYCenter-5, 10, 10));
		g2d.draw(new Rectangle((int)camXCenter-safeAreaX/2, (int)camYCenter-safeAreaY/2, safeAreaX, safeAreaY));
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x,(int) y, (int)width, (int)height);
	}
	public Rectangle getBounds2() {
		return new Rectangle((int)x-100,(int) y-100, (int)width+200, (int)height+200);
	} 
	
	double targetXCenter, targetYCenter, dx,dy;
	int factor = 10;
	
	public void center(Objects.Object target){
		targetXCenter = game.Width/2; 
		targetYCenter = game.Height/2;
				
		if(target.getBounds().x + target.getBounds().width-game.Width/2 >= (handler.gameWidth*32) - game.Width){
			targetXCenter = handler.gameWidth*32 - game.Width/2;
		}
		
		else if((target.getBounds().x + target.getBounds().width) >= game.Width/2){ // center on target only when it crosses half of the screen	
			targetXCenter = target.getBounds().x+target.getBounds().width/2;
		}
		
		if(target.getBounds().y + target.getBounds().height-game.Height/2 >= (handler.gameHeight*32) - game.Height){
			targetYCenter = handler.gameHeight*32 - game.Height/2+32;
		}
		else if((target.getBounds().y + target.getBounds().height) >= game.Height/2){
			targetYCenter = target.getBounds().y+target.getBounds().height/2;
		}	
		
		dx = camXCenter - targetXCenter;
		dy = camYCenter - targetYCenter;
		
		if(dx >= safeAreaX/2 || dx <= -safeAreaX/2){
			x += -dx/factor;
		}
		else{
			x += -dx/factor;
		}
		if(dy >= safeAreaY/2 || dy <= -safeAreaY/2){	
			y += -dy/factor;
		}
		else{
			y += -dy/factor;
		}
		
	}
	
	public void screenShake(int shake){
		int xval = ThreadLocalRandom.current().nextInt(-shake, shake + 1);
		int yval = ThreadLocalRandom.current().nextInt(-shake, shake + 1);
		x += xval;
		y += yval;
	}
	
}
