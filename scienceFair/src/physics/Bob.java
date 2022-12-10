package physics;

import java.awt.*;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import math.Vector;

public class Bob {
    /**
     * Position of the center. Default (0,0)
     */
    Vector center = new Vector();

    double lowestPoint = 0;
    
    /**
     * Angle off from the lowest point. Default 3.1415...
     */
    double theta = 0;
    
    double oldTheta = 0;

    /**
     * Change in angle per second.
     */
    double thetaPrime = 0;

    /**
     * Length of the rod connecting this bob to its other point. 
     */
    double rodLength;
    
    /**
     * Mass of the bob. Mass is idealized as a single point. 
     */
    double mass;

    public Bob(double _rodLength, double _mass) throws Exception
    {
        if (_rodLength <= 0) throw new Exception("Invalid Rod Length!");
        if (_mass <= 0) throw new Exception("Invalid mass!");
        rodLength = _rodLength;
        mass = _mass;
    }

    public Bob(double x, double y, double _rodLength, double _mass) 
        throws Exception
    {
        this(_rodLength, _mass);
        center = new Vector(x, y);
    }

    public Bob(double x, double y, double _rodLength, double _theta, 
        double _mass) throws Exception
    {
        this(x, y, _rodLength, _mass);
        theta = _theta;
    }

    /**
     * Set the new center of the circle this bob rotates around.
     * @param x The new X-position of the center.
     * @param y The new Y-position of the center.
     */
    public void setCenter(double x, double y)
    {
        center = new Vector(x, y);
    }
    
    /**
     * Set the new center of the circle this bob rotates around.
     * @param pos a pair of doubles for the new x and y position. 
     */
    public void setCenter(Vector pos)
    {
    	center = new Vector(pos.x, pos.y);
    }

    /**
     * Get the center position
     * @return A list of 2 doubles giving the (x, y) position of 
     * the position the bob rotates around.
     */
    public Vector getCenter()
    {
        return new Vector(center.x, center.y);
    }

    /**
     * Set the angle of the Bob. 
     * Corrects for values greater than 2pi or less than 0.
     * @param _theta The angle to set. Can be any double value. 
     */
    public void setTheta(double _theta) {
    	if (Config.constrainTheta)
    	{
	        if (_theta < 0)
	        {
	            int revolutions = (int) (_theta / (2 * Math.PI)) + 1;
	            _theta -= revolutions * 2 * Math.PI;
	        }
	        theta = _theta % (2 * Math.PI);
    	}
    	else 
    	{
    		theta = _theta;
    	}
    }

    /**
     * Get the angle of the bob
     * @return The current angle. 
     */
    public double getTheta() {
        return theta;
    }
    /**
     * Get the angular velocity of the bob
     * @return The current angle. 
     */
    public double getThetaPrime() {
        return thetaPrime;
    }

    /**
     * Set the angular velocity of the Bob. 
     * Corrects for values greater than 2pi or less than 0.
     * @param _theta The angle to set. Can be any double value. 
     */
    public void setThetaPrime(double _thetaPrime) {
        thetaPrime = _thetaPrime;
    }

    /**
     * Set the length of the rod connection this bob to its center.
     * @param _rodLength The new length of the rod.
     * @throws Exception If the new length is not positive (Len <= 0).
     */
    public void setLength(double _rodLength) throws Exception
    {
        if (_rodLength <= 0) throw new Exception("Invalid Rod Length!");
        rodLength = _rodLength;
    }

    /**
     * Get the length of the rod connected to this bob. 
     * @return The length of the rod. 
     */
    public double getLength()
    {
        return rodLength;
    }

    /**
     * Set the mass of the bob. 
     * @param _mass the new mass. 
     * @throws Exception If the new mass is not positive (mass <= 0).
     */
    public void setMass(double _mass) throws Exception
    {
        if (_mass <= 0) throw new Exception("Invalid Mass!");
        rodLength = _mass;
    }

    /**
     * Get the mass of the bob. 
     * @return the mass of the bob.
     */
    public double getMass()
    {
        return mass;
    }

    // Non-stored getters:
    
    /**
     * Calculate the lowest possible height that this bob can be, 
     * given its current center. 
     * @return The lowest possible height of the bob.
     */
    public double getMinimumHeight()
    {
        return center.y - rodLength;
    }

    /**
     * Get the current X position of the bob itself.
     * @return the current X position.
     */
    public double getX()
    {
        return center.x + (rodLength * Math.sin(theta));
    }

    /**
     * Get the current Y position of the bob itself.
     * @return the current Y position.
     */
    public double getY()
    {
        return center.y - rodLength * Math.cos(theta);
    }

    /**
     * Get the current vertical displacement of the bob from its rest state.
     * @return the height of the bob from its minimum. 
     */
    public double getYDisplacement()
    {
        return (rodLength * (1 - Math.cos(theta)));
    }

    /**
     * Get the current position of the bob
     * @return a list of doubles giving the (x,y) position of the bob.
     */
    public Vector getPosition()
    {
        return new Vector(getX(), getY());
    }

    /**
     * Calculate the potential energy of the system, ignoring the height 
     * of the center above its minimum.
     * @return the potential energy. 
     */
    public double getPotentialEnergy()
    {
        // U(g) = m * g * h
        // h = l * (1 - cos(theta))
        // TODO: determine how to determine the gravity of the simulation.
        return mass * Config.gravity * getYDisplacement();
    }

    /**
     * Calculate the potential energy of the system, given the height 
     * of the center above its minimum.
     * @return the potential energy. 
     */
    public double getPotentialEnergy(double height)
    {
        return mass * 
        		Config.gravity * 
        		(height + rodLength * getYDisplacement());
    }
    
    /**
     * Calculate the tangential velocity of the bob.
     * @return the tangential velocity of the bob. 
     */
    public double getVelocity() 
    {
    	// v = r * w
    	return rodLength * thetaPrime;
    }

    /**
     * Calculate the kinetic energy of the bob. 
     * @return the kinetic energy of the bob. 
     */
    public double getKineticEnergy()
    {
    	// KE = 1/2 * m * v^2
    	double v = getVelocity();
    	return 0.5 * mass * v * v;
    }

    // Modifying methods:

    /**
     * Change the current angle based on the current angular velocity.
     * @param tickTime the amount of time to use when ticking the angle.
     */
    public void tickAngle(double tickTime)
    {
        theta += thetaPrime * tickTime;
        if (Config.constrainTheta) 
        {
	        theta %= 2 * Math.PI;
	        if (theta < 0) theta += 2 * Math.PI;
        }
    }

    /**
     * Draw a line representing the rod to a screen.
     * @param g The screen to draw to.
     */
    public void drawLine(Graphics2D g)
    {
		g.setStroke(
            new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        );
        Vector center = getCenter();
        Vector pos = getPosition();

        center.x = -50 * center.x + 250;
        pos.x = -50 * pos.x + 250;
        center.y = -50 * center.y + 250;
        pos.y = -50 * pos.y + 250;
        g.draw(new Line2D.Double(center.x, center.y, pos.x, pos.y));
    }

    public void drawBob(Graphics2D g)
    {
        Vector pos = getPosition();
        pos.x = -50 * pos.x + 250;
        pos.y = -50 * pos.y + 250;
        g.fillOval((int)pos.x - 10, (int)pos.y - 10, 20, 20);
    }

    @Override
    public String toString()
    {
        DecimalFormat f = new DecimalFormat("0.00");
        Vector pos = getPosition();
        String out = "Bob{(" + f.format(pos.x) + "," + f.format(pos.y) + "),";
        out += "t=" + f.format(theta) + ",l=" + f.format(rodLength) + "}";
        return out;
    }
}
