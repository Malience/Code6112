package engine;

public class Time 
{
	private static final double SECOND = 1000000000.0;
	private static double last;
	private static float delta;
	
	public static void init() {
		last = getTime();
	}
	
	public static float getDelta() {
		return delta;
	}
	
	public static float updateDelta() {
		double time = getTime();
		delta = (float) (time - last);
		last = time;
		return delta;
	}
	
	public static double getTime()
	{
		return System.nanoTime() / SECOND;
	}
}
