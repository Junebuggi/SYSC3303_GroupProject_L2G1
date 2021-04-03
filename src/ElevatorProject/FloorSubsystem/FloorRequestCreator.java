
/**
 * 
 * The FloorRequestCreator class will create and print nRequests with parameters from
 * the Information class. These requests can then be copied and pasted in to the
 * floorRequest.txt file.
 *
 * @author Emma Boulay [Iteration 3]
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject.FloorSubsystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import ElevatorProject.Information;

public class FloorRequestCreator {
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	private static Random rand = new Random();

	/**
	 * This method will print nRequests with the specified parameters with a
	 * chronological 5-15 second difference in time stamps
	 * 
	 * @param nFloor     The number of floors the system has
	 * @param nElevators The number of elevators the system has
	 * @param nRequest   The number of requests to be printed
	 */
	public static void printFloorRequest(int nFloor, int nElevators, int nRequest) {
		Date now = new Date();
		for (int i = 0; i < nRequest; i++) {
			String strDate = sdf.format(now);
			int offset = rand.nextInt(1000) + 4000;
			now = new Date(now.getTime() + offset);
			int flr = rand.nextInt(nFloor + 1 - 1) + 1;
			String floor = "" + flr;
			String floorButton;

			// Logic to ensure first level only has Up button and top level only down button
			if (flr == 1)
				floorButton = "UP";
			else if (flr == nFloor)
				floorButton = "DOWN";
			else
				floorButton = (new String[] { "UP", "DOWN" })[rand.nextInt(2)];
			String car;

			if (floorButton.equals("UP")) {
				car = String.valueOf(rand.nextInt(nFloor - flr) + (flr + 1));
			} else
				car = String.valueOf(rand.nextInt(flr) + 1);

			String[] message = new String[] { strDate, floor, floorButton, car };
			System.out.println(String.join(" ", message));
		}
		return;
	}

	public static void main(String[] args) {
		printFloorRequest(Information.NUM_FLOORS, Information.NUM_ELEVATORS, 1000);
	}
}
