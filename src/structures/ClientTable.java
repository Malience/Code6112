package structures;
import java.net.InetAddress;

public class ClientTable {
	private static final int DEFAULT_SIZE = 5;
	private static final int DEFAULT_EXPANSION_SIZE = 5;
	
	public ClientData[] table;
	private IntStack free_list;
	private int expansion_size;
	public volatile int next = 0;
	
	public ClientTable() {this(DEFAULT_SIZE, DEFAULT_EXPANSION_SIZE);}
	public ClientTable(int size) {this(size, DEFAULT_EXPANSION_SIZE);}
	public ClientTable(int size, int expansion_size) {table = new ClientData[size]; free_list = new IntStack(size, expansion_size); this.expansion_size = expansion_size;}
	
	public int createClient(InetAddress address, int port, int loc, int tgt) {
		if(!free_list.isEmpty()) {
			int free = free_list.pop();
			
			table[free].setData(address, port, free, loc, tgt);
			return free;
		}
		if(next >= table.length) expand();
		table[next] = new ClientData(address, port, next, loc, tgt);
		return next++;
	}
	
	public ClientData get(int id) {return table[id].id < 0 ? null : table[id];}
	public void destroy(int id) {if(id >= table.length || table[id] == null) return; table[id].id = -1; free_list.push(id);}
	
	public void setExpansionSize(int expansion_size) {this.expansion_size = expansion_size;}
	private void expand() {expand(expansion_size);}
	private void expand(int amount) {ClientData[] copy = new ClientData[table.length + amount];System.arraycopy(table, 0, copy, 0, table.length); table = copy;}
}