package car;
import java.util.PriorityQueue;

import map.Map;

public class Car {
	public int id = -1, x, y, tx = -1, ty = -1;
	private Path path;
	
	public Car() {}
	public Car(int x, int y, int tx, int ty) {
		this.x = x; this.y = y; this.tx = tx; this.ty = ty;
	}
	
	public Car(int id, Map map, int x, int tx) {
		this.id = id;
		this.x = x % map.rowLength; this.y = x / map.rowLength;
		this.tx = tx % map.rowLength; this.ty = tx / map.rowLength;
	}
	
	public void set(Map map, int x, int tx) {
		this.x = x % map.rowLength; this.y = x / map.rowLength;
		this.tx = tx % map.rowLength; this.ty = tx / map.rowLength;
	}
	
	public boolean arrived() {return path == null || path.next == null;}
	
	public void move() {if(path != null && path.next != null) {x = path.next.x; y = path.next.y; path.next();}}
	
	public void path(Map map, int to) {path(map, to % map.rowLength, to / map.rowLength);}
	public void path(Map map, int toX, int toY) {this.path = path(map, this.x, this.y, toX, toY);}
	public static Path path(Map map, int x, int y, int toX, int toY){
		PriorityQueue<Node> heap = new PriorityQueue<Node>(10);
		Node[][] nodeMap = new Node[map.rowLength][map.colLength];
		nodeMap[x][y] = new Node(null, x, y, 0);
		heap.add(nodeMap[x][y]);
		
		Node current = null;
		while(!heap.isEmpty()) {
			current = heap.poll();
			
			if(current.x == toX && current.y == toY) break;
			
			//Expand
			int indexx = current.x + 1, indexy = current.y;
			if(map.getTile(indexx, indexy)) {
				int cost = current.cost + manhattanDistance(indexx, indexy, toX, toY);
				if(nodeMap[indexx][indexy] != null) {
					if(nodeMap[indexx][indexy].cost > cost) heap.add(nodeMap[indexx][indexy].update(current, cost));
				} else {
					nodeMap[indexx][indexy] = new Node(current, indexx, indexy, cost);
					heap.add(nodeMap[indexx][indexy]);
				}
			}
			indexx = current.x - 1;
			if(map.getTile(indexx, indexy)) {
				int cost = current.cost + manhattanDistance(indexx, indexy, toX, toY);
				if(nodeMap[indexx][indexy] != null) {
					if(nodeMap[indexx][indexy].cost > cost) heap.add(nodeMap[indexx][indexy].update(current, cost));
				} else {
					nodeMap[indexx][indexy] = new Node(current, indexx, indexy, cost);
					heap.add(nodeMap[indexx][indexy]);
				}
			}
			indexx = current.x; indexy = current.y + 1;
			if(map.getTile(indexx, indexy)) {
				int cost = current.cost + manhattanDistance(indexx, indexy, toX, toY);
				if(nodeMap[indexx][indexy] != null) {
					if(nodeMap[indexx][indexy].cost > cost) heap.add(nodeMap[indexx][indexy].update(current, cost));
				} else {
					nodeMap[indexx][indexy] = new Node(current, indexx, indexy, cost);
					heap.add(nodeMap[indexx][indexy]);
				}
			}
			indexy = current.y - 1;
			if(map.getTile(indexx, indexy)) {
				int cost = current.cost + manhattanDistance(indexx, indexy, toX, toY);
				if(nodeMap[indexx][indexy] != null) {
					if(nodeMap[indexx][indexy].cost > cost) heap.add(nodeMap[indexx][indexy].update(current, cost));
				} else {
					nodeMap[indexx][indexy] = new Node(current, indexx, indexy, cost);
					heap.add(nodeMap[indexx][indexy]);
				}
			}	
		}
		return new Path(current);
	}
	
	public static int manhattanDistance(int x0, int y0, int x1, int y1) {return Math.abs(x1 - x0) + Math.abs(y1 - y0);}
	
	public String toString() {return "Car " + id + " - " + "X: " + x + " Y: " + y  + " Target_X: " + tx + " Target_Y: " + ty; }
	
	static class Path{
		public Node next;
		Path(Node n){
			while(n.prev != null) {n.prev.next = n;n = n.prev;}
			next = n;
		}
		public void next() {if(next.next == null) {next = null; return;} next = next.next; next.prev = null;}
		
		public String toString() {
			String s = "";
			Node n = next;
			do{s += n + " -> "; n = n.next;}while(n.next != null);
			return s + n;
		}
	}
	
	static class Node implements Comparable<Node>{
		Node next, prev; int x, y, cost;
		public Node(Node prev, int x, int y, int cost) {this.prev = prev; this.x = x; this.y = y; this.cost = cost;}
		public Node update(Node n, int cost) {this.prev = n; this.cost = cost; return this;}
		public int compareTo(Node n) {return this.cost > n.cost ? 1 : (this.cost == n.cost ? 0 : -1);}
		public String toString() {return "X: " + x + " Y: " + y;}
	}
}
