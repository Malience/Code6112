package structures;

import java.net.InetAddress;

public class AdminData {
	public int id, port;
	public InetAddress address;
	
	public AdminData(InetAddress address, int port, int id) {
		this.address = address;
		this.port = port;
		this.id = id;
	}
	
	public void setData(InetAddress address, int port, int id) {
		this.address = address;
		this.port = port;
		this.id = id;
	}
	
}
