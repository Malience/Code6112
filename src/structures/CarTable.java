package structures;

import car.Car;
import map.Map;

public class CarTable {
	private static final int DEFAULT_SIZE = 5;
	private static final int DEFAULT_EXPANSION_SIZE = 5;
	
	public volatile Car[] table;
	
	public CarTable() {this(DEFAULT_SIZE, DEFAULT_EXPANSION_SIZE);}
	public CarTable(int size) {this(size, DEFAULT_EXPANSION_SIZE);}
	public CarTable(int size, int expansion_size) {table = new Car[size];}
	
	public void createCar(int id, Map map, int loc, int tgt) {
		if(id >= table.length) expand(table.length - id + 5);
		if(table[id] == null) table[id] = new Car(id, map, loc, tgt);
		else {
			table[id].id = id;
			table[id].set(map, loc, tgt);
		}
	}
	
	public Car get(int id) {return table[id].id < 0 ? null : table[id];}
	public void destroy(int id) {if(id >= table.length || table[id] == null) return; table[id].id = -1;}
	
	
	public boolean contains(int id) {if(id >= table.length) return false; return table[id] != null && table[id].id >= 0;}
	private void expand(int amount) {Car[] copy = new Car[table.length + amount];System.arraycopy(table, 0, copy, 0, table.length); table = copy;}
}