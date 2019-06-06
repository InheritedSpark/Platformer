package Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.ID;
import Main.Main;

public class Platform extends Object{

	Main game;
	public boolean isfragile = false;
	public boolean isOccupied = false;
	
	public Platform(Main game, int x, int y, int width, int height, ID id, boolean isfragile) {
		super(game, x, y, width, height, id);
		this.game = game;
		this.isfragile = isfragile;
		health = 200;
	}

	@Override
	public void tick() {
		if(isfragile){
			if(health <= 0){
				game.handler.getGameObjects().remove(this);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(2));
        if(isfragile){
        	g2d.setColor(Color.gray);
        }
		g2d.draw(getBounds());
		/*if(isOccupied){
			g2d.fill(getBounds());
		}*/
		
//		g2d.drawImage(game.asset.tiles[18],(int) x, (int)y, null);
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

}
