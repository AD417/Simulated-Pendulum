package physics;

import java.awt.*;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

public class Bob {
    /**
     * Position of the center. Default (0,0)
     */
    double centerX = 0;
    /**
     * Position of the center. Default (0,0)
     */
    double centerY = 0;

    /**
     * Angle off from the lowest point. Default 3.1415...
     */
    double theta = Math.PI ;

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
        centerX = x;
        centerY = y;
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
        centerX = x;
        centerY = y;
    }

    /**
     * Get the center position
     * @return A list of 2 doubles giving the (x, y) position of 
     * the position the bob rotates around.
     */
    public double[] getCenter()
    {
        return new double[] {centerX, centerY};
    }

    /**
     * Set the angle of the Bob. 
     * Corrects for values greater than 2pi or less than 0.
     * @param _theta The angle to set. Can be any double value. 
     */
    public void setTheta(double _theta) {
        if (_theta < 0)
        {
            int revolutions = (int) (_theta / (2 * Math.PI)) + 1;
            _theta -= revolutions * 2 * Math.PI;
        }
        theta = _theta % (2 * Math.PI) ;
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
        return centerY - rodLength;
    }

    /**
     * Get the current X position of the bob itself.
     * @return the current X position.
     */
    public double getX()
    {
        return centerX + (rodLength * Math.sin(theta));
    }

    /**
     * Get the current Y position of the bob itself.
     * @return the current Y position.
     */
    public double getY()
    {
        return centerY - rodLength * Math.cos(theta);
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
    public double[] getPosition()
    {
        return new double[] {getX(), getY()};
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
        // TODO: determine how to determine the gravity of the simualtion.
        return mass * 9.81 * (rodLength * getYDisplacement());
    }

    /**
     * Calculate the potential energy of the system, given the height 
     * of the center above its minimum.
     * @return the potential energy. 
     */
    public double getPotentialEnergy(double height)
    {
        return mass * 9.81 * (height + rodLength * getYDisplacement());
    }
    
    /**
     * Calculate the tangential velocity of the bob.
     * @return the tangential velocity of the bob. 
     */
    public double getVelocity() 
    {
    	// v = 2 * pi * r * w
    	return 2 * Math.PI * rodLength * thetaPrime;
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
        theta %= 2 * Math.PI;
        if (theta < 0) theta += 2 * Math.PI;
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
        double[] center = getCenter();
        double[] pos = getPosition();
        for (int i = 0; i < 2; i++)
        {
            center[i] = 50 * center[i] + 250;
            pos[i] = 50 * pos[i] + 250;
        }
        g.draw(new Line2D.Double(center[0], center[1], pos[0], pos[1]));
    }

    public void drawBob(Graphics2D g)
    {
        double[] pos = getPosition();
        pos[1] = 50 * pos[1] + 250;
        pos[0] = 50 * pos[0] + 250;
        g.fillOval((int)pos[0] - 10, (int)pos[1] - 10, 20, 20);
    }

    public String toString()
    {
        DecimalFormat f = new DecimalFormat("0.00");
        double[] pos = getPosition();
        String out = "Bob{(" + f.format(pos[0]) + "," + f.format(pos[1]) + "),";
        out += "t=" + f.format(theta) + ",l=" + f.format(rodLength) + "}";
        return out;
    }
}
