package math;

import physics.Pendulum.ODE;

public class EulersMethod extends AbstractSolverMethod {

	public EulersMethod(ODE _ode)
	{
		super(_ode);
	}
	
	@Override
	public void step(double stepSize) {
		// Get the current state of the simulation, and copy it into simState.
		ode.updateVars();
		final int len = ode.vars.length;
		
		final double[] simState = new double[len];
		for (int i = 0; i < len; i++) simState[i] = ode.vars[i];
		
		final double[] k1 = ode.evaluateChange(simState, 0);
		
		for (int i = 0; i < len; i++) simState[i] += k1[i] * stepSize;
		
		final double[] k2 = ode.evaluateChange(simState, stepSize);
		
		for (int i = 0; i < len; i++) 
			simState[i] = ode.vars[i] + (0.5 * (k1[i] + k2[i]) * stepSize);
		
		ode.setVars(simState);
	}
	
}
