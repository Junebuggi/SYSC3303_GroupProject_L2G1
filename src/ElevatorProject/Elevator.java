package ElevatorProject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Elevator.java
 * 
 * The Elevator thread will try to get requests from the scheduler while it is stationary. 
 * It will then handle those requests and pass an acknowledgment to the Scheduler to be passed 
 * back to the floorSubsystem. It also changes the states of the state machine.
 *
 * @author Hasan Baig
 * @author Alden Wan Yeung Ng
 * @author Emma Boulay [iteration 3]
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class Elevator extends Network implements Runnable{

	// Current Elevator State
	private ElevatorState elevatorState;
	
	// All Concrete Elevator States
	private ElevatorState idle;
	private ElevatorState moving;
	private ElevatorState arrived;
	
	// Iteration 1 Variables
	public int schedulerPort;
	private byte[] currentRequest;
	private int elevatorNumber;
	private int currentFloor = 1;
	private Motor motor = Motor.IDLE;
	private Door door = Door.CLOSE;
	public enum Motor{
		UP, DOWN, IDLE;
	}
	public enum Door{
		OPEN, CLOSE;
	}
	private HashMap<Integer, ArrayList<Integer>> floorDestinations = new HashMap<Integer, ArrayList<Integer>>();
	private ArrayList<Integer> floorsToVisit = new ArrayList<>();
	private int nFloors;
	private ElevatorButton[] btns;
	public Boolean stationary = true;
	private DirectionLamp[] dirLamps;
	
   /**
	 * Constructor class used to initialize the object of the Elevator class.
	 * 
	 * @param scheduler			the schedule where the actions of the elevator are passed to
	 * @param elevatorNumber	the elevator number
	 */
	public Elevator(int elevatorNumber, int schedulerPort, int nFloors) {
		
		this.elevatorNumber = elevatorNumber;
		this.schedulerPort = schedulerPort;
		this.nFloors = nFloors;
		this.currentRequest = null;
		
		btns = createButtons();
		
		// Creating all concrete state objects
		this.idle = new IdleES(this);
		this.moving = new MovingES(this);
		this.arrived = new ArrivedES(this);
		
		// Default State to Idle
		elevatorState = idle;
		dirLamps = new DirectionLamp[] { new DirectionLamp("UP"), new DirectionLamp("DOWN") };
	}
	
	public ElevatorButton[] createButtons() {
		ElevatorButton[] btns = new ElevatorButton[nFloors];
		
		for(int i = 1; i <= nFloors; i++) {
			btns[i-1] = new ElevatorButton(i);
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
	
	public ElevatorState getIdleState() {return this.idle; }
	public ElevatorState getMovingState() {return this.moving; }
	public ElevatorState getArrivedState() {return this.arrived; }
	
	/**
	 * Method prints current elevator information.
	 */
	@Override
	public String toString() {
		String strRequest = new String(this.currentRequest);
		String[] parsedStr = strRequest.split(" ");
		return "Time: " + parsedStr[1] + "\nFloor: " + parsedStr[2] + "\nFloor Button: " + parsedStr[3] + "\nCar Button: " + parsedStr[4];
	}
	
	/**
	 * Method to move the elevator.
	 */
	public void move(int floor) {
		this.stationary = false;
		int numFloorsToTravel = Math.abs(currentFloor-floor);
    	System.out.println("Elevator is moving to floor " + floor);
		try {
			Thread.sleep(Information.TRAVEL_TIME_PER_FLOOR*numFloorsToTravel);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		currentFloor=floor;
		this.stationary = true;
	}
	
	/**
	 * Send Scheduler Request for Data
	 */
	public String[] arrivalSensor(int floor, int elevator, String direction) {
		byte[] message = pac.toBytes("arrivalSensor " + "hh:mm:ss.mmm " + floor + " " + elevator + " " + direction);
		byte[] data = rpc_send(schedulerPort, message, 500);

		String[] strData = null;
		try {
			strData = (new String(data, pac.getEncoding())).split(" ");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strData;
	}
	
	public DirectionLamp getDirectionLamp(String direction) {
		if(direction.equals("UP"))
			return dirLamps[0];
		else
			return dirLamps[1];
	}
	
	public ElevatorButton getElevatorButton(int btnNumber) {
		return btns[btnNumber-1];
	}
	
	public void setDoorState(String doorState) {
		this.door = Door.valueOf(doorState);
	}
	
	//To be revised
	public synchronized int getNextFloor() {
		int next = -1;
		
		if(motor.equals(Motor.IDLE)) {
			while(floorsToVisit.isEmpty()) {
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
		
		for(int i = 1; i < floorsToVisit.size(); i++) {
			if(Math.abs(currentFloor - floorsToVisit.get(i)) < distance) {
				closest = floorsToVisit.get(i);
			}
		}
		
		return closest;
	}
	
	public void setMotorState(String motorState) {
		this.motor = Motor.valueOf(motorState);
	}
	
	public void addFloorToVisit(int floor) {
		floorsToVisit.add(floor);
	}
	
	public ArrayList<Integer> getFloorToVisit() {
		return floorsToVisit;
	}
	
	public void addDestination(int floor, int destination) {
		
		if(!floorDestinations.containsKey(floor)) {
			ArrayList<Integer> dest = new ArrayList<>();
			dest.add(destination);
			floorDestinations.put(floor, dest);
		}
		else {
			ArrayList<Integer> dest = floorDestinations.get(floor);
			dest.add(destination);
			floorDestinations.put(floor, dest);
		}
	}
	
	public void moveDestToFloorsToVisit(int floor) {
		if(floorDestinations.containsKey(floor)) {
			ArrayList<Integer> dest = floorDestinations.get(floor);
			
			for(int i = 0; i < floorDestinations.size(); i++) {
				floorsToVisit.add(dest.get(i));
			}
		}
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}
	
	public String getDirection(int floorLevel) {
		if(floorLevel < currentFloor) {
			return "DOWN";
		}
		else
			return "UP";
	}
	
	public Motor getMotorDirection() {
		return motor;
	}
	
	public void arrivingAtFloor(int floor) {
	    for (Iterator<Integer> i = floorsToVisit.iterator(); i.hasNext();) {
	        Integer number = i.next();
	        if (number == floor) {
	            i.remove();
	        }
	    }
	    moveDestToFloorsToVisit(floor);
	}
	
	public int getElevatorNumber() {
		return this.elevatorNumber;
	}
	
	
	/**
	 * Overrides the run method of the Runnable interface.
	 * The floor requests from Scheduler are received if:
	 * 		the queue is not empty and
	 * 		the elevator is stationary.
	 * Changes the state of the elevator according to the logic presented.
	 */
	@Override
	public void run() {
		System.out.println("Elevator " + elevatorNumber + " is set up and ready to go");
		while (true) {

				this.elevatorState.Moving();
				
		}
			
	}
}
