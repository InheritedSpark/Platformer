package Objects;

import Main.ID;
import Main.Main;

public class MachineGun extends Gun {

	public MachineGun(Main game, int x, int y, int width, int height, ID id, String type) {
		super(game, x, y, width, height, id, type);
		bulletType = 0;
		ammo = 100;
		ammoLeft = ammo;
		spriteRight = game.asset.lmgRight;
		spriteLeft = game.asset.lmgLeft; 
		lineOfSight = false;
		bulletSpeed = 75;
		damage = 15;
		pushback = 1;
		shake = 10;
		fireDelay = 8;
		recoil = 0.4;
		reloadSpeed = 1000;
		range = 10000;
		bulletWidth = 16;
		bulletHeight = 8;
	}
}
