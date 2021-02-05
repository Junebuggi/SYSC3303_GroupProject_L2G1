package ElevatorProject;

import java.util.Random;

public class ElevatorSystem {



	public static void main(String[] args) {
		Thread elevator, floor, scheduler;
		Scheduler schedulerObj = new Scheduler();
		scheduler = new Thread(schedulerObj, "Scheduler");
		elevator = new Thread(new Elevator(schedulerObj), "Elevator");
		floor = new Thread(new Floor(schedulerObj), "floor");
		
		elevator.start();
		floor.start();
		scheduler.start();


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