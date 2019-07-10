package Worlds;

import Graphics.Graphics;

public class World {

	static float a = 0;

	public static void update() {
		//a++;
	}

	public static void render() {
		Graphics.setRotation(a);
		Graphics.setColor(0, 1, 0, 1f);
		Graphics.fillRect(0, 0, 200, 200);
//		for (int i = -1; i < 1; i++) {
//			for (int j = -1; j < 1; j++) {
//				Graphics.fillRect(i * 20, j * 20, 2, 2);
//			}
//		}
	}

}
