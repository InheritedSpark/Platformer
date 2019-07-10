package Graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Graphics {

	// Colors
	private static float r = 1;
	private static float g = 1;
	private static float b = 1;
	private static float a = 1;
	// Rotation
	private static float theta = 0;

	public static void drawImage(imageLoader image, float x, float y, float width, float height) {
		GL2 gl = EventListener.gl;

		Texture tex = image.getTexture();
		tex.enable(gl);
		tex.bind(gl);

		if (tex != null) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
		}

		gl.glTranslatef(x, y, 0);
		gl.glRotatef(theta, 0, 0, 1);

		gl.glColor4f(1, 1, 1, 0.85f);
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0,1);
		gl.glVertex2f(-width / 2, -height / 2);
		gl.glTexCoord2f(1,1);
		gl.glVertex2f(width / 2, -height / 2);
		gl.glTexCoord2f(1,0);
		gl.glVertex2f(width / 2, height / 2);
		gl.glTexCoord2f(0,0);
		gl.glVertex2f(-width / 2, height / 2);
		gl.glEnd();
		gl.glFlush();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

		gl.glRotatef(-theta, 0, 0, 1);
		gl.glTranslatef(-x, -y, 0);
	}

	public static void fillRect(float x, float y, float width, float height) {
		GL2 gl = EventListener.gl;

		gl.glTranslatef(x, y, 0);
		gl.glRotatef(theta, 0, 0, 1);

		gl.glColor4f(r, g, b, a);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(-width / 2, -height / 2);
		gl.glVertex2f(width / 2, -height / 2);
		gl.glVertex2f(width / 2, height / 2);
		gl.glVertex2f(-width / 2, height / 2);
		gl.glEnd();
		gl.glFlush();

		gl.glRotatef(-theta, 0, 0, 1);
		gl.glTranslatef(-x, -y, 0);
	}
	public static void drawRect(float x, float y, float width, float height) {
		GL2 gl = EventListener.gl;

		gl.glTranslatef(x, y, 0);
		gl.glRotatef(theta, 0, 0, 1);

		gl.glColor4f(r, g, b, a);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(-width / 2, -height / 2);
		gl.glVertex2f(width / 2, -height / 2);
		gl.glVertex2f(width / 2, height / 2);
		gl.glVertex2f(-width / 2, height / 2);
		gl.glEnd();
		gl.glFlush();

		gl.glRotatef(-theta, 0, 0, 1);
		gl.glTranslatef(-x, -y, 0);
	}

	public static void setColor(float r, float g, float b, float a) {
		Graphics.r = Math.max(0, Math.min(1, r));
		Graphics.g = Math.max(0, Math.min(1, g));
		Graphics.b = Math.max(0, Math.min(1, b));
		Graphics.a = Math.max(0, Math.min(1, a));
	}

	public static void setRotation(float theta) {
		Graphics.theta = theta;
	}
}
