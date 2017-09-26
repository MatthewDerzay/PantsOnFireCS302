/**
 * This is the class that manages water objects. It contains water object constructor as well as its update method.
 * 
 * @author Matt Derzay
 * @version 1.0
 */
public class Water {
	//Initialize the graphic to the WATER file.
	private Graphic graphic = new Graphic("WATER"); 
	//A multiplier to make the graphic move farther each time it is updated.
	private float speed = 0.7f;
	//The distance the water object had traveled over its lifetime.
	private float distanceTraveled = 0;
	//Distance the object has traveled since the last time the update method was called.
	private float distanceChange; 
	 
	/**
	 * This constructor initializes a new Water object. It sets the position in 
	 * the x and y axis and sets the angle it is facing.
	 * 
	 * @param x is the position on the x axis.
	 * @param y is the position on the y axis.
	 * @param direction is the angle the water is facing.
	 */
	public Water(float x, float y, float direction) {
		//Set the initial position and direction of the new water object.
		graphic.setX(x);
		graphic.setY(y);
		graphic.setDirection(direction);
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
	 * This method updates the water object. It draws the graphic and keeps the
	 * object moving forward.
	 * 
	 * @param time How much time has passed since the last time this method was 
	 * called.
	 * 
	 * @return It returns itself if it is still alive or null if it is not.
	 */
	public Water update(int time) {	
		//Distance the water object has moved since the last time the update method was called.
		distanceChange = (float) Math.sqrt(graphic.getDirectionX() * (speed * time) * 
									       graphic.getDirectionX() * (speed * time) + 
										   graphic.getDirectionY() * (speed * time) * 
										   graphic.getDirectionY() * (speed * time));
		
		distanceTraveled += distanceChange;
		
		//Move forward until the water object had traveled 200 pixels.
		if (distanceTraveled < 200) {
			graphic.setX(graphic.getX() + graphic.getDirectionX() * (speed * time));
			graphic.setY(graphic.getY() + graphic.getDirectionY() * (speed * time));
		} else {
			return null;
		}
		
		graphic.draw();
		
		return this;
	}
}
