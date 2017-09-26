import java.util.ArrayList;

/**
 * This class is for managing the hero object. It contains the constructor, the update method and the collision 
 * method between the hero and fireballs.
 * 
 * @author Matt Derzay
 * @version 1.0
 */
public class Hero {
	//Initialize the graphic to the HERO file.
	private Graphic graphic = new Graphic("HERO"); 
	//A multiplier to make the graphic move farther each time it is updated.
	private float speed = 0.12f; 
	//The way in which the hero is controlled.
	int controlType; 
	
	/**
	 * This constructor initializes a new Hero object. It sets the position in 
	 * the x and y axis and sets its control type.
	 * 
	 * @param x is the position on the x axis.
	 * @param y is the position on the y axis.
	 * @param controlTypeP is the way the user controls the hero ( 1 through 3).
	 */
	public Hero(float x, float y, int controlTypeP) {
		//Setting the hero's initial position and control type.
		graphic.setX(x);
		graphic.setY(y);
		controlType = controlTypeP;
	}
	
	/**
	 * This is an accessor method to use in other classes to obtain this objects
	 * graphic.
	 * 
	 * @return The object Graphic, which represents the image of the hero.
	 */
	public Graphic getGraphic() {
		return this.graphic;
	}
	
	/**
	 * This method cycles through the fireball array list to see if any are colliding 
	 * with the hero object.
	 * 
	 * @param fireballs is the array list of fireballs.
	 * 
	 * @return Return true if the hero is colliding with a fireball, otherwise return
	 * false.
	 */
	public boolean handleFireballCollisions(ArrayList<Fireball> fireballs) {
		boolean hasCollided = false;
		
		//Cycle through the fireball array list.
		for(int i = 0; i < fireballs.size(); i++) {
			//Determine if the fireball has collided with hero
			if(fireballs.get(i).getGraphic().isCollidingWith(this.getGraphic())) {
				hasCollided = true;
				break;
			}
		}
		return hasCollided;
	}

	/** 
	 * This method updates the hero object. It creates new water objects in the position of 
	 * the hero and is drawn and moved based on the user input and control type chosen. 
	 * 
	 * @param time How much time has passed since the last time this method has been called.
	 * @param waterArrayP Array list of water objects.
	 */
	public void update(int time, Water[] waterArrayP) {
		//Create new water objects if space or mouse is pressed.
		if(GameEngine.isKeyPressed("SPACE") || GameEngine.isKeyHeld("MOUSE")) {
			//Only create a water object if there is an empty spot in the array.
			for(int i = 0; i < 8; i++) {
				if(waterArrayP[i] == null) {
					waterArrayP[i] = new Water(graphic.getX(), graphic.getY(), graphic.getDirection());
					break;
				}
			}
		}
		
		//Switch statement for each of the control types.
		switch(controlType) {
		case 1:
			float left = ((Double) Math.PI).floatValue(); //Pi as a float.
			float down = ((Double) (Math.PI / 2.0)).floatValue(); //Pi/2 as a float.
			float up   = ((Double) (Math.PI * 3.0 / 2.0)).floatValue(); //3Pi/2 as a float.
			
			//Move right if "D" is held and set the direction to the right.
			if(GameEngine.isKeyHeld("D") == true) {
				graphic.setX(graphic.getX() + (speed * time));
				graphic.setDirection(0);
			}
			//Move left if "A" is held and set direction to the left.
			if(GameEngine.isKeyHeld("A") == true) {
				graphic.setX(graphic.getX() - (speed * time));
				graphic.setDirection(left);
			}
			//Move up if "W" is held and set direction up.
			if(GameEngine.isKeyHeld("W") == true) {
				graphic.setY(graphic.getY() - (speed * time));
				graphic.setDirection(up);
			}
			//Move down if "S" is held and set direction down.
			if(GameEngine.isKeyHeld("S") == true) {
				graphic.setY(graphic.getY() + (speed * time));
				graphic.setDirection(down);
			}
			graphic.draw();
			break;
			
		case 2:
			//Hero will always face the mouse.
			graphic.setDirection(GameEngine.getMouseX(), GameEngine.getMouseY());
			
			//Move right if "D" is held.
			if(GameEngine.isKeyHeld("D") == true) {
				graphic.setX(graphic.getX() + (speed * time));
			}
			//Move left if "A" is held.
			if(GameEngine.isKeyHeld("A") == true) {
				graphic.setX(graphic.getX() - (speed * time));
			}
			//Move up if "W" is held.
			if(GameEngine.isKeyHeld("W") == true) {
				graphic.setY(graphic.getY() - (speed * time));
			}
			//Move down if "S" is held.
			if(GameEngine.isKeyHeld("S") == true) {
				graphic.setY(graphic.getY() + (speed * time));
			}
			
			graphic.draw();
			break;
			
		case 3:
			//Distance on the x axis of the hero to the mouse.
			float xDistance = Math.abs(graphic.getX() - GameEngine.getMouseX());
			//Distance on the y axis of the hero to the mouse.
			float yDistance = Math.abs(graphic.getY() - GameEngine.getMouseY());
			//Distance from the hero to the mouse.
			float distance = (float)Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
			
			//The hero will always face the mouse.
			graphic.setDirection(GameEngine.getMouseX(), GameEngine.getMouseY());
			
			//Move towards the mouse until the hero is within 20 pixels.
			if(distance >= 20) {
				graphic.setX(graphic.getX() + graphic.getDirectionX() * (speed * time));
				graphic.setY(graphic.getY() + graphic.getDirectionY() * (speed * time));
			}
			
			graphic.draw();
			break;
			
		default:
			System.out.println("Invalid control type definition.");
			
		}
	}
}
