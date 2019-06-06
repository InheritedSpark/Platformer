package Main;

public class Lighting {

	Main game;
	private int [] lm;
	private int [] lb;
	
	public Lighting(Main game){
		this.game = game;
		lm = new int [game.Width*game.Height];
		lb = new int [game.Width*game.Height];	
	}
	
	public void setLightMap(int x, int y, int value){
		if((x < 0 || x >= 0)){
			
		}
	}
	
	
}
