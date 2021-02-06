/**
 * Information.java
 * 
 * A class that stores the constants of Iteration1 and the enumerators for the lamp state, motor direction, 
 * door state and direction lamp.
 *
 * @author Hasan Baig
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;

public class Information {
	
	public static final Integer NUM_FLOORS = 7;
	
	//Time measured and averaged in Iteration 0 (unit: milliseconds)
	public static final Integer TRAVEL_TIME_PER_FLOOR = 19800;
	public static final Integer TIME_OPEN_DOOR = 4600;
	public static final Integer TIME_CLOSE_DOOR = 4600;
	
	public enum lampState{ON, OFF}
	public enum motorDirection{UP, DOWN, IDLE}
	public enum doorState{OPEN, CLOSE}
	public enum directionLamp{UP, DOWN, NOT_PRESSED}


}
