package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import Graphics.Tile;
import Objects.Ak47;
import Objects.Enemy;
import Objects.FlyEnemy;
import Objects.MachineGun;
import Objects.MovingPlatform;
import Objects.Object;
import Objects.Platform;
import Objects.Player;
import Objects.Portal;
import Objects.Sniper;
import Objects.Turret;
import Objects.Update;

public class Handler {
	private boolean up, down, left, right, shoot, up2, down2, left2, right2, shoot2, mouse1, build;
	public int grenade;
	Main game;
	private LinkedList<Object> gameObjects;
	private LinkedList<Tile> tiles;
	private LinkedList<Object>[] layers;
	private ArrayList<Object> objects;
	public ArrayList<Update> updates;
	public Player player;
	public boolean winner;
	private int weaponCount;
	private int mouseX;
	private int mouseY;
	public int clicked = -1;
	public int gameWidth, gameHeight;

	// Camera///
	public Camera cam;
	public boolean shake1 = false;
	public boolean shake2 = false;
	public boolean restart = true;

	public static Font font = new Font("Comic Sans MS", Font.PLAIN + Font.ITALIC, 40);

	public Handler(Main game) {
		this.game = game;
		gameObjects = new LinkedList<Object>();
		tiles = new LinkedList<Tile>();
		objects = new ArrayList<Object>();
		updates = new ArrayList<Update>();
		cam = new Camera(game, this);
		weaponCount = 0;
		this.grenade = -1;

	}

	public void layers(int layers_number) {
		layers = new LinkedList[layers_number];
		for(int i = 0; i < layers_number; i++){
			layers[i] = new LinkedList<Object>(); 
		}
	}

	int tar = 100;
	boolean spawn = true;

	double alpha = 10;
	boolean dec = false;

	public void tick() {
		if (!restart) {
			weaponCount = 0;

			// add objects if they appear in camera
			for (int i = 0; i < objects.size(); i++) {
				boolean found = false;
				Object obj = objects.get(i);
				if (obj.getBounds().intersects(cam.getBounds2())) { // check if
																	// object is
																	// in camera
					for (int j = 0; j < gameObjects.size(); j++) { // find the
																	// object
						if (obj == gameObjects.get(j)) { // dont add if found
															// and break from
															// the loop
							found = true;
							break;
						}
					}
					if (!found) { // add only when not found
						gameObjects.add(obj);
					}
				}
			}

			/// spawn weapons
			if (weaponCount == 0 && player != null) {
				spawnWeapons();
			}

			for (int i = 0; i < gameObjects.size(); i++) {
				Object gameObj = gameObjects.get(i);
				if (gameObj == null) {
					continue;
				}
				if (!gameObj.getBounds().intersects(cam.getBounds2())) {
					if (gameObj.getId() == ID.Platform) {
						Platform p = (Platform) gameObj;
						if (!p.isfragile) { // not fragile
							gameObjects.remove(gameObj);
						}
					}
				}
				if (!gameObj.getBounds().intersects(cam.getBounds())) { // outside
																		// the
																		// camera
					if (gameObj.getId() == ID.Bullet1 || gameObj.getId() == ID.Bullet2) {
						gameObjects.remove(gameObj);
					} else if (gameObj.getId() == ID.MovePlat || gameObj.getId() == ID.Grenade
							|| gameObj.getId() == ID.Effect) {
						gameObj.tick();
					}

				} else { // inside the camera
					if (gameObj.getId() != ID.Player1) {
						gameObj.tick();
					}
					// count weapons
					if (gameObj.getId() == ID.Gun) {
						weaponCount++;
					}
				}
				if (player == null) {
					if (gameObj.getId() == ID.Player1) {
						player = (Player) gameObj;
					}
				}
			}

			// camera//
			cam.tick();
			rotateCamera();
			/// player
			if (player != null) {
				cam.center(player);
				player.tick();
			}

			for (int i = 0; i < updates.size(); i++) {
				Update temp = updates.get(i);
				temp.tick();
				if (temp.remove) {
					updates.remove(temp);
				}
			}
		}
	}

	Random rand = new Random();
	private double tx = 1, ty = 1;
	public int shake = 0;
	public double theta = 0;
	boolean rotate = false;

	public void render(Graphics g) {
		// tx+=0.001;
		Graphics2D g2d = (Graphics2D) g;
		// cam.render(g2d);
		g2d.setStroke(new BasicStroke(5));
		g2d.rotate(theta, game.Width / 2, game.Height / 2);
		g2d.translate(-cam.x, -cam.y);
		// g2d.scale(tx, tx);
		// cam.render(g2d);
		if (!restart) {
			for (int i = 0; i < gameObjects.size(); i++) {
				Object gameObj = gameObjects.get(i);
				if (gameObj == null) {
					continue;
				}
				// render only objects in camera
				// if(cam.getBounds().intersects(gameObj.getBounds())){
				gameObj.render(g2d);
				// continue;
				// }
				if (gameObj.getId() == ID.Portal) {
					Portal p = (Portal) gameObj;
					if (p.getBoundsExit().intersects(cam.getBounds())) {
						gameObj.render(g2d);
						continue;
					}
				} else if (gameObj.getId() == ID.MovePlat) {
					MovingPlatform mp = (MovingPlatform) gameObj;
					boolean draw = false;
					for (int k = 0; k < mp.path.length; k++) {
						if (new Rectangle((int) mp.path[k][0], (int) mp.path[k][1], 10, 10)
								.intersects(cam.getBounds())) {
							draw = true;
							break;
						}
					}
					if (draw) {
						mp.render(g2d);
					}
				}
			}
		}
		if (player != null) {
			player.render(g2d);
		}
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.get(i);
			tile.render(g2d);
		}
		// updates
		for (int i = 0; i < updates.size(); i++) {
			updates.get(i).render(g2d);
		}
		// render the player on top of everything
		// loadLevel(game.asset.level);
		// shake1 = false;s
		g2d.translate(cam.x, cam.y);
		g2d.rotate(-theta, game.Width / 2, game.Height / 2);
		shake = 0;
		// g2d.scale(-cam.zoomRatio, -cam.zoomRatio);
		// g2d.translate(-((game.Width/2)-(cam.camXCenter*cam.zoomRatio)+shake),
		// -((game.Height/2)-(cam.camYCenter*cam.zoomRatio)+shake));

		if (restart || alpha > 0) {
			g2d.setColor(new Color(0, 0, 0, (int) alpha));
			g2d.fill(new Rectangle(0, 0, game.Width, game.Height));
			if (dec) {
				alpha -= 0.6;
			} else {
				alpha += 5;
			}
			if (alpha >= 255) {
				alpha = 255;
				restart();
				dec = true;
			} else if (alpha <= 0) {
				alpha = 0;
				dec = false;
			}

		}

	}

	public void restart() {
		restart = false;
		rotate = false;
		theta = 0.00;
		gameObjects.clear();
		objects.clear();
		weaponCount = 0;

		// level gen
		// LevelGenerator lg = new LevelGenerator(10, 5, 5);
		// lg.init();
		// int [] [] map = lg.generatePath();
		// lg.loadMap(this);
		//
		// for(int i = 0; i < map.length;i++){
		// for(int j = 0; j < map[0].length; j++){
		// System.out.print(map[i][j]+" ");
		// }
		// System.out.println();
		// }
		// System.out.println();

		player = new Player(game, 2 * 32, 14 * 32, 64, 64, ID.Player1, this);
		addObject(new MovingPlatform(game, 173 * 32, 32 * 32, 128, 32, ID.MovePlat));
		addObject(new Portal(game, 79 * 32, 30 * 32, 85 * 32, 30 * 32, 32, 32, ID.Portal));
		addObject(new Portal(game, 117 * 32, 36 * 32, 117 * 32, 16 * 32, 32, 32, ID.Portal));
		addObject(new Portal(game, 154 * 32, 33 * 32, 85 * 32, 6 * 32, 32, 32, ID.Portal));
		addObject(new Portal(game, 105 * 32, 5 * 32, 158 * 32, 33 * 32, 32, 32, ID.Portal));
		addObject(new Portal(game, 143 * 32, 26 * 32, 183 * 32, 33 * 32, 32, 32, ID.Portal));

		// addObject(new Laser(game, 25*32, 29*32, 10, 200, 0, ID.Laser));

		// load level//
		loadLevel(game.asset.level);
		// loadLevelTiles(game.asset.level);
		addObject(player);

		setLeft(false);
		setRight(false);
		setDown(false);
		setUp(false);
	}

	public void rotateCamera() {
		if (player != null) {
			if (player.shrink) {
				rotate = true;
				;
			} else if (player.expand) {
				rotate = false;
			}
		}
		if (rotate) {
			theta += 0.01;
		} else {
			if (theta > 0) {
				theta -= 0.01;

			} else {
				theta = 0.00;
			}

		}
	}

	///// Spawn new weapons
	public void spawnWeapons() {
		// addObject(new Sniper(game, game.Height/3 + 128, 100, 80, 50, ID.Gun,
		// "sniper"));
		// addObject(new Ak47(game, game.Height / 2 - 110, 100, 60, 32, ID.Gun,
		// "ak47"));
		// addObject(new Uzi(game, game.Height / 4 - 110, 100, 50, 32, ID.Gun,
		// "uzi"));
		// addObject(new MachineGun(game, game.Height / 5 - 110, 100, 50, 32,
		// ID.Gun, "lmg"));
	}

	// filters
	public BufferedImage filter(BufferedImage image) {
		float ninth = 1.0f / 9.0f;
		float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };
		BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
		BufferedImage filtered = blur.filter(image, null);
		return filtered;
	}

	////////////////////////// Getter and
	////////////////////////// Setter////////////////////////////////////////////////////////////////////////
	public void addTile(Tile object) {
		tiles.add(object);
	}

	public void addObject(Object object) {
		gameObjects.add(object);
	}

	public void removeObject(Object object) {
		gameObjects.remove(object);
	}

	public LinkedList<Object> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(LinkedList<Object> gameObjects) {
		this.gameObjects = gameObjects;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isShoot1() {
		return shoot;
	}

	public void setShoot1(boolean shoot) {
		this.shoot = shoot;
	}

	public boolean isUp2() {
		return up2;
	}

	public void setUp2(boolean up2) {
		this.up2 = up2;
	}

	public boolean isDown2() {
		return down2;
	}

	public void setDown2(boolean down2) {
		this.down2 = down2;
	}

	public boolean isLeft2() {
		return left2;
	}

	public void setLeft2(boolean left2) {
		this.left2 = left2;
	}

	public boolean isRight2() {
		return right2;
	}

	public void setMouse1(boolean mouse1, int mouseX, int mouseY) {
		this.mouse1 = mouse1;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public boolean isMouse1() {
		return mouse1;
	}

	public void setRight2(boolean right2) {
		this.right2 = right2;
	}

	public boolean isShoot2() {
		return shoot2;
	}

	public void setShoot2(boolean shoot2) {
		this.shoot2 = shoot2;
	}

	public int[] getMouse() {
		return new int[] { mouseX, mouseY };
	}

	public boolean getBuild() {
		return this.build;
	}

	public void setBuild(int clicked) {
		if (this.clicked >= 1) {
			this.clicked = 1;
		} else if (this.clicked <= -1) {
			this.clicked = -1;
		}
		this.clicked += clicked;
		if (this.clicked == 0)
			this.build = true;
		else
			this.build = false;
	}

	public void setGrenade(int shoot) {
		if (grenade == -1) {
			if (shoot > 0)
				this.grenade += shoot * 2;
		} else if (grenade == 1) {
			if (shoot < 0)
				this.grenade += shoot;
		}
		// if (grenade == 0) {
		// grenade = -1;
		// }
	}

	public int getGrenade() {
		return this.grenade;
	}

	public void loadLevel(BufferedImage image) {
		int xstart = (int) cam.x / 32;
		int ystart = (int) cam.y / 32;
		int xend = xstart + (int) cam.width / 32;
		int yend = ystart + (int) cam.height / 32;

		if (xstart < 0)
			xstart = 0;
		if (ystart < 0)
			ystart = 0;
		int w = image.getWidth();
		int h = image.getHeight();
		gameWidth = w;
		gameHeight = h;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pixel = image.getRGB(i, j);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				// spawn only visible objects
				if (red == 255 && green == 0 && blue == 0) {
					//// platforms--> red
					Object obj = new Platform(game, i * 32, j * 32, 32, 32, ID.Platform, false);
					if (obj.getBounds().intersects(cam.getBounds())) {
						addObject(obj);
					}
					objects.add(obj);
				} else if (red == 0 && green == 0 && blue == 255) {
					//// player---> blue
					player = new Player(game, i * 32, j * 32, 64, 64, ID.Player1, this);
					addObject(player);
				}

				else if (red == 0 && green == 255 && blue == 0) {
					//// Enemy---> green
					Object enemy = new Enemy(game, i * 32, j * 32, 64, 64, ID.Enemy);
					addObject(enemy);
				} else if (red == 255 && green == 255 && blue == 255) {
					//// platform fragile ---> white
					Platform mp = new Platform(game, i * 32, j * 32, 32, 32, ID.Platform, true);
					addObject(mp);
				} else if (red == 255 && green == 255 && blue == 0) {
					//// Turret---> yellow
					Turret t = new Turret(game, i * 32, j * 32, 32, 32, ID.Turret, "turret", 400);
					addObject(t);
				} else if (red == 255 && green == 0 && blue == 255) {
					addObject(new Ak47(game, i * 32, j * 32, 60, 32, ID.Gun, "ak47"));
				} else if (red == 155 && green == 155 && blue == 155) {
					addObject(new MachineGun(game, i * 32, j * 32, 50, 32, ID.Gun, "lmg"));
				} else if (red == 255 && green == 155 && blue == 155) {
					addObject(new Sniper(game, i * 32, j * 32, 64, 50, ID.Gun, "sniper"));
				} else if (red == 250 && green == 250 && blue == 155) {
					//// add fly enemy
					addObject(new FlyEnemy(game, player, i * 32, j * 32, 64, 64, 350, ID.Enemy));
				}

			}
		}
		System.out.println(gameObjects.size());

	}

	public void loadLevelTiles(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pixel = image.getRGB(i, j);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (red == 255 && green == 0 && blue == 0) {
					//// middle grass--> red
					addTile(new Tile(game.asset.tiles[8], i * 32, j * 32, 32, 32));
				}
				// else if(red == 0 && green == 0 && blue == 255 ){
				// ////left grass corner---> blue
				// addTile(new Tile(game.asset.tiles[9], i*32,j*32,32,32));
				// }
				//
				// else if(red == 0 && green == 255 && blue == 0 ){
				// ////right grass corner---> green
				// addTile(new Tile(game.asset.tiles[18], i*32,j*32,32,32));
				// }
				// else if(red == 255 && green == 255 && blue == 255 ){
				// ////one grass tile---> white
				// addTile(new Tile(game.asset.tiles[19], i*32,j*32,32,32));
				// }
				// else if(red == 100 && green == 50 && blue == 0 ){
				// ////one grass tile middle---> white
				// addTile(new Tile(game.asset.tiles[13], i*32,j*32,32,32));
				// }
				// else if(red == 120 && green == 25 && blue == 0 ){
				// ////left corner middle
				// addTile(new Tile(game.asset.tiles[14], i*32,j*32,32,32));
				// }
				// else if(red == 0 && green == 71 && blue == 136 ){
				// ////right corner
				// addTile(new Tile(game.asset.tiles[16], i*32,j*32,32,32));
				// }
				// else if(red == 136 && green == 96 && blue == 0 ){
				// ////middle dirt
				// addTile(new Tile(game.asset.tiles[17], i*32,j*32,32,32));
				// }
			}
		}
	}

}
