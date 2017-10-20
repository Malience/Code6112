package engine;

import map.Map;

public class CoreEngine {
	boolean running = false;
	int framesPerSecond = 60;
	
	
	public void start() {
		running = true;
		this.run();
		this.dispose();
	}
	
	public void stop() {
		running = false;
	}
	
	public void run() {
		Time.init();
		int frames = 0;
		float frameTime = 1.0f / framesPerSecond;
		float delta;
		float unprocessedTime = 0f;
		float frameCounter = 0f;
		
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		Map.instantiateCar();
		
		System.out.print(Map.printMap());
		
		while(running) {
			//frame start
			
			boolean render = false;
			
			delta = Time.updateDelta();
			
			unprocessedTime += delta;
			frameCounter += delta;
			
			//frame end
			while(unprocessedTime >= frameTime)
			{
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(frameCounter >= 1.0)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}	
			}
			if(render){
				frames++;}
			else{try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		}
	}
	
	public void dispose() {
	}
	
	public static void main(String [] args) {
		CoreEngine engine = new CoreEngine();
		engine.start();
	}
}
