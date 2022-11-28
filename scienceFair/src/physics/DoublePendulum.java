package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import math.*;

/**
 * Double Pendulum Simulation. 
 * The primary simulation used in this Science Fair.<br>
 * <br>
 * Given a pendulum bob attached to a second pendulum bob and allowed
 * to swing freely as both bobs move, determine where they will be after 
 * some time. <br>
 * 
 * @author AD417
 *
 */
public class DoublePendulum {

	/**
	 * The first bob, attached to a fixed pivot. 
	 */
	Bob bob1;
	
	/**
	 * The second bob, attached to the first bob and allowed 
	 * to swing freely. 
	 */
	Bob bob2;

    /**
     * The screen the simulation is rendered on. 
     */
    JFrame frame;
    
    AbstractSolverMethod solver;

    /**
     * The number of frames that have already been drawn.
     */
    int ticks = 0;

    boolean isRendering = false;
    
    public DoublePendulum(double l1, double m1, double l2, double m2) 
    		throws Exception
    {
        bob1 = new Bob(l1, m1);
        bob2 = new Bob(l2, m2);
        bob1.setTheta(Math.PI);
        bob2.setTheta(Math.PI);
        bob2.setTheta(-0.001);

        frame = new JFrame();
        frame.add(new Render());
		frame.setSize(517, 537);
		frame.setVisible(true);
        frame.setTitle("Double Pendulum Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		solver = new ModifiedEulersMethod(new ODE());
    }
    
    /**
     * Rendering routine for the simulation.
     * This might be the worst part about this whole thing. I don't yet
     * fully understand the implications / utilities of nested classes.
     * @author AD417
     */
    class Render extends JComponent
    {

		// Not sure what this is for; Eclipse forced me to add it. 
    	private static final long serialVersionUID = 7242687811228757877L;
        public void paintComponent(Graphics g)
        {
            if (isRendering) return;
            isRendering = true;
            Graphics2D g2 = (Graphics2D) g;

            g.setColor(Color.white);
            g.fillRect(0, 0, 500, 500);
            g.setColor(Color.black);
            bob1.drawLine(g2);
            bob2.drawLine(g2);
            g.setColor(Color.red);
            bob1.drawBob(g2);
            g.setColor(Color.blue);
            bob2.drawBob(g2);
            isRendering = false;
        }
    }
    
    /**
     * Half of a differential equation solver. The other half can be 
     * changed as needed while the simulation runs. <br>
     * <br>
     * Collects data points relevant for the equation solving in one
     * place, and allows for their manipulation. 
     * @author AD417<br>
     * Based on code written by myphysicslab:
     * {@link https://github.com/myphysicslab/myphysicslab/blob/c47ed29a4d79a449e2a673e6ff788227b7a53bf0/src/sims/pendulum/DoublePendulumSim.js}
     *
     */
    public class ODE extends AbstractODE
    {
    	
    	/**
    	 * 
    	 * Compile a list containing all the important variables in this sim.
    	 * @return A container for the important variables in this simulation, 
    	 * packaged for convenient use in other parts of the code. <br>
    	 * <br>
    	 * For a Double Pendulum, they are stored as: <br>
    	 * 0. Theta1 <br>
    	 * 1. ThetaPrime1 <br>
    	 * 2. Theta2 <br>
    	 * 3. ThetaPrime2 <br>
    	 * 4. Theta''1 <br>
    	 * 5. Theta''2 <br>
    	 * 6. Kinetic Energy  <br>
    	 * 7. Potential Energy  <br>
    	 * 8. Total Energy  <br>
    	 * 9. Time  <br>
    	 */
    	@Override
    	public double[] getVars()
    	{
    		double[] vars = new double[10];
    		// Angle of bob 1
    		vars[0] = bob1.theta;
    		// Angular velocity  of bob 2
    		vars[1] = bob1.thetaPrime;
    		// Angle  of bob 1
    		vars[2] = bob2.theta;
    		// Angular velocity of bob 2
    		vars[3] = bob2.thetaPrime;

    		vars[4] = 0;
    		vars[5] = 0;
    		
    		// Kinetic Energy
    		vars[6] = bob1.getKineticEnergy() + bob2.getKineticEnergy();
    		// Potential Energy
    		vars[7] = bob1.getPotentialEnergy() + 
    				bob2.getPotentialEnergy(bob2.getY());
    		// Total Energy
    		vars[8] = vars[4] + vars[5];
    		//Time 
    		vars[9] = ticks * Config.tickSize * 0.001;
            
            return vars;
    	}
    	
    	/**
    	 * Determine the change in the state of the simulation, given the 
    	 * provided current state. <br>
    	 * <br>
    	 * Note that said current state may not
    	 * be the current state as stored in {@link vars}. <br>
    	 * To determine the change in the current state, use
    	 * {@link Pendulum.evaluateThisChange} instead. 
    	 * @param current the state of the simulation to evaluate.
    	 * @param timeStep ???
    	 * @return the change in the state of the provided simulation.
    	 */
    	@Override
    	public double[] evaluateChange(double[] current, double timeStep)
    	{
    		double[] change = new double[10];
    		
    		final double th1 = current[0];
    		final double dth1 = current[1];
    		final double th2 = current[2];
    		final double dth2 = current[3];
    		final double m1 = bob1.mass;
    		final double m2 = bob2.mass;
    		final double L1 = bob1.rodLength;
    		final double L2 = bob2.rodLength;
    		final double g = Config.gravity;
    		
    		// Theta1's Change
    		change[0] = dth1;
    		
    		// Lots of magic numbers here. 
    		double thetaPrimeChange = -g*(2*m1+m2)*Math.sin(th1);
    		thetaPrimeChange -= g*m2*Math.sin(th1-2*th2);
    		thetaPrimeChange -= 2*m2*dth2*dth2*L2*Math.sin(th1-th2);
    		thetaPrimeChange -= m2*dth1*dth1*L1*Math.sin(2*(th1-th2));
    		thetaPrimeChange /= L1*(2*m1+m2-m2*Math.cos(2*(th1-th2)));
    		// ThetaPrime1's change.
    		change[1] = thetaPrimeChange;

    		// Theta2's change
    		change[2] = dth2;
    		
    		// More magic.
    		thetaPrimeChange = (m1+m2)*dth1*dth1*L1;
    		thetaPrimeChange += g*(m1+m2)*Math.cos(th1);
    		thetaPrimeChange += m2*dth2*dth2*L2*Math.cos(th1-th2);
    		thetaPrimeChange *= 2*Math.sin(th1-th2);
    		thetaPrimeChange /= L2*(2*m1+m2-m2*Math.cos(2*(th1-th2)));
    		//ThetaPrime2's change.
    		change[3] = thetaPrimeChange;
    		
    		// Time's change -- 1-1
    		change[9] = 1;
    		
    		
    		return change;
    	}
    	
    	@Override
    	public void setVars(double[] vars)
    	{
    		// if (_vars.length != vars.length) throw new Exception("Invalid sim configuration!");
    		bob1.setTheta(vars[0]);
    		bob1.thetaPrime = vars[1];
    		bob2.setTheta(vars[2]);
    		bob2.thetaPrime = vars[3];
    		
    		bob2.setCenter(bob1.getX(), bob1.getY());
    	}
    }


    /**
     * Events that should occur on every tick to maintain the simulation.
     * @param tickTime the amount of real time (in seconds) that passes.
     */
    public void tick(double tickTime)
    {
    	solver.step(tickTime);
    }

    /**
     * Events that should occur every time the frame should be redrawn.
     */
    public void render()
    {
        frame.repaint();
    }

    /**
     * The main loop of the simulation. 
     */
    public void loop()
    {
    	// TODO: Figure out how to make the timer do non-int ticks. 
    	Timer timer = new Timer((int) Config.tickSize, new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        tick(Config.tickSize / 1000);
    	        if (++ticks % 25 == 0) {
    	        	render();
    	        }
    	    }
    	});
    	timer.start();
    }

    public static void main(String[] args) throws Exception
    {
        DoublePendulum p = new DoublePendulum(4, 1.0, 1.5, 3.0);
        p.loop();
    }
    
    @Override
    public String toString()
    {
    	String out = "DoublePendulum{Bob1: " + 
    			bob1.toString() + 
    			", Bob2: " +
    			bob2.toString() + 
    			"}";
    	return out;
    }
}
