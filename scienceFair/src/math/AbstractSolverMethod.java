package math;

import physics.AbstractODE;

/**
 * Half of a iterative equation solver. The other half (sim delta) is provided
 * by whatever simulation we are using. <br>
 * <br>
 * An abstract solver that must be inherited to provide functionality for 
 * iterating simulations. 
 * @author AD417
 *
 */
public abstract class AbstractSolverMethod {
	
	AbstractODE ode;
	
	public AbstractSolverMethod(AbstractODE _ode)
	{
		ode = _ode;
	}
	
	public abstract void step(double stepSize);
}
