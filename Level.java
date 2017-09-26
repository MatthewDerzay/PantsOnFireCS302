import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.lang.Object;

/**
 * The Level class is responsible for managing all of the objects in your game.
 * The GameEngine creates a new Level object for each level, and then calls that
 * Level object's update() method repeatedly until it returns either "ADVANCE"
 * (to go to the next level), or "QUIT" (to end the entire game).
 * 
 * @author Matt Derzay
 * @version 1.0
 */
public class Level {
	//Middle of the screen on the x-axis.
	float heroX = GameEngine.getWidth() / 2; 
	//Middle of the screen on the y-axis.
	float heroY = GameEngine.getHeight() / 2; 
	//Which way the user controls the hero.
	int controlType; 
	//The random object to generate random numbers.
	Random randGen = new Random(); 
	//Number of pants the game starts with.
	int startingPantNumber = 20;
	//Number of fires the game starts with.
	int startingFireNumber = 6; 
	//The game will end if set to true.
	boolean gameLose = false; 
	//The current level of the game.
	static int level = 1; 
	
	//The hero object that will be initialized later.
	Hero hero;
	
	//The initialization of the array of water objects.
	Water[] water = new Water[] {null, null, null, null, null, null, null, null};
	
	//The initialization of the array lists of pant, fireball, and fire objects.
	ArrayList<Pant> pants = new ArrayList<Pant>();
	ArrayList<Fireball> fireballs = new ArrayList<Fireball>();
	ArrayList<Fire> fires = new ArrayList<Fire>();
	
	//Return values from different classes.
	Fireball fireballReturn;
	Water waterReturn;
	Fire fire;
	
	/**
	 * This constructor initializes a new Level object, so that the GameEngine
	 * can begin calling its update() method to advance the game's play.  In
	 * the process of this initialization, all of the objects in the current
	 * level should be instantiated and initialized to their beginning states.
	 * 
	 * @param randGen is the only Random number generator that should be used
	 * throughout this level, by the Level itself and all of the Objects within.
	 * @param level is a string that either contains the word "RANDOM", or the 
	 * contents of a level file that should be loaded and played. 
	 */
	public Level(Random randGen, String level) { 
		System.out.println("Level: " + level);
		if(level.equals("RANDOM")) {
			createRandomLevel();
		} else {
			loadLevel(level);
		}
	}
	
	/**
	 * The GameEngine calls this method repeatedly to update all of the objects
	 * within your game, and to enforce all of the rules of your game.
	 * 
	 * @param time is the time in milliseconds that have elapsed since the last
	 * time this method was called.  This can be used to control the speed that
	 * objects are moving within your game.
	 * 
	 * @return When this method returns "QUIT" the game will end after a short
	 * 3 second pause and a message indicating that the player has lost.  When
	 * this method returns "ADVANCE", a short pause and win message will be 
	 * followed by the creation of a new level which replaces this one.  When
	 * this method returns anything else (including "CONTINUE"), the GameEngine
	 * will simply continue to call this update() method as usual. 
	 */
	public String update(int time) {	
		//Call the update method of the hero class.
		hero.update(time, water);
		
		//Cycle through the water array and update water objects if it exists.
		for(int i = 0; i < water.length; i++) {
			if(water[i] != null) {
		    	waterReturn = water[i].update(time);
		    	//If the water object had reached its distance limit and returns null then delete water from the array.
		    	if(waterReturn == null) {
		    		water[i] = null;
		    	}
		    }
		}
		
		//Cycle through the pants array list and update each one.
		for(int j = 0; j < pants.size(); j++) {
			pants.get(j).update(time);
		}
		
		//Cycle through the fireballs array list and update each one.
		for(int k = 0; k < fireballs.size(); k++) {
			fireballs.get(k).update(time);
		}
		
		//Cycle through the fires array list and update each one.
		for(int l = 0; l < fires.size(); l++) {
			fireballReturn = fires.get(l).update(time);
			/*
			 * If the time has reached 0 for the fire's fireball timer and returns a new fireball then add the new 
			 * fireball to the array list.
			*/
			if(fireballReturn != null) {
				fireballs.add(fireballReturn);
			}
		}
		
		//If hero is hit by a fireball set game lose to true.
		gameLose = hero.handleFireballCollisions(fireballs);
		
		//If game lose equal true then end the game.
		if(gameLose == true) {
			return "QUIT";
		}
		
		//Cycle through the fireball array list to see if fireballs are colliding with water.
		for(int m = 0; m < fireballs.size(); m++) {
			fireballs.get(m).handleWaterCollisions(water);
		}
		
		//Cycle through the fire array list to see if fires are colliding with water.
		for(int n = 0; n < fires.size(); n++) {
			fires.get(n).handleWaterCollisions(water);
		}
		
		//Cycle through the pant array list to see if fireballs are colliding with pants.
		for(int o = 0; o < pants.size(); o++) {
			fire = pants.get(o).handleFireballCollisions(fireballs);
			//If fireballs collide with pants, remove the pant and add the new fire to the array list.
			if(fire != null) {
				fires.add(fire);
				pants.remove(o);
			}
		}
		
		//Cycle through the fireball array list to see if any should be remove because they are not alive anymore.
		for(int p = 0; p < fireballs.size(); p++) {
			if(fireballs.get(p).shouldRemove() == false) {
				fireballs.remove(p);
			}
		}
		
		//Cycle through the fire array list to see if any should be remove because they are not alive anymore.
		for(int q = 0; q < fires.size(); q++) {
			if(fires.get(q).shouldRemove() == false) {
				fires.remove(q);
			}
		}
		//Cycle through the pant array list to see if any should be remove because they are not alive anymore.
		for(int r = 0; r < pants.size(); r++) {
			if(pants.get(r).shouldRemove() == false) {
				pants.remove(r);
			}
		}
		
		//If all pants die then end the game.
		if(pants.size() == 0) {
			return "QUIT";
		}
		
		//If all fires have been put out then progress to next level.
		if(fires.size() == 0) {
			level++;
			return "ADVANCE";
		}
		
		return "CONTINUE"; 
	}

	/**
	 * This method returns a string of text that will be displayed in the
	 * upper left hand corner of the game window.  Ultimately this text should 
	 * convey the number of unburned pants and fires remaining in the level.  
	 * However, this may also be useful for temporarily displaying messages that 
	 * help you to debug your game.
	 * 
	 * @return a string of text to be displayed in the upper-left hand corner
	 * of the screen by the GameEngine.
	 */
	public String getHUDMessage() {
		return "Level: " + level + "\nPants left: " + pants.size() + "\nFires left: " + fires.size(); 
	}

	/**
	 * This method creates a random level consisting of a single Hero centered
	 * in the middle of the screen, along with 6 randomly positioned Fires,
	 * and 20 randomly positioned Pants.
	 */
	public void createRandomLevel() { 
		//Set the control type randomly between the three available.
		controlType = randGen.nextInt(3) + 1;
		
		//The initialization of the hero object.
		hero = new Hero(heroX, heroY, controlType); 
		
		//Create the amount of pants stated above to start the game with.
		for(int a = 0; a < startingPantNumber; a++) {
			//Create a new pant object in a random position and add to the array list.
			pants.add(new Pant(randGen.nextInt(GameEngine.getWidth()), 
								randGen.nextInt(GameEngine.getHeight()), randGen));
		}
					
		//Create the amount of fires stated above to start the game with.
		for(int b = 0; b < startingFireNumber; b++) {
			//Create a fire object in a random position and add to the array list.
			fires.add(new Fire(randGen.nextInt(GameEngine.getWidth()), 
								randGen.nextInt(GameEngine.getHeight()), randGen));
		}		
	}

	/**
	 * This method initializes the current game according to the Object location
	 * descriptions within the level parameter.
	 * 
	 * @param level is a string containing the contents of a custom level file 
	 * that is read in by the GameEngine.  The contents of this file are then 
	 * passed to Level through its Constructor, and then passed from there to 
	 * here when a custom level is loaded.  You can see the text within these 
	 * level files by dragging them onto the code editing view in Eclipse, or 
	 * by printing out the contents of this level parameter.  Try looking 
	 * through a few of the provided level files to see how they are formatted.
	 * The first line is always the "ControlType: #" where # is either 1, 2, or
	 * 3.  Subsequent lines describe an object TYPE, along with an X and Y 
	 * position, formatted as: "TYPE @ X, Y".  This method should instantiate 
	 * and initialize a new object of the correct type and at the correct 
	 * position for each such line in the level String.
	 */
	public void loadLevel(String level) { 
		//Beginning of a line that is for a new hero object.
		final String HERO = "HERO @"; 
		//Beginning of a line that is for a new pant object.
		final String PANT = "PANT @"; 
		//Beginning of a line that is for a new fire object.
		final String FIRE = "FIRE @"; 
		//Beginning of a line that is for the control type.
		final String CONTROL_TYPE = "ControlType:"; 
		
		Scanner scnr = new Scanner(level);
		
		//While there are still lines left in the file.
		while(scnr.hasNextLine()) {
			String line = scnr.nextLine();
			
			if (line.startsWith(CONTROL_TYPE)) {
				//Read in the type number and convert it into an int.
				String controlTypeS = line.substring(CONTROL_TYPE.length()).trim();
				int controlTypeI = Integer.parseInt(controlTypeS);
				//Set the control type for the level.
				controlType = controlTypeI;
				
			} else if(line.startsWith(FIRE)) {
				//Create a new string of the coordinates after the object type and remove spaces.
				String coords = line.substring(FIRE.length()).trim();
				//Split the coordinates up into two strings in an array.
				String[] coordsArray = coords.split(",");
				//Convert strings into floats and create a new fire object.
				float fX = (float)Double.parseDouble(coordsArray[0]);
				float fY = (float)Double.parseDouble( coordsArray[1]);		
				fires.add(new Fire(fX, fY, randGen));
				
			} else if(line.startsWith(HERO)) {
				//Create a new string of the coordinates after the object type and remove spaces.
				String coords = line.substring(HERO.length()).trim();
				//Split the coordinates up into two strings in an array.
				String[] coordsArray = coords.split(",");
				//Convert strings into floats and create a new hero object.
				float hX = (float)Double.parseDouble(coordsArray[0]);
				float hY = (float)Double.parseDouble( coordsArray[1]);		
				hero = new Hero(hX, hY, controlType);
				
			} else if(line.startsWith(PANT)) {
				//Create a new string of the coordinates after the object type and remove spaces.
				String coords = line.substring(PANT.length()).trim();
				//Split the coordinates up into two strings in an array.
				String[] coordsArray = coords.split(",");
				//Convert strings into floats and create a new pant object.
				float pX = (float)Double.parseDouble(coordsArray[0]);
				float pY = (float)Double.parseDouble( coordsArray[1]);		
				pants.add(new Pant(pX, pY, randGen));
			}
		}
		scnr.close();
	}

	/**
	 * This method creates and runs a new GameEngine with its first Level.  Any
	 * command line arguments passed into this program are treated as a list of
	 * custom level filenames that should be played in a particular order.
	 * 
	 * @param args is the sequence of custom level files to play through.
	 */
	public static void main(String[] args) {
		GameEngine.start(null,args);
	}
}