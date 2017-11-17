package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import car.Car;
import engine.ClientConsole;
import engine.Time;
import map.Map;

public class Client {
	public Map map;
	public Car car;
	public ByteBuffer converter;
	public DatagramPacket data;
	public DatagramSocket server;
	public InetAddress address;
	public ClientConsole console;
	public int port;
	public boolean running = true;
	
	public Client(String address, int port, int x, int y, int tx, int ty) {
		byte[] bytes = new byte[12];
		converter = ByteBuffer.wrap(bytes);
		data = new DatagramPacket(bytes, 12);
		try { 
			this.address = InetAddress.getByName(address);
			this.port = port;
			server = new DatagramSocket(0); server.setSoTimeout(0);
			car = new Car(x, y, tx, ty);
		} catch (Exception e) {e.printStackTrace(); System.exit(0);}
		this.map = new Map();
		
	}
	
	public static Client createClient(String [] args) {
		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		String address = null;
		int port = 0, x = 0, y = 0;
		try {
		if(args.length >= 2) {
			address = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			System.out.print("Input Server Address: ");
			address = k.readLine();
			System.out.print("Input Server Port: ");
			port = Integer.parseInt(k.readLine());
		}
		
		if(args.length >= 4) {
			x = Integer.parseInt(args[2]);
			y = Integer.parseInt(args[3]);
		} else {
			System.out.print("Input Start Location X: ");
			x = Integer.parseInt(k.readLine());
			System.out.print("Input Start Location Y: ");
			y = Integer.parseInt(k.readLine());
		}
		}catch(Exception e) {}
		
		Client client = new Client(address, port, x, y, x, y);
		if(args.length >= 6) {
			client.car.tx = Integer.parseInt(args[4]);
			client.car.ty = Integer.parseInt(args[5]);
		}
		
		client.console = new ClientConsole(client, k);
		client.console.start();
		
		return client;
	}
	
	static int framesPerSecond = 5;
	public static void main(String [] args) {
		Client client = createClient(args);
		
		float frameTime = 1.0f / framesPerSecond;
		float delta, unprocessedTime = 0f, frameCounter = 0f;
		
		client.sendData();
		client.recieve();
		
		try {
		while(client.running) {
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
				if(client.car.arrived() && client.car.x != client.car.tx && client.car.y != client.car.ty) {
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
	
	public void disconnect() {send(-1, -car.id-1);};
	
	public void recieve() {
		try {
			server.receive(data);
			car.id = converter.getInt(0);
			car.path(map, converter.getInt(8));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendData() {send(getLoc(), getTarget());}
	public void send(int x, int tx) {
		try {
		data.setAddress(address); data.setPort(port);
		converter.clear();
		converter.putInt(car.id);
		converter.putInt(x);
		converter.putInt(tx);
		converter.flip();
		server.send(data);
		} catch(IOException e) {e.printStackTrace();}
	}
	
	public int getLoc() {return map.hash(car.x, car.y);}
	public int getTarget() {return map.hash(car.tx, car.ty);}
}
