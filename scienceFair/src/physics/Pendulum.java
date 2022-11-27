package physics;

import java.awt.*;
import javax.swing.*;

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
        bob.setAngularVelocity(0.01);

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
     * Events that should occur on every tick to maintain the simulation.
     * @param tickTime the amount of real time (in seconds) that passes.
     */
    public void tick(double tickTime)
    {
        bob.tickAngle(tickTime);
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
    	while (++ticks <= 1000000) 
    	{
	        tick(0.015);
	        render();
	        try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }

    public static void main(String[] args) throws Exception
    {
        Pendulum p = new Pendulum(3, 5);
        p.loop();
    }
}
