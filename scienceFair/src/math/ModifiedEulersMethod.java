package math;

import physics.AbstractODE;

/**
 * Half of an iterative equation solver. The other half (sim delta) is provided
 * by whatever simulation we are using. <br>
 * <br>
 * A modification of the normal Euler's Method that helps the stability
 * of the simulation over long periods of time. 
 * @author AD417
 *
 */
public class ModifiedEulersMethod extends AbstractSolverMethod {

	public ModifiedEulersMethod(AbstractODE _ode)
	{
		super(_ode);
	}
	
	@Override
	public void step(double stepSize) 
	{
		// Get the current state of the simulation, and copy it into simState.
		double[] vars = ode.getVars();
		final int len = vars.length; // NullPointerException	
		
		final double[] simState = new double[len];
		for (int i = 0; i < len; i++) simState[i] = vars[i];
		
		final double[] k1 = ode.evaluateChange(simState, 0);
		
		for (int i = 0; i < len; i++) simState[i] += k1[i] * stepSize;
		
		final double[] k2 = ode.evaluateChange(simState, stepSize);
		
		for (int i = 0; i < len; i++) 
			simState[i] = vars[i] + (0.5 * (k1[i] + k2[i]) * stepSize);
		
		ode.setVars(simState);
	}
	
}
