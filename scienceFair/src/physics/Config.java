package physics;

public final class Config 
{
	/**
	 * The force of gravity in this simulation. Default 9.81m/s^2
	 */
	public static double gravity = 9.81;
	
	/**
	 * The length of time that a single tick takes. Default 1ms. 
	 */
	public static double tickSize = 1;
	
	/**
	 * Whether or not the value of theta in a simulation should 
	 * remain between 0 and 2pi. 
	 */
	public static final boolean constrainTheta = false;
	
	/**
	 * The framerate of the simulation Hardcoded to 40fps.
	 */
	public static final double FPS = 40;
}
