package map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Map {
	private static final String mapFile = "./res/map.map";
	
	static final char ROAD = 'R', BUILDING = 'B', CAR = 'C', DEPOT = 'D';
	
	public char[] map;
	public int rowLength, colLength;
	
	public Map() {this(mapFile);}
	public Map(String mapFile) {
		try { 
			Scanner k = new Scanner(new File(mapFile));
			String line = k.nextLine().trim().replaceAll("\\s","");
			rowLength = line.length();
			map = new char[rowLength];
			while(true){
				for(int j = 0; j < rowLength; j++) map[colLength * rowLength + j] = line.charAt(j); //THOSE ADVANCED PROGRAMMING STRATS THO
				colLength++;
				if(!k.hasNextLine()) break;
				line = k.nextLine().trim().replaceAll("\\s","");
				expand();
			}
			k.close();
		} catch (FileNotFoundException e) {System.out.println("Map not found!");}
		
	}
	public boolean getTile(int x, int y) {return x < rowLength && x >= 0 && y < colLength && y >= 0 && map[x + y * rowLength] == ROAD;}
	public int hash(int x, int y) {return x + y * rowLength;}
	private void expand() {char[] copy = new char[map.length + rowLength];System.arraycopy(map, 0, copy, 0, map.length); map = copy;}
	
	public int getRandomRoad() {
		int start = new Random().nextInt(map.length);
		int rand = start;
		while(rand < map.length && map[rand] != ROAD) rand++;
		if(map[rand] != ROAD) {
			rand = start;
			while(rand >= 0 && map[rand] != ROAD) rand--;
		}
		if(map[rand] != ROAD) {
			System.err.println("This map seriously doesn't have a single road!?");
			System.exit(0);
			return -1;
		}
		return rand;
	}
	
	public String toString() {
		String out = "";
		for(int i = 0; i < colLength; i++) {
			for(int j = 0; j < rowLength ; j++) out += map[i * rowLength + j]; out += "\n";
		}return out;
	}
}