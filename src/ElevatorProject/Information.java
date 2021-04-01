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

/**
 * This class contains all the static final variables used in the system
 * 
 * @author SYSC_3303 L2 G1
 *
 */
public class Information {

	public static final Integer NUM_FLOORS = 22;
	public static final Integer NUM_ELEVATORS = 4;

	public static final Integer SCHEDULER_PORT = 23;

	public static final double TIME_MULTIPLIER = 0.1;

	// Time measured and averaged in Iteration 0 (unit: milliseconds)
	public static final Integer TRAVEL_TIME_PER_FLOOR = (int) (9500 * TIME_MULTIPLIER);
	public static final Integer TIME_OPEN_DOOR = (int) (4600 * TIME_MULTIPLIER);
	public static final Integer TIME_CLOSE_DOOR = (int) (4600 * TIME_MULTIPLIER);
	
	public static boolean gui = false;

}
