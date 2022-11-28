package math;

import physics.Pendulum.ODE;

public abstract class AbstractSolverMethod {
	
	ODE ode;
	
	public AbstractSolverMethod(ODE _ode)
	{
		ode = _ode;
	}
	
	public abstract void step(double stepSize);
}
