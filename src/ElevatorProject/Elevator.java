package ElevatorProject;
import java.util.*;
import java.lang.Math.*;

public class Elevator implements Runnable {
	
	private Scheduler schedule;
	private Integer elevatorNumber;
	private Integer currentFloor = 0;
	private Information.doorState doorState = Information.doorState.CLOSE;
	private Information.lampState lampState = Information.lampState.OFF;
	
	Object elevatorRequest = null;
	private Integer floorRequestedFrom;
	private Integer floorToVisit;
	private Boolean stationary = true;
	
//	private Integer[] elevatorButtonSelected = new Integer[NUM_FLOORS];	//set of elevator button to select a floor
//	private Integer[] elevatorLamp = new Integer[NUM_FLOORS];			//indicate the floor(s) which will be visited by the elevator
	
	/**
	 * Constructor class used to initialize the object of the Elevator class.
	 * 
	 * @param schedule			the schedule where the actions of the elevator are passed to
	 * @param elevatorNumber	the elevator number
	 */
	public Elevator(Scheduler schedule, int elevatorNumber) {
		this.schedule = schedule;
		this.elevatorNumber = elevatorNumber;
	}
	
	/**
	 * Method to open door.
	 */
	public void openDoor() {
		try {
			Thread.sleep(Information.TIME_OPEN_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		doorState = Information.doorState.OPEN;
		System.out.println("Elevator door opened.");
	}
	
	/**
	 * Method to close door.
	 */
	public void closeDoor() {
		try {
			Thread.sleep(Information.TIME_CLOSE_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		doorState = Information.doorState.CLOSE;
		System.out.println("Elevator door closed.");
	}
	
	/**
	 * Method to move the elevator.
	 */
	public void move(int floor) {
		int numFloorsToTravel = Math.abs(currentFloor-floor);
		try {
			Thread.sleep(Information.TRAVEL_TIME_PER_FLOOR*numFloorsToTravel);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		currentFloor=floor;
		System.out.println("Elevator is moving to floor " + currentFloor);
	}
	
	/**
	 * Method prints current elevator information.
	 */
	public void displayInformation() {
		System.out.println("This is Elevator Number: " + this.elevatorNumber);
		System.out.println("The Current Floor is: " + this.currentFloor);
		System.out.println();
	}
	
	/**
	 * Overrides the run method of the Runnable interface.
	 * The floor requests from Scheduler are received if:
	 * 		the queue is not empty and
	 * 		the elevator is stationary.
	 */	
	@Override
	public void run() { 
		for(;;) {
			displayInformation();
			elevatorRequest = schedule.getRequest(stationary);
			
			floorRequestedFrom = 1; //dont which data structure to deconstruct 
			floorToVisit = 4;		//also dont know how to get value from Object (toString?)
			
			System.out.println("The users floor is: " + floorRequestedFrom);
			System.out.println("The requested floor is: " + floorToVisit);
			
			if(doorState == Information.doorState.OPEN) { closeDoor(); }
			stationary = false;
			move(floorRequestedFrom);
			stationary = true;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
			
			schedule.sendMessage();
			stationary = false;
			move(floorToVisit);
			stationary = true;
			
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
	}

}
