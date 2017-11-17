package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import map.Map;
import structures.AdminTable;
import structures.ClientData;
import structures.ClientTable;

public class Server {
	public static Map map;
	public static ByteBuffer converter;
	public static DatagramPacket data;
	public static DatagramSocket server;
	public static ClientTable client_table;
	public static AdminTable admin_table;
	
	public static void main(String [] args) {
		byte[] bytes = new byte[12];
		converter = ByteBuffer.wrap(bytes);
		data = new DatagramPacket(bytes, 12);
		try {
			int port = 0;
			if(args.length > 0) port = Integer.parseInt(args[0]);
			server = new DatagramSocket(port); server.setSoTimeout(10000);
			System.out.println("Listening on port: " +  server.getLocalPort());
		} catch (Exception e) {e.printStackTrace(); System.exit(0);}
		map = new Map();
		client_table = new ClientTable();
		admin_table = new AdminTable();
		
		while(true) {
			try {
				server.receive(data);
				
				int id = converter.getInt(0);
				int loc = converter.getInt(4);
				int tgt = converter.getInt(8);
				
				if(loc < 0) { 	//If its an Admin
					if(id < 0) {
						id = admin_table.createAdmin(data.getAddress(), data.getPort());
						converter.putInt(0, id);
					}
					if(tgt < 0) {
						client_table.destroy(-tgt-1);
						converter.putInt(4, 0);
						sendToAllAdmins();
					}
					else switch(tgt) {
					case 0: //Sync
						syncAdmin(id);
						break;
					case 1: //Disconnect
						admin_table.destroy(id);
						break;
					}
					
				} else { 		//If its a Client
					if(id < 0) {
						id = client_table.createClient(data.getAddress(), data.getPort(), 0, 0);
						converter.putInt(0, id);
					}
					ClientData client = client_table.get(id);
					
					client.setLoc(converter.get(4), converter.getInt(8));
					
					sendBackAndToAdmins();
				}
				
			} catch(Exception e) {
				
			}
		}
	}
	
	public static void sendBack() {
		try {server.send(data);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void sendBackAndToAdmins() {
		try {
			server.send(data);
			sendToAllAdmins();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void sendToClientAndAdmins(int id) {
		try {
			data.setAddress(client_table.table[id].address);
			data.setPort(client_table.table[id].port);
			server.send(data);
			sendToAllAdmins();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void sendToClient(int id) {
		try {
			data.setAddress(admin_table.table[id].address);
			data.setPort(admin_table.table[id].port);
			server.send(data);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void syncAdmin(int id) {
		try {
			data.setAddress(admin_table.table[id].address);
			data.setPort(admin_table.table[id].port);
			server.send(data);
			for(int i = 0; i < client_table.next; i++) {
				if(client_table.table[i].id < 0) continue;
				pack(i, client_table.table[i].loc, client_table.table[i].tgt);
				server.send(data);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void sendToAllAdmins() {
		try {
			for(int i = 0; i < admin_table.next; i++) {
				if(admin_table.table[i].id < 0) continue;
				data.setAddress(admin_table.table[i].address);
				data.setPort(admin_table.table[i].port);
				server.send(data);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void packClient(int id) {pack(id, client_table.table[id].loc, client_table.table[id].tgt);}
	public static void pack(int id, int loc, int tgt) {
		converter.clear();
		converter.putInt(id);
		converter.putInt(loc);
		converter.putInt(tgt);
		converter.flip();
	}
}
