package physics;

public final class Config 
{
	/**
	 * The force of gravity in this simulation. 
	 * Default 9.81m/s^2
	 */
	public static double gravity = 9.81;
	
	/**
	 * The length of time that a single tick takes, in milliseconds.
	 * Default 1ms. 
	 */
	public static double tickSize = 1;
	
	/**
	 * Whether or not the value of theta in a simulation should 
	 * remain between 0 and 2pi. 
	 * Default false.
	 */
	public static final boolean constrainTheta = false;
	
	/**
	 * The framerate of the simulation. Hardcoded to 40fps.
	 */
	public static final double FPS = 40;
	
	/**
	 * How often we should add the data we have collected so far to the output file.
	 * Default 1 (Every tick.)
	 */
	public static final int ticksPerWrite = 1;
	
	/**
	 * Whether we should even bother rendering the sim, or just rush through the math. 
	 * Default true.
	 */
	public static final boolean renderSim = true;
	
	/**
	 * The amount of ticks the sim will run before halting.
	 * Default 10000 (10 seconds real time).
	 */
	public static final long maxTicks = 10000;
}
