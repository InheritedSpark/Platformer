package Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class imageLoader {
	private static BufferedImage image = null;
	private Texture tex = null;
	
	public static BufferedImage loadImage(String path){
		try {
			image = ImageIO.read(imageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public imageLoader(String path){
		URL url = imageLoader.class.getResource(path);
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Texture getTexture(){
		if(image == null){
			return null;
		}
		if(tex == null){
			tex = AWTTextureIO.newTexture(Renderer.getProfile(), image, true);
		}
		return tex;
	}
}
