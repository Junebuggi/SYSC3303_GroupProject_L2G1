/**
 * ElevatorSystem.java
 * 
 * The main class which initializes the floorSubsystem(client), elevatorSubsystem(client) and the
 * Scheduler(server) threads and then starts them.
 *
 * @author ALL
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;

import java.util.Random;

public class ElevatorSystem {

	private static Random rand = new Random();
	
	/**
	 * Creates a new floor thread, scheduler thread, and elevator thread.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Thread elevator, floor;
		
		Scheduler schedulerObj = new Scheduler();
    		System.out.println("Setting up the Scheduler.");
		
    		elevator = new Thread(new Elevator(schedulerObj, 1), "Elevator");
    		floor = new Thread(new Floor(schedulerObj), "Floor");

		
    		System.out.println("Threads are created.");
    
    		elevator.start();
    		floor.start();
		
    		System.out.println("Threads have started.\n");
		
    		return;
	}

	/**
	 * Creates a random elevator request. This method was used to construct the floorRequest.txt file
	 */
	private static String makeElevatorRequest() {
		Random rand = new Random();
		String hh = String.format("%02d", rand.nextInt(24));
		String mm = String.format("%02d", rand.nextInt(60));
		String ss = String.format("%02d", rand.nextInt(60));
		String mmm = String.format("%02d", rand.nextInt(1000));
		String floor = String.valueOf(rand.nextInt(7) + 1);
		String floorButton = (new String[] { "Up", "Down" })[rand.nextInt(2)];
		String car = String.valueOf(rand.nextInt(7) + 1);
		String[] message = new String[] { hh + ":" + mm + ":" + ss + "." + mmm, floor, floorButton, car };

		// a string separated by white spaces
		return String.join(" ", message);
	}

}
