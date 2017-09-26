/**
 * This is the class that manages the fireball objects. It contains the constructor for fireballs as well as fireball's
 * update method and the collision method between the fireball and water objects.
 * 
 * @author Matt Derzay
 * @version 1.0
 */
public class Fireball {
	//Initialize the graphic to the FIREBALL file.
	private Graphic graphic = new Graphic("FIREBALL");
	//A multiplier to make the graphic move farther each time it is updated.
	private float speed = 0.2f;
	//Whether or not the fireball still exists.
	private boolean isAlive = true;
	 
	/**
	 * This constructor initializes a new Fireball object. It sets the position in 
	 * the x and y axis and sets its angle it is facing.
	 * 
	 * @param x is the position on the x axis.
	 * @param y is the position on the y axis.
	 * @param directionAngle is the angle which the fireball is facing.
	 */
	public Fireball(float x, float y, float directionAngle) {
		//Set the initial position and direction of the fireball.
		graphic.setX(x);
		graphic.setY(y);
		graphic.setDirection(directionAngle);
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
	 * This method updates the graphic for the object to continue moving forward and 
	 * if the object goes to far off the screen is destroyed.
	 * 
	 * @param time How much time has passed since the last time this method was called.
	 */
	public void update(int time) {
		//Only update if the object still exists.
		if(isAlive == true) {	
			//If the fireball is 100 or move pixels off the screen, remove it from the game.
			if(graphic.getX() <= -100 || graphic.getX() >= GameEngine.getWidth() + 100 || graphic.getY() <= -100 ||
			   graphic.getY() >= GameEngine.getHeight() + 100) {
				isAlive = false;
			}
			
			//Move forward.
			graphic.setX(graphic.getX() + graphic.getDirectionX() * (speed * time));
			graphic.setY(graphic.getY() + graphic.getDirectionY() * (speed * time));
			
			graphic.draw(); 
		}
	}
	
	/**
	 * This method cycles through the water array to see if any are colliding 
	 * with the fireball object.
	 * 
	 * @param water is the array of water objects.
	 */
	public void handleWaterCollisions(Water[] water) {
		//Cycle through the water array.
		for(int i = 0; i < water.length; i++) {
			if(water[i] != null) {
				//If the water object exists, check to see if it is colliding with the fireball object.
				if(water[i].getGraphic().isCollidingWith(getGraphic())) {
					//If it is colliding, remove the fireball and water objects.
					this.isAlive = false;
					water[i] = null;
				}
			}
		}
	}
	
	/**
	 * This is a mutator method to set the fireball's isAlive field to false.
	 */
	public void destroy() {
		this.isAlive = false;
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
