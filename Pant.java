import java.util.ArrayList;
import java.util.Random;

/**
 * This is the class that manages the pant objects. It contains the constructor for the pant objects as well as the 
 * water object's update method and the collision method between the pant and fireball objects.
 *  
 * @author Matt Derzay
 * @version 1.0
 */
public class Pant {
	//Initialize the graphic to the FIREBALL file.
	private Graphic graphic = new Graphic("PANT");
	//A random object used to generate random numbers.
	private Random randGen;
	//Whether or not te pant object still exists.
	private boolean isAlive = true;
	 
	/**
	 * This constructor initializes a new Pant object. It sets the position in 
	 * the x and y axis.
	 * 
	 * @param x is the position on the x axis.
	 * @param y is the position on the y axis.
	 * @param randGen is the random object that is passed to generate random numbers.
	 */
	public Pant(float x, float y, Random randGen) {
		this.randGen = randGen;
		//Set the initial position of the pant object.
		graphic.setX(x);
		graphic.setY(y);
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
	
	public void update(int time) {
		//If the pant exists draw it.
		if(isAlive) {
			graphic.draw();
		}
	}
	
	/**
	 * This method cycles through the fireball array list to see if any are colliding 
	 * with the pant object.
	 * 
	 * @param fireballs is the array list of fireballs.
	 * 
	 * @return Return the new fire object created in the postion where the pants were
	 * destroyed.
	 */
	public Fire handleFireballCollisions(ArrayList<Fireball> fireballs) {
		//Cycle through the fireball array list.
		for(int i = 0; i < fireballs.size(); i++) {
				//Check to see if any fireball is colliding with the pant object.
				if(fireballs.get(i).getGraphic().isCollidingWith(getGraphic())) {	
					//If so remove the fireball and create a new fire where the pant used to be.
					fireballs.get(i).destroy();
					Fire fire = new Fire(getGraphic().getX(), getGraphic().getY(), randGen);
					return fire;
				}
			
		}
		return null;
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
