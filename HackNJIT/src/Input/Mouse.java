package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Main.Main;

public class Mouse implements MouseListener, MouseMotionListener {

	public int x,y;
	private Main game;
	public Mouse(Main game){
		this.game = game;
	}
	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.handler.setMouse1(true, e.getX(), e.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		game.handler.setMouse1(false,e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		game.handler.setMouse1(game.handler.isMouse1(), x+(int)game.handler.cam.x, y+(int)game.handler.cam.y);
		
	}

}
