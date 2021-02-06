package ElevatorProject;


public class Elevator implements Runnable {
	
//	private Integer[] elevatorButtonSelected = new Integer[NUM_FLOORS];	//set of elevator button to select a floor
//	private Integer[] elevatorLamp = new Integer[NUM_FLOORS];			//indicate the floor(s) which will be visited by the elevator
	
	private Scheduler scheduler;
	private byte[] currentRequest;
	private int elevatorNumber;
	private int currentFloor = 0;
	private Information.doorState doorState = Information.doorState.CLOSE;
	private Information.lampState lampState = Information.lampState.OFF;
	private int floorRequestedFrom;
	private int floorToVisit;
	private Boolean stationary = true;
	
   /**
	 * Constructor class used to initialize the object of the Elevator class.
	 * 
	 * @param schedule			the schedule where the actions of the elevator are passed to
	 * @param elevatorNumber	the elevator number
	 */
	public Elevator(Scheduler scheduler, int elevatorNumber) {
		this.scheduler = scheduler;
		this.currentRequest = null;
    this.elevatorNumber = elevatorNumber;
    floorRequestedFrom = -1;
    floorToVisit = -1;
	}
	/**
	 * Method prints current elevator information.
	 */
	public String toString() {
		String strRequest = new String(this.currentRequest);
		String[] parsedStr = strRequest.split(" ");
 		return "Time: " + parsedStr[0] + "\nFloor: " + parsedStr[1] + "\nFloor Button: " + parsedStr[2] + "\nCar Button: " + parsedStr[3];
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
		this.stationary = false;
		int numFloorsToTravel = Math.abs(currentFloor-floor);
    System.out.println("Elevator is moving to floor " + currentFloor);
		try {
			Thread.sleep(Information.TRAVEL_TIME_PER_FLOOR*numFloorsToTravel);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		currentFloor=floor;
		this.stationary = true;
	}
	
	/**
	 * Overrides the run method of the Runnable interface.
	 * The floor requests from Scheduler are received if:
	 * 		the queue is not empty and
	 * 		the elevator is stationary.
	 */	
	@Override
	public void run() {

		int i = 0;
		while (true) {
			synchronized (scheduler) {
				if (scheduler.isWork() && stationary) {
          
					this.currentRequest = (byte[])scheduler.getRequest();
					System.out.println("Elevator Subsystem:");
					System.out.println(this.toString());
					scheduler.acknowledgeRequest(("ACK " + elevatorNumber).getBytes());
				}
			}
					i++;
					if(i == 99) {
						System.exit(0);
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
          
				
		}

	}

}
