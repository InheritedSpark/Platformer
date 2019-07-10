package Graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Assets {
	public BufferedImage levelTiles, playerSpriteSheet, playerSpriteSheetflipped, player2SpriteSheet,
			player2SpriteSheetflipped, tileSet, enemy1SpriteSheet;
	public BufferedImage background;
	public BufferedImage dust;

	public static BufferedImage level = imageLoader.loadImage("/level.png");
	/// guns
	public BufferedImage gunSpritesRight;
	public BufferedImage gunSpritesLeft;
	public BufferedImage ak47Right, ak47Left;
	public BufferedImage uziRight, uziLeft;
	public BufferedImage lmgRight, lmgLeft;
	public BufferedImage sniperRight, sniperLeft;
	public BufferedImage explosion;
	public BufferedImage itemsImage;
	public static BufferedImage turret;

	public BufferedImage[] player1Sprites = new BufferedImage[6];
	public BufferedImage[] player1SpritesFlipped = new BufferedImage[6];
	public BufferedImage[] player2Sprites = new BufferedImage[6];
	public BufferedImage[] player2SpritesFlipped = new BufferedImage[6];

	public BufferedImage[] enemy1Sprites = new BufferedImage[5];
	public BufferedImage[] enemy1Attack = new BufferedImage[6];
	public BufferedImage[] enemy1Death = new BufferedImage[7];
	public BufferedImage enemyHit;

	public BufferedImage[] flyingEnemy = new BufferedImage[4];
	public BufferedImage[] flyingEnemyHit = new BufferedImage[2];

	public BufferedImage[] tiles = new BufferedImage[20];
	public BufferedImage[] dustEffect = new BufferedImage[11];
	public BufferedImage[] explosionEffect = new BufferedImage[8];
	public BufferedImage[] items = new BufferedImage[8];

	public void loadgraphics() {
		levelTiles = imageLoader.loadImage("/levelTiles.png");
		tileSet = imageLoader.loadImage("/tileset.png");
		playerSpriteSheet = imageLoader.loadImage("/player1.png");
		player2SpriteSheet = imageLoader.loadImage("/player2.png");
		playerSpriteSheetflipped = imageLoader.loadImage("/player1flipped.png");
		player2SpriteSheetflipped = imageLoader.loadImage("/player2flipped.png");

		enemy1SpriteSheet = imageLoader.loadImage("/enemy1.png");
		background = imageLoader.loadImage("/background1.png");
		dust = imageLoader.loadImage("/landingDust.png");
		explosion = imageLoader.loadImage("/explosion.png");

		// items
		itemsImage = imageLoader.loadImage("/items1.png");
		items[0] = itemsImage.getSubimage(0, 0, 24, 24);
		items[1] = itemsImage.getSubimage(24, 0, 24, 24);
		items[2] = itemsImage.getSubimage(48, 0, 24, 24);
		items[3] = itemsImage.getSubimage(72, 0, 24, 24);

		// player1 sprites //x, y, width, height
		player1Sprites[0] = playerSpriteSheet.getSubimage(0, 0, 118, 150);
		player1Sprites[1] = playerSpriteSheet.getSubimage(0, 150, 118, 150);
		player1Sprites[2] = playerSpriteSheet.getSubimage(0, 300, 118, 150);
		player1Sprites[3] = playerSpriteSheet.getSubimage(0, 450, 118, 150);
		player1Sprites[4] = playerSpriteSheet.getSubimage(0, 600, 118, 150);
		player1Sprites[5] = playerSpriteSheet.getSubimage(0, 750, 118, 150);

		player1SpritesFlipped[0] = playerSpriteSheetflipped.getSubimage(0, 0, 118, 150);
		player1SpritesFlipped[1] = playerSpriteSheetflipped.getSubimage(0, 150, 118, 150);
		player1SpritesFlipped[2] = playerSpriteSheetflipped.getSubimage(0, 300, 118, 150);
		player1SpritesFlipped[3] = playerSpriteSheetflipped.getSubimage(0, 450, 118, 150);
		player1SpritesFlipped[4] = playerSpriteSheetflipped.getSubimage(0, 600, 118, 150);
		player1SpritesFlipped[5] = playerSpriteSheetflipped.getSubimage(0, 750, 118, 150);

		// enemy1 move //x, y, width, height
		enemy1Sprites[0] = enemy1SpriteSheet.getSubimage(19, 152, 25, 30);
		enemy1Sprites[1] = enemy1SpriteSheet.getSubimage(81, 155, 28, 28);
		enemy1Sprites[2] = enemy1SpriteSheet.getSubimage(145, 150, 27, 33);
		enemy1Sprites[3] = enemy1SpriteSheet.getSubimage(210, 145, 30, 38);
		enemy1Sprites[4] = enemy1SpriteSheet.getSubimage(275, 150, 27, 33);

		// enemy1 attack
		enemy1Attack[0] = enemy1SpriteSheet.getSubimage(19, 153, 25, 30);
		enemy1Attack[1] = enemy1SpriteSheet.getSubimage(83, 153, 25, 30);
		enemy1Attack[2] = enemy1SpriteSheet.getSubimage(146, 150, 27, 35);
		enemy1Attack[3] = enemy1SpriteSheet.getSubimage(211, 145, 30, 38);
		enemy1Attack[4] = enemy1SpriteSheet.getSubimage(274, 150, 27, 33);
		enemy1Attack[5] = enemy1SpriteSheet.getSubimage(338, 156, 28, 26);

		// enemy1 death
		enemy1Death[0] = enemy1SpriteSheet.getSubimage(260, 185, 32, 30);
		enemy1Death[1] = enemy1SpriteSheet.getSubimage(142, 216, 30, 31);
		enemy1Death[2] = enemy1SpriteSheet.getSubimage(205, 211, 34, 36);
		enemy1Death[3] = enemy1SpriteSheet.getSubimage(270, 213, 32, 34);
		enemy1Death[4] = enemy1SpriteSheet.getSubimage(342, 217, 18, 29);
		enemy1Death[5] = enemy1SpriteSheet.getSubimage(411, 217, 9, 9);

		enemyHit = dye(enemy1Sprites[0], new Color(220, 20, 60));

		// flying enemy
		flyingEnemy[0] = imageLoader.loadImage("/flying/frame-1.png");
		flyingEnemy[1] = imageLoader.loadImage("/flying/frame-2.png");
		flyingEnemy[2] = imageLoader.loadImage("/flying/frame-3.png");
		flyingEnemy[3] = imageLoader.loadImage("/flying/frame-4.png");
		// hit
		flyingEnemyHit[0] = imageLoader.loadImage("/flying/got hit/frame-1.png");
		flyingEnemyHit[1] = imageLoader.loadImage("/flying/got hit/frame-2.png");

		// tiles
		tiles[0] = tileSet.getSubimage(0, 0, 32, 32); /// grass
		tiles[1] = tileSet.getSubimage(1 * 32, 0, 32, 32); /// grass tube
		tiles[2] = tileSet.getSubimage(2 * 32, 0, 32, 32); /// rock
		tiles[3] = tileSet.getSubimage(3 * 32, 0, 32, 32); /// marble ball
		tiles[4] = tileSet.getSubimage(4 * 32, 0, 32, 32); /// right arrow
		tiles[5] = tileSet.getSubimage(5 * 32, 0, 32, 32); /// left arrow
		tiles[6] = tileSet.getSubimage(6 * 32, 0, 32, 32); /// up arrow
		tiles[7] = tileSet.getSubimage(7 * 32, 0, 32, 32); /// flowers
		tiles[8] = tileSet.getSubimage(9 * 32, 1 * 32, 32, 32); /// stone
																/// platform
																/// middle
		tiles[18] = tileSet.getSubimage(8 * 32, 1 * 32, 32, 32); /// stone
																	/// platform
																	/// left
		tiles[19] = tileSet.getSubimage(10 * 32, 1 * 32, 32, 32); /// stone
																	/// platform
																	/// right

		tiles[9] = tileSet.getSubimage(0 * 32, 1 * 32, 32, 32); /// left grass
																/// corner
		tiles[10] = tileSet.getSubimage(1 * 32, 1 * 32, 32, 32);/// middle grass
		tiles[11] = tileSet.getSubimage(2 * 32, 1 * 32, 32, 32);/// right grass
		tiles[12] = tileSet.getSubimage(3 * 32, 1 * 32, 32, 32);/// one grass
																/// tile
		tiles[13] = tileSet.getSubimage(3 * 32, 2 * 32, 32, 32);/// one grass
																/// tile middle
		tiles[14] = tileSet.getSubimage(0 * 32, 2 * 32, 32, 32);/// left corner
																/// middle
		tiles[15] = tileSet.getSubimage(0 * 32, 3 * 32, 32, 32);/// left corner
																/// bottom
		tiles[16] = tileSet.getSubimage(2 * 32, 2 * 32, 32, 32);/// right corner
		tiles[17] = tileSet.getSubimage(1 * 32, 2 * 32, 32, 32);/// middle dirt

		// guns///
		gunSpritesRight = imageLoader.loadImage("/guns.png");
		gunSpritesLeft = imageLoader.loadImage("/gunsFlipped.png");
		turret = imageLoader.loadImage("/turret.png");

		ak47Right = gunSpritesRight.getSubimage(0, 0, 106, 40);
		ak47Left = gunSpritesLeft.getSubimage(660, 0, 106, 40);

		uziRight = gunSpritesRight.getSubimage(185, 75, 80, 40);
		uziLeft = gunSpritesLeft.getSubimage(500, 72, 80, 40);

		lmgRight = gunSpritesRight.getSubimage(640, 2, 100, 40);
		lmgLeft = gunSpritesLeft.getSubimage(30, 2, 100, 40);

		sniperRight = gunSpritesRight.getSubimage(0, 2 * 64, 140, 64);
		sniperLeft = gunSpritesLeft.getSubimage(645, 2 * 64, 120, 64);

		/// landing affects
		dustEffect[0] = dust.getSubimage(2 * 127, 0 * 127, 127, 127);
		dustEffect[1] = dust.getSubimage(3 * 127, 1 * 127, 127, 127);
		dustEffect[2] = dust.getSubimage(0 * 127, 1 * 127, 127, 127);
		dustEffect[3] = dust.getSubimage(1 * 127, 1 * 127, 127, 127);
		dustEffect[4] = dust.getSubimage(2 * 127, 1 * 127, 127, 127);
		dustEffect[5] = dust.getSubimage(3 * 127, 1 * 127, 127, 127);
		dustEffect[6] = dust.getSubimage(0 * 127, 2 * 127, 127, 127);
		dustEffect[7] = dust.getSubimage(1 * 127, 2 * 127, 127, 127);
		dustEffect[8] = dust.getSubimage(2 * 127, 2 * 127, 127, 127);
		dustEffect[9] = dust.getSubimage(3 * 127, 2 * 127, 127, 127);
		dustEffect[10] = dust.getSubimage(0 * 127, 3 * 127, 127, 127);

		/// explosion
		explosionEffect[0] = explosion.getSubimage(0, 0, 100, 100);
		explosionEffect[1] = explosion.getSubimage(1 * 128, 0 * 127, 128, 127);
		explosionEffect[2] = explosion.getSubimage(2 * 128, 0 * 127, 128, 127);
		explosionEffect[3] = explosion.getSubimage(3 * 128, 0 * 127, 128, 127);
		explosionEffect[4] = explosion.getSubimage(0 * 128, 1 * 127, 128, 127);
		explosionEffect[5] = explosion.getSubimage(1 * 128, 1 * 127, 128, 127);
		explosionEffect[6] = explosion.getSubimage(2 * 128, 1 * 127, 128, 127);
		explosionEffect[7] = explosion.getSubimage(3 * 128, 1 * 127, 128, 127);
	}

	public BufferedImage dye(BufferedImage image, Color color) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dyed.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0, 0, w, h);
		g.dispose();
		return dyed;
	}
}
