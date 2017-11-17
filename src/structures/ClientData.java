package structures;

import java.net.InetAddress;

public class ClientData {
	public int id, port, loc, tgt;
	public InetAddress address;
	
	public ClientData(InetAddress address, int port, int id, int loc, int tgt) {
		this.address = address;
		this.port = port;
		this.id = id;
		this.loc = loc;
		this.tgt = tgt;
	}
	
	public void setData(InetAddress address, int port, int id, int loc, int tgt) {
		this.address = address;
		this.port = port;
		this.id = id;
		this.loc = loc;
		this.tgt = tgt;
	}
	
	public void setLoc(int loc, int tgt) {
		this.loc = loc;
		this.tgt = tgt;
	}
}
