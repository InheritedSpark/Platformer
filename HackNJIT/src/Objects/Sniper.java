package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Main.ID;
import Main.Main;

public class Sniper extends Gun{
	
	public Sniper(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id, type);
		
		bulletType = 0;
		spriteRight = game.asset.sniperRight;
		spriteLeft = game.asset.sniperLeft; 
		lineOfSight = true;
		range = 10000;
		bulletSpeed = 100;
		damage = 50;
		pushback = 5;
		shake = 15;
		fireDelay = 5;
		recoil = 10;
		ammo = 1;
		ammoLeft = ammo;
		reloadSpeed = 50f;
		bulletWidth = 20;
		bulletHeight = 8;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Dialog", Font.BOLD, 18));
//		g2d.draw(getBounds());
		if (equipped){
			if((parent == ID.Player1 || parent == ID.Player2)){
				reload(g2d);
				if(player.facing > 0){
					g2d.drawImage(spriteRight, (int)x, (int)y, width, height, null);
					if(lineOfSight){
						g2d.setColor(Color.red);
						g.drawLine((int)x+width-5, (int)y+25, (int)x+250, (int)y+25);
					}
				}
				else{
					g2d.drawImage(spriteLeft, (int)x, (int)y, width, height, null);
					if(lineOfSight){
						g2d.setColor(Color.red);
						g.drawLine((int)x, (int)y+25, (int)x-250, (int)y+25);
					}
				}
			}
		}
		else{
			g2d.drawImage(spriteLeft, (int)x, (int)y, width, height, null);
		}
	}
}
