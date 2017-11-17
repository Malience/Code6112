package engine;

import java.io.BufferedReader;

import networking.Admin;

/**
 * Admin Console
 * Allows user input through the command console
 */
public class AdminConsole extends Thread{
	
	
	public volatile boolean running = true;
	BufferedReader k; public AdminConsole(BufferedReader k) {this.k = k;}
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
					System.out.println("remove [ID] -> Removes the car with id [ID] from the server (Note: it may reconnect)");
					System.out.println("print [ID] -> Prints information about the car with id [ID]");
					System.out.println("printall -> Prints all cars");
					System.out.println("freeze -> Freezes/Unfreezes the simulation. Cars stop moving and new cars are rejected");
					System.out.println("sync -> Sync to the server");
					System.out.println("exit -> Exits the program");
					break;
				case "remove":
					try {
						int id = Integer.parseInt(splits[1]);
						Admin.car_table.destroy(id);
						Admin.network.send(-id-1); //The netcode for deleting is the -id
						System.out.println("Car " + id + " has been removed!");
					}catch(Exception e) {System.err.println("Invalid arguments!");}
					break;
				case "print":
					try {
						int id = Integer.parseInt(splits[1]);
						System.out.println(Admin.car_table.get(id));
					}catch(Exception e) {System.err.println("Invalid arguments!");}
					break;
				case "printall":
					for(int i = 0; i < Admin.car_table.table.length; i++) {
						if(Admin.car_table.table[i] == null || Admin.car_table.table[i].id < 0) continue;
						System.out.println(Admin.car_table.get(i));
					}
					break;
				case "freeze":
					Admin.frozen = !Admin.frozen;
					System.out.println("Simulation " + (Admin.frozen ? "frozen!" : "unfrozen!"));
					break;
				case "exit":
					System.err.println("Closing Program!");
					running = false;
					Admin.running = false;
					return;
				case "sync":
					Admin.network.sync();
					System.out.println("System Syncing...");
					break;
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
