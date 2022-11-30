package math;

import physics.AbstractODE;

/**
 * Half of an iterative equation solver. The other half (sim delta) is provided
 * by whatever simulation we are using. <br>
 * <br>
 * IMPORTANT: This solver is NOT mathematically stable, and will quickly break
 * any simulation it is used in. Use {@link math.ModifiedEulersMethod} instead.
 * @author AD417
 *
 */
public class EulersMethod extends AbstractSolverMethod {

	public EulersMethod(AbstractODE _ode) {
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

		ode.setVars(simState);
	}
}
