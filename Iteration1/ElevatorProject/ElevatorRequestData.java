/**
 * ElevatorRequestData.java
 * 
 * The ElevatorRequest class models an event request
 *
 * @author rutvikshah
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ElevatorRequestData {
	
	private Time time; //hh:mm:ss.mmm
	private Integer floor; //current floor level
	private Integer carButton; //destination floor
	private String floorButton; //direction: "Up" or "Down"
	
	/**
	 * Constructor class used to initialize the object of the ElevatorRequestData class.
	 * 
	 * @param time			the time inputted from floor subsystem
	 * @param floor			the floor inputted from floor subsystem
	 * @param floorButton	the floorButton inputted from floor subsystem
	 * @param carButton		the carButton inputted from floor subsystem
	 */
	public ElevatorRequestData (Time time, Integer floor, String floorButton, Integer carButton) {
		this.time = time;
		this.floor = floor;
		this.carButton = carButton;
		this.floorButton = floorButton;
	}
}
