package ElevatorProject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Elevator.java
 * 
 * The Elevator threads floorsToVisit arraylist is updated by requests received
 * from the Elevator Subsystem. When there are requests to be serviced and the
 * elevator is in the IDLE state, it will service requests. It will also inform
 * the scheduler through the arrival sensor at each floor it approaches to check
 * if it should stop. It also changes the states of the state machine.
 *
 * @author Hasan Baig
 * @author Alden Wan Yeung Ng
 * @author Emma Boulay [iteration 3]
 * 
 *         SYSC 3303 L2 Group 1
 * @version 1.0
 */

/**
 * @author emmaboulay
 *
 */
public class Elevator extends Network implements Runnable {

	// Current Elevator State
	private ElevatorState elevatorState;
	
	// All Concrete Elevator States
	private ElevatorState idle;
	private ElevatorState moving;
	private ElevatorState arrived;

	private int schedulerPort;
	private int elevatorNumber;
	private int currentFloor = 1;
	private Motor motor = Motor.IDLE;
	private Door door = Door.OPEN;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

	public enum Motor {
		UP, DOWN, IDLE;
	}

	public enum Door {
		OPEN, CLOSE;
	}

	// A hashmap that stores all the destinations (values) of the passengers who get
	// picked up at a floor (the key)
	private HashMap<Integer, ArrayList<Integer>> floorDestinations = new HashMap<Integer, ArrayList<Integer>>();
	// A list of all the buttons currently pressed and floors where an elevator is
	// requested for pickup
	private ArrayList<Integer> floorsToVisit = new ArrayList<>();
	private int nFloors;
	private ElevatorButton[] btns;
	private DirectionLamp[] dirLamps;

	/**
	 * Constructor class used to initialize the object of the Elevator class.
	 * 
	 * @param elevatorNumber The elevator number [1 to nElevators]
	 * @param schedulerPort The port of the scheduler's listening thread
	 * @param nFloors The number of floors the building has
	 */
	public Elevator(int elevatorNumber, int schedulerPort, int nFloors) {

		this.elevatorNumber = elevatorNumber;
		this.schedulerPort = schedulerPort;
		this.nFloors = nFloors;

		btns = createButtons();
		dirLamps = new DirectionLamp[] { new DirectionLamp("UP"), new DirectionLamp("DOWN") };

		// Creating all concrete state objects
		this.idle = new IdleES(this);
		this.moving = new MovingES(this);
		this.arrived = new ArrivedES(this);

		// Default State to Idle
		elevatorState = idle;

	}

	/**
	 * This method creates all the elevator buttons
	 * 
	 * @return An array of all the elevator buttons in the elevator
	 */
	public ElevatorButton[] createButtons() {
		ElevatorButton[] btns = new ElevatorButton[nFloors];

		for (int i = 1; i <= nFloors; i++) {
			btns[i - 1] = new ElevatorButton(i);
		}

		return btns;
	}

	/**
	 * Returns State of this elevator
	 */
	public ElevatorState getState() {
		return this.elevatorState;
	}

	/**
	 * Set the State of the Elevator
	 */
	public void setState(ElevatorState newElevatorState) {
		elevatorState = newElevatorState;
	}
	
	/**
	 * Returns the Idle state
	 * 
	 * @return Idle state
	 */
	public ElevatorState getIdleState() {
		return this.idle;
	}
	
	/**
	 * Returns the moving state
	 * 
	 * @return Moving state
	 */
	public ElevatorState getMovingState() {
		return this.moving;
	}

	/**
	 * Returns the Arrived state
	 * 
	 * @return Arrived state
	 */
	public ElevatorState getArrivedState() {
		return this.arrived;
	}

	/**
	 * Method prints current elevator information.
	 */
	public String toString(String[] request) {
		
		String strRequest = Network.pac.joinStringArray(request);
		String[] parsedStr = strRequest.split(" ");
		return "Time: " + parsedStr[1] + "\nFloor: " + parsedStr[2] + "\nFloor Button: " + parsedStr[3]
				+ "\nCar Button: " + parsedStr[4];
	}

	/**
	 * Method to move the elevator.
	 */
	public void move(int floor) {
		//this.stationary = false;
		int numFloorsToTravel = Math.abs(currentFloor - floor);
		System.out.println("Elevator is moving to floor " + floor);
		try {
			Thread.sleep(Information.TRAVEL_TIME_PER_FLOOR * numFloorsToTravel);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		currentFloor = floor;
		//this.stationary = true;
	}

	/**
	 * This method implements the arrival sensor. It will send the arrivalSensor to the scheduler using 
	 * an RPC method. 
	 * 
	 * @param floor The floor where the arrival sensor was triggered
	 * @param elevator The elevator that triggered the sensor
	 * @param direction The direction of the elevator that triggered the sensor
	 * @return If there is no pending request at the current floor then it will return "ACK" otherwise
	 * it returns information about the request to be serviced.
	 */
	public String[] arrivalSensor(int floor, int elevator, String direction) {
		Date now = new Date();
	    String strDate = sdf.format(now);
	    
		byte[] message = pac.toBytes("arrivalSensor " + strDate + " " + floor + " " + elevator + " " + direction);
		//An RPC send to the scheduler with a 500ms timeout from a new socket which will be closed when the call completes
		byte[] data = rpc_send(schedulerPort, message, 500);

		String[] strData = null;
		try {
			strData = (new String(data, pac.getEncoding())).split(" ");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return strData;
	}

	/**
	 * This method returns the direction lamp of the elevator. Each elevator has a pair of two direction lamps
	 * 
	 * @param direction "UP" or "DOWN"
	 * @return If "UP", directionLamp at index 0, otherwise direction lamp at index 1
	 */
	public DirectionLamp getDirectionLamp(String direction) {
		if (direction.equals("UP"))
			return dirLamps[0];
		else
			return dirLamps[1];
	}

	/**
	 * This method returns the specified elevator button
	 * @param btnNumber The number of the elevator button
	 * @return The elevator button at btnNumber
	 */
	public ElevatorButton getElevatorButton(int btnNumber) {
		return btns[btnNumber - 1];
	}
	/**
	 * This methods sets the door state of the elevator
	 * @param doorState "OPEN" or "CLOSE"
	 */
	public void setDoorState(String doorState) {
		this.door = Door.valueOf(doorState);
	}

	/**
	 * This method returns the next floor to be visited by the elevator. If there are no floors
	 * the elevator will wait until a new floor is added to the array (by the elevatorSubsystem)
	 * 
	 * @return An integer representation of the next floor to be visited
	 */
	public synchronized int getNextFloor() {

		if (motor.equals(Motor.IDLE)) {
			while (floorsToVisit.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return floorsToVisit.get(0);
		}
		int closest = floorsToVisit.get(0);
		int distance = Math.abs(closest - currentFloor);

		for (int i = 1; i < floorsToVisit.size(); i++) {
			if (Math.abs(currentFloor - floorsToVisit.get(i)) < distance) {
				closest = floorsToVisit.get(i);
			}
		}

		return closest;
	}
	
	/**
	 * This method sets the motor state of the elevator
	 * @param motorState "UP", "DOWN", "IDLE"
	 */
	public void setMotorState(String motorState) {
		this.motor = Motor.valueOf(motorState);
	}
	
	/**
	 * This method adds a floor to the floorsToVisit array
	 * 
	 * @param floor To be visited
	 */
	public void addFloorToVisit(int floor) {
		floorsToVisit.add(floor);
	}
	
	/**
	 * Add a floor to visit and its corresponding destination to the floorDestinations hashmap
	 * 
	 * @param floor The floor to visit
	 * @param destination The destination the passenger wants to go to from this floor
	 */
	public void addDestination(int floor, int destination) {

		if (!floorDestinations.containsKey(floor)) {
			ArrayList<Integer> dest = new ArrayList<>();
			dest.add(destination);
			floorDestinations.put(floor, dest);
		} else {
			ArrayList<Integer> dest = floorDestinations.get(floor);
			dest.add(destination);
			floorDestinations.put(floor, dest);
		}
	}
	/**
	 * This method will move all of the destination floors (values) from the floorDestinations hashmap that correspond 
	 * with the given floor (the key) and move them to floors to be visited. And turn on the button for each new floor
	 * 
	 * @param floor The floor thats been visited
	 */
	public void moveDestToFloorsToVisit(int floor) {
		if (floorDestinations.containsKey(floor) && !floorDestinations.get(floor).isEmpty()) {
			ArrayList<Integer> dest = floorDestinations.get(floor);

			for (int i = 0; i < dest.size(); i++) {
				floorsToVisit.add(dest.get(i));
				
			}
		}
	}
	
	/**
	 * This method returns the current floor of the elevator
	 * @return the current floor
	 */
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	/**
	 * This method changes the current floor of the elevator
	 * @param floor The new floor of the elevator
	 */
	public void setCurrentFloor(int floor) {
		System.out.println("Current Floor: " + currentFloor);
		this.currentFloor = floor;
		System.out.println("New Floor: " + currentFloor);
		
	}
	
	/**
	 * The direction the elevator needs to go to get to the next floor
	 * 
	 * @param floorLevel The desired floor level
	 * @return The direction elevator needs to go
	 */
	public String getDirection(int floorLevel) {
		if (floorLevel < currentFloor) 
			return "DOWN";
		else if(floorLevel > currentFloor)
			return "UP";
		else
			return "IDLE";
	}
	
	/**
	 * This method returns the current motor state of the elevator
	 * @return the motor state, "UP", "DOWN" or "IDLE"
	 */
	public Motor getMotorDirection() {
		return motor;
	}

	/**
	 * This method removes the floor from the floorsToVisit array and moves any
	 * destination floors for the given floor over.
	 * 
	 * @param floor The floor the elevator just arrived at.
	 */
	public void arrivingAtFloor(int floor) {
		for (Iterator<Integer> i = floorsToVisit.iterator(); i.hasNext();) {
			Integer number = i.next();
			if (number == floor) {
				i.remove();
			}
		}
		moveDestToFloorsToVisit(floor);
	}
	
	/**
	 * This method returns the elevator number
	 * @return elevator number
	 */
	public int getElevatorNumber() {
		return this.elevatorNumber;
	}
	
	/**
	 * This method turns off a given button lamp. Is called when a elevator arrives at a floor.
	 * @param btnNumber The button to be turned off
	 */
	public void TurnOffButtonLamp(int btnNumber) {
		getElevatorButton(btnNumber).setLampState("OFF");
		System.out.println("Elevator" + getElevatorNumber() + " button " + btnNumber + " lamp off.");
	}
	
	public void ButtonPress(int floorLevel) {
		System.out.println("Elevator" + getElevatorNumber() + " Button: " + floorLevel + ", has been pressed");
		TurnOnButtonLamp(floorLevel); //Illuminate the designated floor button
	}
	
	public void TurnOnButtonLamp(int btnNumber) {
		getElevatorButton(btnNumber).setLampState("ON");
		System.out.println("Elevator" + getElevatorNumber() + " button " + btnNumber + " lamp on.");
	
	}
	
	public void TurnOnDirectionLamp(String direction) {
		getDirectionLamp(direction).setLamp("ON");
		System.out.println("Elevator" + getElevatorNumber() +  " " + direction + " button lamp on.");
	}
	
	public void TurnOffDirectionLamp(String direction) {
		getDirectionLamp(direction).setLamp("OFF");
		System.out.println("Elevator" + getElevatorNumber() +  " " + direction + " button lamp off.");
	}
	
	
	public void CloseDoors() {
		System.out.println("Elevator" + getElevatorNumber() + " door closing.");
		try {
			Thread.sleep(Information.TIME_CLOSE_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		setDoorState("CLOSE");
		System.out.println("Elevator" + getElevatorNumber() + " door closed.");
	}
	
	public void OpenDoors() {
		System.out.println("Elevator" + getElevatorNumber() + " door opening.");
		try {
			Thread.sleep(Information.TIME_OPEN_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		setDoorState("OPEN");
		System.out.println("Elevator" + getElevatorNumber() + " door opened.");
		
	}
	
	public int getMaxFloor() {
		return this.nFloors;
	}
	
	/**
	 * Overrides the run method of the Runnable interface. The floor requests from
	 * Scheduler are received if: the queue is not empty and the elevator is
	 * stationary. Changes the state of the elevator according to the logic
	 * presented.
	 */
	@Override
	public void run() {
		System.out.println("Elevator " + elevatorNumber + " is set up and ready to go");
		
		while (true) 
			this.elevatorState.Moving();
	}
}
