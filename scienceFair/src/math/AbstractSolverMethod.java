package math;

import physics.AbstractODE;

public abstract class AbstractSolverMethod {
	
	AbstractODE ode;
	
	public AbstractSolverMethod(AbstractODE _ode)
	{
		ode = _ode;
	}
	
	public abstract void step(double stepSize);
}
