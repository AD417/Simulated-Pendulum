package math;

import physics.AbstractODE;

public class RungeKutta extends AbstractSolverMethod {
	
	public RungeKutta(AbstractODE _ode) {
		super(_ode);
	}
	
	public void step(double stepSize)
	{
		// Get the current state of the simulation, and copy it into simState.
		double[] vars = ode.getVars();
		final int len = vars.length; // NullPointerException	
		
		final double[] simState = new double[len];
		for (int i = 0; i < len; i++) simState[i] = vars[i];
		
		final double[] k1 = ode.evaluateChange(simState, 0);
		
		for (int i = 0; i < len; i++) 
			simState[i] = vars[i] + k1[i] * stepSize/2;
		
		final double[] k2 = ode.evaluateChange(simState, stepSize * 0.5);
		
		for (int i = 0; i < len; i++) 
			simState[i] = vars[i] + k2[i] * stepSize/2;
		
		final double[] k3 = ode.evaluateChange(simState, stepSize * 0.5);

		for (int i = 0; i < len; i++) 
			simState[i] = vars[i] + k3[i] * stepSize;
		
		final double[] k4 = ode.evaluateChange(simState, stepSize);
		
		for (int i = 0; i < len; i++)
			simState[i] = vars[i] + (k1[i] + 2 * k2[i] + 2 * k3[i] + k4[i]) * stepSize / 6;
		
		// System.out.println(k1[1]);
		// System.out.println(k2[1]);
		// System.out.println(k3[1]);
		// System.out.println(k4[1]);
		// System.out.println();
		ode.setVars(simState);
	}
}
