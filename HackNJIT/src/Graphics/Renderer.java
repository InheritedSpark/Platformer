package Graphics;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;




public class Renderer {

	private static GLWindow window = null;
	private static GLProfile profile = null;

	public static int ScreenWidth = 1280;
	public static int ScreenHeight = 640;
	public static float unitsWide = 1000;

	public static void init() {
		GLProfile.initSingleton();
		profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);

		window = GLWindow.create(caps);
		window.setSize(ScreenWidth, ScreenHeight);
		window.setResizable(false);
		window.addGLEventListener(new EventListener());
		window.setVisible(true);
		// window.setFullscreen(true);

		// FPSAnimator an = new FPSAnimator(window, 60);
		// an.start();
		
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
	}
	
	public static void render(){
		if(window == null){
			return;
		}
		window.display();
		
		
	}

	public static int getWindowWidth() {
		return window.getWidth();
	}

	public static int getWindowHeight() {
		return window.getHeight();
	}

	public static GLProfile getProfile() {
		// TODO Auto-generated method stub
		return profile;
	}
}
