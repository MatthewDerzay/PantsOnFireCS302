import java.util.Random;

/**
 * This is the class that manages the fire objects. It contains the fire's constructor as well as the fire's update 
 * method and the collision method between fire objects and water objects.
 * 
 * @author Matt Derzay
 * @version 1.0
 */
public class Fire {
	//Initialize the graphic to the FIRE file.
	private Graphic graphic = new Graphic("FIRE"); 
	//A random object used to generate random numbers.
	private Random randGen; 
	//Countdown until the next fireball is fired.
	private int fireballCountdown; 
	//Life of the fire.
	private int heat = 40; 
	//Whether or not the fire has been put out.
	boolean isAlive = true; 
	
	/**
	 * This constructor initializes a new Fire object. It sets the position in 
	 * the x and y axis and the time between each fire ball.
	 * 
	 * @param x is the position on the x axis.
	 * @param y is the position on the y axis.
	 * @param randGen is the random object passed in to generate random numbers.
	 */
	public Fire(float x, float y, Random randGen) {
		this.randGen = randGen;
		graphic.setX(x);
		graphic.setY(y);
		fireballCountdown = randGen.nextInt(3000) + 3000;
	}
	
	/**
	 * This is an accessor method to use in other classes to obtain this objects
	 * graphic.
	 * 
	 * @return The graphic of the object.
	 */
	public Graphic getGraphic() {
		return this.graphic;
	}
	
	/**
	 * This method updates the fire object. It checks if the heat is above 
	 * zero(still alive) and counts a random amount between 3 and 6 seconds
	 * to where it will shoot a new fireball in a random direction.
	 * 
	 * @param time How much time has passed since the last time this method
	 * was called.
	 * 
	 * @return A fireball that is shot from the fire in a random direction.
	 */
	public Fireball update(int time) {	
		//If the heat is reduced to 0 the fire is put out and needs to be removed.
		if(heat <= 0) {
			isAlive = false;
		}
		
		//Only draw if the fire still exists.
		if(isAlive) {
			graphic.draw();
		}
			
		//Subtract the countdown by the amount of time has passed since the last time this method was called.
		fireballCountdown -= time;
		
		if(fireballCountdown <= 0) {
			//Create a new fireball when the timer reaches 0.
			Fireball fireball = new Fireball(graphic.getX(), graphic.getY(), randGen.nextFloat() * (float)Math.PI * 2);
			//Set the timer back to a time between 3 ad 6 seconds.
			fireballCountdown = randGen.nextInt(3000) + 3000;
			return fireball;
		}
		return null;
	}	
	
	/**
	 * This method cycles through the water array to see if any are colliding 
	 * with the fire object.
	 * 
	 * @param water is the array of water objects.
	 */
	public void handleWaterCollisions(Water[] water) {
		//Cycle through the water array.
		for(int i = 0; i < water.length; i++) {
			if(water[i] != null) {
				//If the water object exists then check to see if it is colliding with the fire object.
				if(water[i].getGraphic().isCollidingWith(getGraphic())) {
					//Remove the water and reduce the heat of the fire.
					water[i] = null;
					heat--;
				}
			}
		}
	}
	
	/**
	 * This method checks the objects isAlive field to see whether or not the 
	 * object is still alive or should be removed.
	 * 
	 * @return Return true if the object is still alive otherwise return false.
	 */
	public boolean shouldRemove() {
		if(isAlive) {
			return true;
		} else {
			return false;
		}
	}
}
