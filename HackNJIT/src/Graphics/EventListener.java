package Graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import Main.Handler;

public class EventListener implements GLEventListener {

	public static GL2 gl = null;
	private imageLoader image;
	float a = 0;
	
	public void display(GLAutoDrawable draw) {
		// clear the screen
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		Handler.render();
		
	}

	public void dispose(GLAutoDrawable draw) {
		System.exit(0);
	}

	Handler han;
	public void init(GLAutoDrawable draw) {
		gl = draw.getGL().getGL2();
		
		gl.setSwapInterval(0);
		
		gl.glClearColor(1, 1, 1, 1);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		image = new imageLoader("/background1.jpg");
		Handler.init();
		
	}

	public void reshape(GLAutoDrawable draw, int x, int y, int width, int height) {
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		float unitsTall = Renderer.getWindowHeight()/(Renderer.getWindowWidth()/Renderer.unitsWide);
		
		gl.glOrtho(-Renderer.unitsWide/2, Renderer.unitsWide/2, -unitsTall/2, unitsTall/2, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

}
