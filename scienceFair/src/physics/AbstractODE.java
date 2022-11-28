package physics;

/**
 * Half of a differential equation solver. The other half can be 
 * changed as needed while the simulation runs. <br>
 * <br>
 * Collects data points relevant for the equation solving in one
 * place, and allows for their manipulation. 
 * @author AD417<br>
 * Based on code written by myphysicslab.
 *
 */
public abstract class AbstractODE {
	
	
	public AbstractODE() {}
	
	/**
	 * Get the important variables in this simulation. 
	 * @return an array containing a number of values from a sim.
	 */
	public abstract double[] getVars();
	
	/**
	 * Determine the change in the state of the simulation, given the 
	 * provided current state. <br>
	 * <br>
	 * Note that said current state might not
	 * be the current state as stored in {@link vars}. <br>
	 * @param current the state of the simulation to evaluate.
	 * @param timeStep an indicator of how much time has passed 
	 * since the true current state.
	 * @return the change in the state of the provided simulation.
	 */
	public abstract double[] evaluateChange(double[] current, double timeStep);
	
	/**
	 * Sync the simulation to the new state calculated by the second half of 
	 * the solver. 
	 * @param _vars the new state of the sim. 
	 */
	public abstract void setVars(double[] _vars);
	
}
