package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import Graphics.Assets;
import Input.KeyInput;
import Input.Mouse;

public class Main implements Runnable{
	
	public int Width,Height;
	Display display;
	BufferStrategy bs;
	////assets//
	public Assets asset;
	private boolean isRunning = false;
	private Thread thread;
	BufferedImage screen;
	////Handler////
	public Handler handler;
	
	
	public Mouse mouse;

	public Main(String gameName, int Width, int Height){
		this.Width=Width;
		this.Height=Height;
		display = new Display(gameName,Width,Height);
		init();
	}
	
	public void init(){
		//load assets///
		asset = new Assets();
		asset.loadgraphics();
		
		mouse = new Mouse(this);
		handler = new Handler(this);
		
//		screen = handler.filter(handler.filter(handler.filter(handler.filter(asset.background))));
		
//		screen = asset.background;
		
		//Camera//
		display.canvas.addKeyListener(new KeyInput(handler));
		display.canvas.addMouseMotionListener(mouse);
		display.canvas.addMouseListener(mouse);
	}

	public synchronized void start(){
		isRunning = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	 
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public double delta = 0;
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
//		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		stop();
		
	}

	double alpha = 10;
	boolean dec = false;
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs==null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		//////////////////////////////////
//		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, Width, Height);
//		g2d.drawImage(asset.background, 0, 0, Width, Height, null);
		//BufferedImage player = asset.playerSpriteSheet;
		//g2d.drawImage(player, 0, 0, null);
		
		////draw objects///
		handler.render(g2d);
//		g2d.drawImage(screen, 0, 0, Width, Height, null);
		
		
		
		if(handler.restart || alpha > 0){
			g2d.setColor(new Color(0,0,0,(int)alpha));
			g2d.fill(new Rectangle(0, 0, Width, Height));
			if(dec){
				alpha -= 0.6;
			}else{
				alpha += 5;
			}
			if(alpha >= 255){
				alpha = 255;
				handler.restart();
				dec = true;
			}else if(alpha <= 0){
				alpha = 0;
				dec = false;
			}
			
		}
		
		
		
		//////////////////////////////////

		g.dispose();
		bs.show();
		
	}

	long start = System.currentTimeMillis();;
	long New;
	private void tick() {
		handler.tick();
	}
	
			
}
