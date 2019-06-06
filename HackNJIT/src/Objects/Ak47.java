package Objects;

import Main.ID;
import Main.Main;

public class Ak47 extends Gun{

	public Ak47(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id, type);
	
		bulletType = 0;
		ammo = 32;
		ammoLeft = ammo;
		spriteRight = game.asset.ak47Right;
		spriteLeft = game.asset.ak47Left; 
		lineOfSight = false;
		bulletSpeed = 75;
		damage = 10;
		pushback = 2;
		shake = 3;
		fireDelay = 7;
		recoil = 5;
		reloadSpeed = 300f;
		range = 10000;
		bulletWidth = 9;
		bulletHeight = 7;
	}
	

}
