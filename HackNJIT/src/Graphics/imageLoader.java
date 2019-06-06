package Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class imageLoader {
	private static BufferedImage image;
	
	public static BufferedImage loadImage(String path){
		try {
			image = ImageIO.read(imageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
