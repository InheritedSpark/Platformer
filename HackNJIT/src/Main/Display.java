package Main;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	Canvas canvas;
	
	public Display(String name, int Width, int Height){
		frame = new JFrame(name);
		frame.setPreferredSize(new Dimension(Width,Height));
		frame.setMaximumSize(new Dimension(Width,Height));
		frame.setMinimumSize(new Dimension(Width,Height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas = new Canvas();
		canvas.setMaximumSize(new Dimension(Width,Height));
		canvas.setMinimumSize(new Dimension(Width,Height));
		frame.add(canvas);
		frame.pack();
	}
	
	public JFrame getFrame(){
		return frame;
	}
	public Canvas getCanvas(){
		return canvas;
	}
}
