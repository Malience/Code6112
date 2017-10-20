package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import math.Vector2i;

public class NetworkListener {
	DatagramSocket server;
	byte[] bytes;
	DatagramPacket data;
	ByteBuffer converter;
	static final int PACKET_SIZE = 10;
	static final int timeout = 1000;
	public static boolean run = true;
	private static boolean[] hasChanged;
	
	public NetworkListener(int size, DatagramSocket server){
		hasChanged = new boolean[size];
		bytes = new byte[PACKET_SIZE];
		converter = ByteBuffer.wrap(bytes);
		data = new DatagramPacket(bytes, PACKET_SIZE);
		this.server = server;
		try {
			server.setSoTimeout(timeout);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void pack(byte id, byte state, Vector2i pos) {pack(id, state, pos.x, pos.y);}
	public void pack(byte id, byte state, int x, int y) {
		converter.put(id);
		converter.put(state);
		converter.putInt(x);
		converter.putInt(y);
	}
	
	
	
}
