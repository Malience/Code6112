package networking;

import java.util.Scanner;

import engine.Time;

public class SimulatedClient extends Client{

	public SimulatedClient(String address, int port, int x, int y, int tx, int ty) {
		super(address, port, x, y, tx, ty);
	}
	
	public void setTarget() {
		int tgt = this.map.getRandomRoad();
		this.car.tx = tgt % super.map.rowLength;
		this.car.ty = tgt / super.map.rowLength;
	}
	
	public static SimulatedClient createSimulatedClient(String [] args) {
		Scanner k = null;
		String address = null;
		int port = 0;
		if(args.length >= 2) {
			address = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			k = new Scanner(System.in);
			System.out.print("Input Server Address: ");
			address = k.nextLine();
			System.out.print("Input Server Port: ");
			port = Integer.parseInt(k.nextLine());
		}
		SimulatedClient client = new SimulatedClient(address, port, 0, 0, 0, 0);
		if(args.length >= 4) {
			client.car.x = Integer.parseInt(args[2]);
			client.car.y = Integer.parseInt(args[3]);
		} else {
			int tgt = client.map.getRandomRoad();
			client.car.x = tgt % client.map.rowLength;
			client.car.y = tgt / client.map.rowLength;
		}
		
		if(args.length >= 6) {
			client.car.tx = Integer.parseInt(args[4]);
			client.car.ty = Integer.parseInt(args[5]);
		} else {
			client.setTarget();
		}
		
		return client;
	}
	
	public static void main(String [] args) {
		SimulatedClient client = createSimulatedClient(args);
		
		float frameTime = 1.0f / framesPerSecond;
		float delta, unprocessedTime = 0f, frameCounter = 0f;
		boolean running = true;
		
		try {
		while(running) {
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
				if(client.car.arrived()) {
					if(client.car.x == client.car.tx && client.car.y == client.car.ty) {
						client.setTarget();
						if(client.car.tx < 0 || client.car.ty < 0) {
							running = false;
							break;
						}
					}
					client.sendData();
					client.recieve();
				}
				client.car.move();
			}
			else{try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		}
		
		client.disconnect();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
