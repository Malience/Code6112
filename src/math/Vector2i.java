package math;

public class Vector2i {
	public Vector2i(int x, int y) {
		this.x = x; this.y = y;
	}

	public int x, y;
	
	
	public boolean equals(Vector2i v) {
		return v.x == this.x && v.y == this.y;
	}
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
}
