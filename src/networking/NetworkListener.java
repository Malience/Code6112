package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import map.Map;

public class NetworkListener extends Thread{
	int id = -1;
	DatagramSocket server;
	DatagramPacket data;
	ByteBuffer converter;
	public InetAddress address;
	public int port;
	Map map;
	public boolean run = true;
	
	public NetworkListener(Map map, String address, int port){
		byte[] bytes = new byte[12];
		converter = ByteBuffer.wrap(bytes);
		data = new DatagramPacket(bytes, 12);
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.server = new DatagramSocket(0); this.server.setSoTimeout(100);
		} catch (Exception e) {e.printStackTrace(); System.exit(0);}
		this.map = map;
	}
	
	public void run(){
		try{
		while(run){
			try{
				server.receive(data);
				int id = converter.getInt(0);
				int loc = converter.getInt(4);
				int tgt = converter.getInt(8);
				if(loc < 0) this.id = id;
				else {
					if(tgt < 0) {
						Admin.car_table.destroy(-tgt-1);
						continue;
					}
					if(Admin.frozen) continue;
					if(!Admin.car_table.contains(id)) {
						Admin.car_table.createCar(id, map, loc, tgt);
					} else Admin.car_table.get(id).set(map, loc, tgt);
					Admin.car_table.get(id).path(map, tgt);
				}
				
				
			} catch(SocketTimeoutException e){}
		}
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Network closing!");
	}
	
	public void disconnect() {send(1);}
	
	public void sync() {send(0);}
	public void send(int netcode) {
		try {
		data.setAddress(address); data.setPort(port);
		converter.clear();
		converter.putInt(id);
		converter.putInt(-1);
		converter.putInt(netcode);
		converter.flip();
		server.send(data);
		} catch(IOException e) {e.printStackTrace();}
	}
	
	public void dispose() {run = false;}
	
}
