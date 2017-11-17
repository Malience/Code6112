package engine;

public class Time {
	private static final float SECOND = 1000000000.0f;
	private static float last, delta;
	static{last = getTime();}
	public static float getDelta() {return delta;}
	public static float getTime(){return System.nanoTime() / SECOND;}
	
	public static float updateDelta() {
		float time = getTime();
		delta = time - last;
		last = time;
		return delta;
	}
}
