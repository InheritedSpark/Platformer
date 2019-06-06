package Objects;

import Main.ID;
import Main.Main;

public class Uzi extends Gun {

	public Uzi(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id, type);
		
		bulletType = 0;
		ammo = 45;
		ammoLeft = ammo;
		spriteRight = game.asset.uziRight;
		spriteLeft = game.asset.uziLeft; 
		lineOfSight = false;
		bulletSpeed = 20;
		damage = 5;
		pushback = 1;
		shake = 2;
		fireDelay = 4;
		recoil = 0.7;
		reloadSpeed = 50;
		range = 10000;
		bulletWidth = 5;
		bulletHeight = 4;
	}
	

}
