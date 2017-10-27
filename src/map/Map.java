package map;
import java.io.File;
import car.Car;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

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
	
	static int id;
	
	public static Car instantiateCar() {
		Vector2i v = roads.remove(new Random().nextInt(roads.size()));
		map[v.x][v.y] = CAR;
		return new Car("Car " + id++, v, v);
	}
	
	public static Vector2i getTarget(Vector2i oldTarget) {
		Vector2i v = roads.remove(new Random().nextInt(roads.size()));
		roads.add(oldTarget);
		return v;
	}
	
	public static void move(Vector2i from, Vector2i to) {
		map[from.x][from.y] = ROAD;
		map[to.x][to.y] = CAR;
	}
	
	public static ArrayList<Vector2i> path(Vector2i from, Vector2i to){
		ArrayList<Vector2i> currentPath = new ArrayList<Vector2i>();
		Stack<Vector2i> buffer = new Stack<Vector2i>();
		buffer.add(from);
		
		while(!buffer.isEmpty()) {
			Vector2i current = buffer.pop();
			currentPath.add(current);
			
			if(current.equals(to)) break;
			//Expand
			if(current.x + 1 < map.length 
					&& map[current.x + 1][current.y] == ROAD 
					&& contains(currentPath, new Vector2i(current.x + 1, current.y))) 
				buffer.push(new Vector2i(current.x + 1, current.y));
			if(current.x > 0 
					&& map[current.x - 1][current.y] == ROAD
					&& contains(currentPath, new Vector2i(current.x - 1, current.y))) 
				buffer.push(new Vector2i(current.x - 1, current.y));
			if(current.y + 1 < map[0].length 
					&& map[current.x][current.y + 1] == ROAD
					&& contains(currentPath, new Vector2i(current.x, current.y + 1))) 
				buffer.push(new Vector2i(current.x, current.y + 1));
			if(current.y > 0 
					&& map[current.x][current.y - 1] == ROAD
					&& contains(currentPath, new Vector2i(current.x, current.y - 1))) 
				buffer.push(new Vector2i(current.x, current.y - 1));
			
		}
		
		return currentPath;
	}
	
	
	public static boolean contains(ArrayList<Vector2i> buffer, Vector2i v) {
		for(Vector2i b : buffer) {
			if(b.equals(v)) return false;
		}
		return true;
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
