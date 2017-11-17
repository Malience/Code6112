package engine;

import java.io.BufferedReader;

import networking.Client;

/**
 * Admin Console
 * Allows user input through the command console
 */
public class ClientConsole extends Thread{
	
	Client client;
	public volatile boolean running = true;
	BufferedReader k; public ClientConsole(Client c, BufferedReader k) {client = c; this.k = k;}
	//Scanner k; public AdminConsole(Scanner k) {this.k = k;}
	
	//Run the command line
	public void run() {
		while(running) {
			try {
				System.out.print(": ");
//				while(!k.ready()) {
//					Thread.sleep(100);
//					if(!running) {
//						dispose();
//						return;
//					}
//				}
				String line = k.readLine().trim().toLowerCase();
				String[] splits = line.split(" ");
				switch(splits[0]) {
				case "?":
				case "help":
					System.out.println("print -> Prints information about this clients car");
					System.out.println("target [X] [Y] -> Sets a new target");
					System.out.println("exit -> Exits the program");
					break;
				case "print":
					System.out.println(client.car);
					break;
				case "target":
					try {
						int x = Integer.parseInt(splits[1]); 
						int y = Integer.parseInt(splits[2]);
						if(client.map.getTile(x, y)) {
							client.car.tx = x;
							client.car.ty = y;
							System.out.println("Target - " + "X: " + x + " Y: " + y);
							client.sendData();
							client.recieve();
						}
						else System.out.println("Invalid Map Coordinates!");
					}catch(Exception e) {System.err.println("Invalid arguments!");}
					break;
				case "exit":
					client.disconnect();
					System.err.println("Closing Program!");
					running = false;
					client.running = false;
					return;
				default:
					System.out.println("Unknown command. Try ? or help");
				}
			}catch(Exception e) {}
		}
		dispose();
	}
	
	public void dispose() {try {
		k.close();
	} catch (Exception e) {
		e.printStackTrace();
	}}
}
