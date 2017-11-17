package networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import engine.AdminConsole;
import engine.RenderingEngine;
import engine.Time;
import map.Map;
import structures.CarTable;

public class Admin {
	public static Map map;
	public static ByteBuffer converter;
	public static DatagramPacket data;
	public static DatagramSocket server;
	public static CarTable car_table;
	public static RenderingEngine renderer;
	public static NetworkListener network;
	public static AdminConsole console;
	public static boolean frozen = false;
	
	public static volatile boolean running = true;
	
	public static void main(String [] args) {
		
		map = new Map();
		car_table = new CarTable();
		
		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		//Scanner k = new Scanner(System.in);
		String address = null;
		int port = 0;
		if(args.length >= 2) {
			address = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			try {
			System.out.print("Input Server Address: ");
			address = k.readLine();
			System.out.print("Input Server Port: ");
			port = Integer.parseInt(k.readLine());
			}catch(Exception e) {e.printStackTrace(); System.exit(0);}
		}
		console = new AdminConsole(k);
		console.start();
		
		
		network = new NetworkListener(map, address, port);
		network.start();
		network.sync();
		renderer = new RenderingEngine();
		
		int framesPerSecond = 5;
		float frameTime = 1.0f / framesPerSecond;
		float delta, unprocessedTime = 0f, frameCounter = 0f;
		
		while(running && !renderer.windowShouldClose()) {
			//frame start
			boolean render = false;
			
			delta = Time.updateDelta();
			
			unprocessedTime += delta;
			frameCounter += delta;
			
			//frame end
			while(unprocessedTime >= frameTime) {
				render = true;
				unprocessedTime -= frameTime;
				
				
				
				if(frameCounter >= 1.0) {
					//System.out.println(frames);
					frameCounter = 0;
				}	
			}
			if(render){
				renderer.pollEvents();
				if(!frozen) moveCars();
				renderer.clear();
				renderer.renderMap(map);
				renderCars();
				renderer.swapBuffers();
			}
			else{try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		}
		
		
		network.dispose();
		network.disconnect();
		renderer.dispose();
		//CALLING IN THE NUKE!!!!!!!!!
		System.exit(0);
	}
	
	public static void moveCars() {
		for(int i = 0; i < car_table.table.length; i++) {
			if(car_table.table[i] == null || car_table.table[i].id < 0) continue;
			car_table.table[i].move();
		}
	}
	
	public static void renderCars() {
		for(int i = 0; i < car_table.table.length; i++) {
			if(car_table.table[i] == null || car_table.table[i].id < 0) continue;
			renderer.renderCar(car_table.table[i].x, car_table.table[i].y);
		}
	}
}
