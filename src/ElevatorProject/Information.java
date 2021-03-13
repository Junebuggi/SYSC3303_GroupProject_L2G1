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
	
	public static final Integer NUM_FLOORS = 35;
	public static final Integer NUM_ELEVATOR = 10;
	
	public static final Integer SCHEDULER_PORT = 23;
	
	public static final double TIME_MULTIPLIER = 0.01;
	
	//Time measured and averaged in Iteration 0 (unit: milliseconds)
	public static final Integer TRAVEL_TIME_PER_FLOOR = (int) (9500 * TIME_MULTIPLIER);
	public static final Integer TIME_OPEN_DOOR = (int) (4600 * TIME_MULTIPLIER);
	public static final Integer TIME_CLOSE_DOOR = (int) (4600 * TIME_MULTIPLIER);
	

}
