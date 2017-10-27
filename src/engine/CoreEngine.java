package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.system.MemoryUtil.NULL;

import car.Car;
import map.Map;

public class CoreEngine {
	RenderingEngine renderer;
	
	boolean running = false;
	int framesPerSecond = 10;
	long window_handle;
	
	public void start() {
		glfwInit();
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		
		window_handle = glfwCreateWindow(800, 600, "6112 Project", NULL, NULL);
		
		glfwMakeContextCurrent(window_handle);
		glfwSwapInterval(1);		
		
		glfwShowWindow(window_handle);
		
		renderer = new RenderingEngine(window_handle);
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
		
		Car[] cars = cargen(20);
		
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
//		Map.instantiateCar();
		
		//Map.instantiateCar();
		
		while(running && !glfwWindowShouldClose(window_handle)) {
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
				
				glfwPollEvents();
				
				if(frameCounter >= 1.0)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}	
			}
			if(render){
				frames++;
				for(Car c : cars) {
					c.run();
				}
				//System.out.println(Map.printMap());
				renderer.run();
			}
			else{try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		}
	}
	
	public Car[] cargen(int num) {
		Car[] out = new Car[num];
		for(int i = 0; i < num; i++) {
			out[i] = Map.instantiateCar();
			out[i].getTarget();
		}
		return out;
	}
	
	public void dispose() {
		renderer.dispose();
		System.out.println("Disposing of Window!");
		glfwDestroyWindow(window_handle);
		glfwTerminate();
	}
	
	public static void main(String [] args) {
		CoreEngine engine = new CoreEngine();
		engine.start();
	}
}
