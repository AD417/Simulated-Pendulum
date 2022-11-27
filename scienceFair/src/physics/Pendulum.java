package physics;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

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

    /**
     * The number of frames that have already been drawn.
     */
    int ticks = 0;


    public Pendulum(double rodLength, double mass) throws Exception
    {
        bob = new Bob(rodLength, mass);
        bob.setThetaPrime(0.1);

        frame = new JFrame();
        frame.add(new Render());
		frame.setSize(517, 537);
		frame.setVisible(true);
        frame.setTitle("Pendulum Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    	return bob.getTheta();
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
    	return bob.getThetaPrime();
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
        return bob.getLength();
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
        return bob.getMass();
    }
    
    
    
    /**
     * Events that should occur on every tick to maintain the simulation.
     * @param tickTime the amount of real time (in seconds) that passes.
     */
    public void tick(double tickTime)
    {
        bob.tickAngle(tickTime / 1000);
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
    	        tick(Config.tickSize);
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
