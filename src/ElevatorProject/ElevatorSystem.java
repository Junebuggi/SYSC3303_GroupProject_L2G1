package ElevatorProject;

import java.util.Random;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ElevatorSystem {

	private static Random rand = new Random();
	
	/**
	 * Creates a new floor thread, scheduler thread, and elevator thread.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Thread elevator, floor, scheduler;
<<<<<<< HEAD
		Scheduler schedule = new Scheduler();
		System.out.println("Setting up the Scheduler.");

		elevator = new Thread(new Elevator(schedule, 1), "Elevator");
		floor = new Thread(new Floor(), "Floor");
		scheduler = new Thread(new Scheduler(), "Scheduler");
		System.out.println("Threads are created.");
		
		elevator.start();
		floor.start();
		scheduler.start();
		System.out.println("Threads have started.\n");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("floorRequest.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String request;
		for (int i = 0; i < 100; i++) {
			request = makeElevatorRequest();
			System.out.println(request);
			writer.println(request);
		}
		writer.close();
		return;

	}

	private static String makeElevatorRequest() {

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