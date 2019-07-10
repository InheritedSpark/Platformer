package Main;

public class start {
	public static void main(String args[]){
		//ExecutorService pool = Executors.newFixedThreadPool(2); 
		System.setProperty("sun.java2d.opengl","True");
		Main main = new Main("Shooter",1508,896);
		main.start();
		
		
		
		//pool.shutdown();
	}
	
}
