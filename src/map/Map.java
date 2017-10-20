package map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import math.Vector2i;

public class Map {
	private static final String mapFile = "./res/map.map";
	
	static final byte ROAD = 0x1;
	static final byte BUILDING = 0x0;
	static final byte CAR = 0x2;
	static final byte DEPOT = 0x3;
	
	public static byte[][] map = new byte[10][10];
	static ArrayList<Vector2i> roads = new ArrayList<Vector2i>();
	
	static {
		try { 
			Scanner k = new Scanner(new File(mapFile));
			int i = 0;
			while(k.hasNextLine()){
				String line = k.nextLine().trim().replaceAll("\\s","");
				for(int j = 0; j < line.length(); j++) {
					switch(line.charAt(j)) {
					case 'R':
						map[i][j] = ROAD;
						roads.add(new Vector2i(i,j));
						break;
					case 'B':
						map[i][j] = BUILDING;
						break;
					}
				}
				i++;
			}
			k.close();
		} catch (FileNotFoundException e) {System.out.println("Map not found!");}
		
	}
	
	public static void instantiateCar() {
		Vector2i v = roads.remove(new Random().nextInt(roads.size()));
		map[v.x][v.y] = CAR;
	}
	
	
	public static String printMap() {
		String out = "";
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				switch(map[i][j]) {
				case ROAD:
					out += "R";
					break;
				case BUILDING:
					out += "B";
					break;
				case CAR:
					out += "C";
					break;
				}
			}
			out += "\n";
		}
		return out;
	}
	
	public static String printRoads() {
		String out = "";
		byte[][] road = new byte[10][10];
		
		for(Vector2i v : roads) {
			road[v.x][v.y] = ROAD;
		}
		
		for(int i = 0; i < road.length; i++) {
			for(int j = 0; j < road[0].length; j++) {
				switch(road[i][j]) {
				case ROAD:
					out += "R";
					break;
				default:
					out+="0";
				}
			}
			out += "\n";
		}
		return out;
	}
}
