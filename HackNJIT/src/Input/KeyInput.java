package Input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Main.Handler;
import Main.ID;
import Objects.Object;



public class KeyInput extends KeyAdapter {
	
	Handler handler;
	
	public KeyInput(Handler handler){
		this.handler = handler;
	}
	
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		for(int i = 0; i < handler.getGameObjects().size(); i++){
			Object tempObject = handler.getGameObjects().get(i);
			if(tempObject == null){
				continue;
			}
			////player 1
			if(tempObject.getId() == ID.Player1){
				if(key == KeyEvent.VK_UP) handler.setUp(true);
				if(key == KeyEvent.VK_DOWN) handler.setDown(true);
				if(key == KeyEvent.VK_RIGHT) handler.setRight(true);
				if(key == KeyEvent.VK_LEFT) handler.setLeft(true);
				if(key == KeyEvent.VK_SPACE) handler.setShoot1(true);
				if(key == KeyEvent.VK_X) handler.setBuild(1);
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		for(int i = 0; i < handler.getGameObjects().size(); i++){
			Object tempObject = handler.getGameObjects().get(i);
			if(tempObject == null){
				continue;
			}
			
			if(tempObject.getId() == ID.Player1){
				if(key == KeyEvent.VK_UP) handler.setUp(false);
				if(key == KeyEvent.VK_DOWN) handler.setDown(false);
				if(key == KeyEvent.VK_RIGHT) handler.setRight(false);
				if(key == KeyEvent.VK_LEFT) handler.setLeft(false);
				if(key == KeyEvent.VK_SPACE) handler.setShoot1(false);
				if(key == KeyEvent.VK_X) handler.setBuild(-1);
			}
			
		}
	}

}
