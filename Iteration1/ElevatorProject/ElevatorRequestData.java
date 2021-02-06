/**
 * 
 */
package ElevatorProject;

/**
 * @author rutvikshah
 *
 */

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ElevatorRequestData {
	private Time time; //hh:mm:ss.mmm
	private Integer floor; //current floor level
	private Integer carButton; //destination floor
	private String floorButton; //direction: "Up" or "Down"
	
	public ElevatorRequestData (Time time, Integer floor, String floorButton, Integer carButton) {
		this.time = time;
		this.floor = floor;
		this.carButton = carButton;
		this.floorButton = floorButton;
	}
}
