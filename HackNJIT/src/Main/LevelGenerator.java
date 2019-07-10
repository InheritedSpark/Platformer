package Main;

import java.util.Random;

import Objects.Platform;

public class LevelGenerator {

	int chunkSize;
	int mapWidth;
	int mapHeight;
	int map[][];
	String path;
	
	Random ran;	
	
	public LevelGenerator(int chunkSize, int mapWidth, int mapHeight){
		this.chunkSize = chunkSize;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.map = new int [mapWidth][mapHeight];
		this.ran = new Random();
		this.path = "";
	}
	
	public void init(){
		for(int j = 0; j<mapHeight;j++){
			for(int i = 0; i<mapWidth; i++){
				map[j][i] = 0;	
			}	
		}
	}
	
	public int[][] generatePath(){
		/*
		 * 1 left exit
		 * 2 right exit
		 * 3 top exit
		 * 4 bottom exit
		*/
		boolean run = true;
		int i = 1;
		int startRoomX = ran.nextInt(mapWidth);
		int startRoomY = 0;//ran.nextInt(mapHeight);
		System.out.println(startRoomY+" "+startRoomX+"\n");
		
		map[startRoomY][startRoomX] = 0;
		
		while(run){
			int next = ran.nextInt(4)+1;
//			System.out.println("step: "+next);
			switch(next){
				case 1: // left exit
					if(startRoomX - 1 >= 0 && map[startRoomY][startRoomX-1] == 0 ){
						i++;
						map[startRoomY][startRoomX] = next;
						startRoomX--;
					}
					break;
				case 2: // right exit
					if(startRoomX + 1 < mapWidth && map[startRoomY][startRoomX+1] == 0){
						i++;
						map[startRoomY][startRoomX] = next;
						startRoomX++;
					}
					break;
				case 3: // top exit
					if(startRoomY - 1 >= 0 && map[startRoomY-1][startRoomX] == 0){
						i++;
						map[startRoomY][startRoomX] = next;
						startRoomY--;
					}
					break;
				case 4: // bottom exit
					if(startRoomY + 1 < mapHeight && map[startRoomY+1][startRoomX] == 0){
						i++;
						map[startRoomY][startRoomX] = next;
						startRoomY++;
					}
					else{
						run = false;
						
					}
					break;
			}
//			System.out.println();
//			System.out.println(next+" row: "+(startRoomY)+" col: "+startRoomY);
		}
		return map;
	}
	
	public void loadMap(Handler handler){
		for(int y = 0; y < mapHeight; y++){
			for(int x = 0; x < mapWidth; x++){
				System.out.println(map[y][x]);
				loadRoom(handler, y, x, map[y][x]);
			}
		}
		handler.getGameObjects().size();
	}
	
	public void loadRoom(Handler handler, int y, int x, int exit){
		int l, u, r, b;
		l = u = r = b = 0;
		int bsize = 25;
		for(int i = 0; i < chunkSize; i++){
			switch(exit){
			case 1 : // left exit
				l = 1;
				break;
			case 2 :
				r = 1;
				break;
			case 3 :
				u = 1;
				break;
			case 4 :
				b = 1;
				break;
			}
			
			if((i == 0 && l == 0) || (i == chunkSize-1 && r == 0)){
				for(int j = 0; j < chunkSize; j++){
					handler.addObject(new Platform(handler.game, i*bsize + (x*chunkSize*bsize),j*bsize+ (y*chunkSize*bsize),bsize, bsize, ID.Platform, false));
				}
			}else{
				if(u == 0)
					handler.addObject(new Platform(handler.game, i*bsize + (x*chunkSize*bsize),0*bsize+ (y*chunkSize*bsize),bsize, bsize, ID.Platform, false));
				if(b == 0)
					handler.addObject(new Platform(handler.game, i*bsize + (x*chunkSize*bsize),(chunkSize-1)*bsize+ (y*chunkSize*bsize),bsize, bsize, ID.Platform, false));
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
