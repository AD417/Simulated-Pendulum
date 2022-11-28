package physics;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import math.*;

public class Pendulum 
{
    /**
     * Stores all information necessary for this simulation.
     */
    Bob bob;

    /**
     * The screen the simulation is rendered on. 
     */
    JFrame frame;
    
    AbstractSolverMethod solver;

    /**
     * The number of frames that have already been drawn.
     */
    int ticks = 0;


    public Pendulum(double rodLength, double mass) throws Exception
    {
        bob = new Bob(rodLength, mass);
        bob.setThetaPrime(1);

        frame = new JFrame();
        frame.add(new Render());
		frame.setSize(517, 537);
		frame.setVisible(true);
        frame.setTitle("Pendulum Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		solver = new EulersMethod(new ODE());
    }
    
    /**
     * Rendering routine for the simulation.
     * This might be the worst part about this whole thing. I don't yet
     * fully understand the implications / utilities of nested classes.
     */
    class Render extends JComponent
    {
    	// Not sure what this is for; Eclipse forced me to add it. 
        private static final long serialVersionUID = -3551534785177435875L;
		boolean isDone = true;
        public void paintComponent(Graphics g)
        {
            if (!isDone) return;
            Graphics2D g2 = (Graphics2D) g;

            g.setColor(Color.white);
            g.fillRect(0, 0, 500, 500);
            g.setColor(Color.black);
            bob.drawLine(g2);
            bob.drawBob(g2);
        }
    }
    
    public class ODE
    {
    	/**
    	 * The important variables in this simulation, 
    	 * packaged for convenient use in other parts of the code. <br>
    	 * <br>
    	 * For a pendulum, they are stored as: <br>
    	 * 0. Angle / Theta <br>
    	 * 1. Angular velocity / ThetaPrime <br>
    	 * 2. Time <br>
    	 * 3. Angular Acceleration / Theta'' <br>
    	 * 4. Kinetic Energy <br>
    	 * 5. Potential Energy <br>
    	 * 6. Total Energy  <br>
    	 */
    	public double[] vars = new double[7];
    	
    	/**
    	 * Update the state of this nested class to align with the current
    	 * state of the sim. 
    	 */
    	public void updateVars()
    	{
    		// Angle
    		vars[0] = bob.theta;
    		// Angular velocity
    		vars[1] = bob.thetaPrime;
    		// Time
    		vars[2] = ticks * Config.tickSize * 0.001;
    		// Angular Acceleration
    		vars[3] = 0;
    		// Kinetic Energy
    		vars[4] = bob.getKineticEnergy();
    		// Potential Energy
    		vars[5] = bob.getPotentialEnergy();
    		// Total Energy
    		vars[6] = vars[4] + vars[5];
    	}
    	
    	/**
    	 * Determine the change in the state of the current state of the
    	 * simulation. 
    	 * @param timeStep
    	 * @return
    	 */
    	public double[] evaluateThisChange(double timeStep)
    	{
    		return evaluateChange(vars, timeStep);
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
    	public double[] evaluateChange(double[] current, double timeStep)
    	{
    		double[] change = new double[current.length];
    		change[0] = current[1];
    		change[1] = (Config.gravity / bob.rodLength) * Math.sin(current[0]);
    		change[2] = 1;
    		change[3] = 0;
    		return change;
    	}
    	
    	public void setVars(double[] _vars)
    	{
    		// if (_vars.length != vars.length) throw new Exception("Invalid sim configuration!");
    		bob.setTheta(_vars[0]);
    		bob.thetaPrime = _vars[1];
    		
    	}
    }
    
    
    /**
     * Set the angle of the Bob. 
     * Corrects for values greater than 2pi or less than 0.
     * @param _theta The angle to set. Can be any double value. 
     */
    public void setTheta(double theta)
    {
    	bob.setTheta(theta);
    }
    
    /**
     * Get the angle of the bob
     * @return The current angle. 
     */
    public double getTheta()
    {
    	return bob.theta;
    }
    
    /**
     * Set the angular velocity of the Bob. 
     * Corrects for values greater than 2pi or less than 0.
     * @param _theta The angle to set. Can be any double value. 
     */
    public void setThetaPrime(double thetaPrime)
    {
    	bob.setThetaPrime(thetaPrime);
    }
    
    /**
     * Get the angular velocity of the bob
     * @return The current angle. 
     */
    public double getThetaPrime()
    {
    	return bob.thetaPrime;
    }
    
    /**
     * Set the length of the rod in this simulation.
     * @param _rodLength The new length of the rod.
     * @throws Exception If the new length is not positive (Len <= 0).
     */
    public void setLength(double rodLength) throws Exception
    {
        bob.setLength(rodLength);
    }

    /**
     * Get the length of the rod in this simulation.
     * @return The length of the rod. 
     */
    public double getLength()
    {
        return bob.rodLength;
    }
    
    /**
     * Set the mass of the bob. 
     * @param _mass the new mass. 
     * @throws Exception If the new mass is not positive (mass <= 0).
     */
    public void setMass(double mass) throws Exception
    {
        bob.setMass(mass);
    }

    /**
     * Get the mass of the bob. 
     * @return the mass of the bob.
     */
    public double getMass()
    {
        return bob.mass;
    }
    
    
    
    /**
     * Events that should occur on every tick to maintain the simulation.
     * @param tickTime the amount of real time (in seconds) that passes.
     */
    public void tick(double tickTime)
    {
        // bob.tickAngle(tickTime / 1000);
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
    	        if (++ticks % 25 == 0) render();
    	    }
    	});
    	timer.start();
    }

    public static void main(String[] args) throws Exception
    {
        Pendulum p = new Pendulum(3, 5);
        p.loop();
    }
}
