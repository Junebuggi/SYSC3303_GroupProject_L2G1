package ElevatorProject;

/**
 * Elevator.java
 * 
 * The Elevator thread will try to get requests from the scheduler while it is stationary. 
 * It will then handle those requests and pass an acknowledgment to the Scheduler to be passed 
 * back to the floorSubsystem. It also changes the states of the state machine.
 *
 * @author Hasan Baig
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class Elevator implements Runnable{

	// Current Elevator State
	private ElevatorState elevatorState;
	
	// All Concrete Elevator States
	private ElevatorState idle;
	private ElevatorState moving;
	private ElevatorState arrived;
	
	// Iteration 1 Variables
	public Scheduler scheduler;
	private byte[] currentRequest;
	private int elevatorNumber;
	private int currentFloor = 0;
	public Information.doorState doorState = Information.doorState.CLOSE;
	public Information.lampState lampState = Information.lampState.OFF;
	private int floorRequestedFrom;
	private int floorToVisit;
	public Boolean stationary = true;
	
   /**
	 * Constructor class used to initialize the object of the Elevator class.
	 * 
	 * @param scheduler			the schedule where the actions of the elevator are passed to
	 * @param elevatorNumber	the elevator number
	 */
	public Elevator(Scheduler scheduler, int elevatorNumber) {
		this.scheduler = scheduler;
		this.currentRequest = null;
		this.elevatorNumber = elevatorNumber;
		floorRequestedFrom = -1;
		floorToVisit = -1;
		
		// Creating all concrete state objects
		this.idle = new IdleES(this);
		this.moving = new MovingES(this);
		this.arrived = new ArrivedES(this);
		
		// Default State to Idle
		elevatorState = idle;
	}
	
	/**
	 * Returns State of this elevator
	 */
	public ElevatorState GetState() {
		return this.elevatorState;
	}
	
	/**
	 * Set the State of the Elevator
	 */
	public void SetState(ElevatorState newElevatorState) {
		elevatorState = newElevatorState;
	}
	
	public ElevatorState getIdleState() {return this.idle; }
	public ElevatorState getMovingState() {return this.moving; }
	public ElevatorState getArrivedState() {return this.arrived; }
	
	/**
	 * Method prints current elevator information.
	 */
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
	public void requestSchedulerforData() {
		synchronized (scheduler) {
			if (scheduler.isWork() && stationary) {
				this.currentRequest = (byte[])scheduler.getRequest();
				System.out.println("Elevator Subsystem:");
				System.out.println(this.toString());
			}
		}
	}

	public void sendAcknowledgement() {
		synchronized (scheduler) {
			scheduler.acknowledgeRequest(("ACK " + elevatorNumber).getBytes());
		}
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
		
		while (true) {
			synchronized (scheduler) {
				if (scheduler.isWork() && stationary) {
					
					// Invoke a request to scheduler for data and change its state to IDLE
					this.elevatorState.ReceivedRequest();
					
					// Start moving the elevator. Elevator is in MOVING state
					this.elevatorState.Moving();
				
					// Arrives at destination
					this.elevatorState.SendAck();
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}				
		}
	}
}
