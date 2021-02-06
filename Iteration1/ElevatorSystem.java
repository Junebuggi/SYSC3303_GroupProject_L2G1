package Iteration1;

import java.util.Random;

public class ElevatorSystem {


	private static Random rand = new Random();
	
	/**
	 * Creates a new floor thread, scheduler thread, and elevator thread.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		Thread elevator, floor, scheduler;

		Scheduler schedulerObj = new Scheduler();
		
    System.out.println("Setting up the Scheduler.");
		
    elevator = new Thread(new Elevator(schedulerObj, 1), "Elevator");
		floor = new Thread(new Floor(schedulerObj), "floor");
		scheduler = new Thread(schedulerObj, "scheduler");
		
    System.out.println("Threads are created.");
    
    elevator.start();
		scheduler.start();
    floor.start();
		
    System.out.println("Threads have started.\n");
		
    return;

	}

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