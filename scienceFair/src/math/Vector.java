package math;

/**
 * 2D Vector class for storing positions in space.
 * Only implements methods useful for this program. Sorry!
 * @author AD417
 *
 */
public class Vector 
{
	/**
	 * X component of a vector. 
	 */
	public double x;
	/**
	 * Y component of a vector.
	 */
	public double y;
	
	public Vector(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector() 
	{
		this(0, 0);
	}
	
	public Vector(Vector vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	/**
	 * Get the length of a vector -- the hypotenuse of a right triangle 
	 * with side lengths x and y. 
	 * @return The length of the vector. 
	 */
	public double getMagnitude()
	{
		return Math.hypot(x, y);
	}
	
	/**
	 * Create a normalized vector -- a vector with length 1 pointing in the 
	 * same direction as this vector. 
	 * @return A vector object with magnitude 1. 
	 */
	public Vector getNormal()
	{
		double vectorLength = getMagnitude();
		return new Vector(x / vectorLength, y / vectorLength);
	}
	
	/**
	 * Create a new vector in the same direction as this vector with 
	 * a set length other than 1. 
	 * Negative values will invert the vector. 
	 * @param newLength The new length of the vector. 
	 * @return A vector object with the given new length. 
	 */
	public Vector withLength(double newLength)
	{
		Vector normalVector = getNormal();
		return new Vector(
				normalVector.x * newLength, 
				normalVector.y * newLength
		);
	}
	
	/**
	 * Get a vector of the same magnitude, rotated 90 degrees *clockwise*.
	 * @return A vector object that's perpendicular to the input vector. 
	 */
	public Vector getPerpendicular()
	{
		return new Vector(y, -x);
	}
	
	public Vector add(Vector other)
	{
		return new Vector(
				this.x + other.x,
				this.y + other.y
		);
	}
	
	public Vector sub(Vector other)
	{
		return new Vector(
				this.x - other.x,
				this.y - other.y
		);
	}
	
	@Override
	public String toString()
	{
		return "Vector{x: " + x + ", y: " + y + "}";
	}
	
	public static void main(String[] args)
	{
		System.out.println("Usage: record positions in 2D.");
	}
}
