package car;

import java.util.ArrayList;
import java.util.Queue;

import map.Map;
import math.Vector2i;

public class Car {
	String name;
	Vector2i pos, target;
	ArrayList<Vector2i> path;
	
	public Car(String name, Vector2i pos, Vector2i target) {this.name = name; this.pos = pos; this.target = target;}
	
	public void run() {
		if(path == null || path.isEmpty()) {
			getTarget();
			path = Map.path(pos, target);
		}
		Vector2i newpos = path.remove(0);
		Map.move(pos, newpos);
		pos = newpos;
	}
	
	public void getTarget() {target = Map.getTarget(target);}
	public String toString() {return "Name: " + name + "\nPos: " + pos + "\nTarget: " + target;}
}
